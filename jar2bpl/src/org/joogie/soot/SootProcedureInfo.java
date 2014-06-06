/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaeaef and Stephan Arlt
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.joogie.soot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.joogie.GlobalsCache;
import org.joogie.util.TranslationHelpers;

import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.jimple.ParameterRef;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import util.Log;
import boogie.declaration.Implementation;
import boogie.declaration.ProcedureDeclaration;
import boogie.enums.BinaryOperator;
import boogie.expression.Expression;
import boogie.expression.IdentifierExpression;
import boogie.location.ILocation;
import boogie.specification.Specification;
import boogie.type.BoogieType;

/**
 * @author martin
 * 
 */
public class SootProcedureInfo {

	private HashMap<Local, IdentifierExpression> localVariable = new HashMap<Local, IdentifierExpression>();
	private LinkedList<IdentifierExpression> inParameters;
	private LinkedList<IdentifierExpression> outParameters;
	private LinkedList<Specification> specification;
	private IdentifierExpression returnVariable;
	private IdentifierExpression exceptionVariable;
	private IdentifierExpression exceptionalReturnFlag = null;
	
	private IdentifierExpression containingClassVariable;

	private ExceptionalUnitGraph exceptionalUnitGraph;

	private IdentifierExpression thisVariable;
	private SootMethod sootMethod;
	private ILocation methodLocation;
	private String cleanName;

	private ProcedureDeclaration procedureDeclaration;
	private Implementation boogieProcedure = null;
	
	public boolean nonNullReturn = false; //TODO: hack
	public boolean exactReturnType = false; //TODO: hack to ensure the return type of String funs
	
	
	public Expression returnTypeVariable = null;
	
	public SootProcedureInfo(SootMethod m) {
		this.sootMethod = m;
		this.specification = new LinkedList<Specification>();
		this.methodLocation = TranslationHelpers
				.translateLocation(this.sootMethod.getTags());
		this.cleanName = TranslationHelpers.getQualifiedName(this.sootMethod);
		this.inParameters = new LinkedList<IdentifierExpression>();
		this.containingClassVariable = GlobalsCache.v().lookupClassVariable(
				this.sootMethod.getDeclaringClass());

		
		if (!this.sootMethod.isStatic()) {
			this.thisVariable = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(null,
							SootPrelude.v().getReferenceType(), "$this", false,
							false, false);
			this.inParameters.add(this.thisVariable);
		} else {
			this.thisVariable = null;
		}
		
		//collect the annotations
		LinkedList<LinkedList<SootAnnotations.Annotation>> pannot = SootAnnotations.parseParameterAnnotations(this.sootMethod);
		
		for (int i = 0; i < this.sootMethod.getParameterCount(); i++) {
			BoogieType type = GlobalsCache.v().getBoogieType(
					this.sootMethod.getParameterType(i));
						
			String param_name = "$in_parameter__" + i;
			IdentifierExpression id = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(this.methodLocation, type,
							param_name, false, false, false);
			this.inParameters.add(id);

			
			//add precondition if parameter has @NonNull annotation
			if (pannot.size()!=0) { //may be null if there are no annotations at all
				LinkedList<SootAnnotations.Annotation> annot = pannot.get(i);
				if (annot.contains(SootAnnotations.Annotation.NonNull)) {
					Expression formula = GlobalsCache
							.v()
							.getPf().mkBinaryExpression(methodLocation, 
									GlobalsCache.v().getPf().getBoolType(), 
									BinaryOperator.COMPNEQ, id, SootPrelude.v().getNullConstant()); 
					
					this.specification.add(GlobalsCache
					.v()
					.getPf().mkRequiresSpecification(
							methodLocation, 
							false, 
							formula)
							);
				}
			}

		}

		this.outParameters = new LinkedList<IdentifierExpression>();
		if (!(this.sootMethod.getReturnType() instanceof VoidType)) {
			
			Type returntype = this.sootMethod.getReturnType();
			BoogieType type = GlobalsCache.v().getBoogieType(
					returntype);
			String param_name = "$return";
			IdentifierExpression id = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(this.methodLocation, type,
							param_name, false, false, false);
			this.outParameters.add(id);
			this.returnVariable = id;
			
			//compute the type variable if the method return a reference type
			//this is used to ensure the type in assume-guarantee reasoning
			this.returnTypeVariable = null;
			if (returntype instanceof RefType) {
				RefType rt = (RefType)returntype;
				this.returnTypeVariable = GlobalsCache.v().lookupClassVariable(rt.getSootClass());
			}
			
			// only makes sense to add non-null annotation for non-prim types
			if (!(returntype instanceof PrimType)) {
			    //check if the procedure retrun has @NonNull annotation
			    LinkedList<SootAnnotations.Annotation> annot = null; 
			    for (Tag tag : this.sootMethod.getTags()) {
				if (tag instanceof VisibilityAnnotationTag) {
				    if (annot!=null) {
					Log.error("Didn't expect so many tags for procedure! Check that!");
					break;
				    }
				    annot = SootAnnotations.parseAnnotations((VisibilityAnnotationTag)tag);
				}
			    }
			    if ( (annot!=null && annot.contains(SootAnnotations.Annotation.NonNull))
				 || SootAnnotations.inHackedListOfMethodsThatReturnNonNullValues(this.sootMethod)
				 ){
				this.nonNullReturn = true;
				this.exactReturnType = SootAnnotations.inHackedListOfMethodsThatReturnNonNullValues(this.sootMethod);
				Expression formula = GlobalsCache
				    .v()
				    .getPf().mkBinaryExpression(methodLocation, 
								GlobalsCache.v().getPf().getBoolType(), 
								BinaryOperator.COMPNEQ, this.returnVariable, SootPrelude.v().getNullConstant()); 
				this.specification.add(GlobalsCache
						       .v()
						       .getPf().mkEnsuresSpecification(
										       methodLocation, 
										       false, 
										       formula)
						       );
			    }
			}
		} else {
			this.returnVariable = null;
		}
		/*
		 * We create an additional out variable called $exception. This variable
		 * is used to handle all kinds of exceptions that may be thrown in the
		 * procedure.
		 */
		String exname = "$exception";
		IdentifierExpression id = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(this.methodLocation,
						SootPrelude.v().getReferenceType(), exname, false,
						false, false);
		this.outParameters.add(id);
		this.exceptionVariable = id;

		Specification[] spec = this.specification.toArray(new Specification[this.specification.size()]);

		this.procedureDeclaration = GlobalsCache
				.v()
				.getPf()
				.mkProcedureDeclaration(this.methodLocation, this.cleanName,
						this.getInParamters(), this.getOutParamters(), spec);

		// now create the exceptional unit graph, which will be used later to
		// check
		// where throw statements can jump to
		if (this.sootMethod.hasActiveBody()) {
			this.exceptionalUnitGraph = new ExceptionalUnitGraph(
					this.sootMethod.getActiveBody(), UnitThrowAnalysis.v());
			//if the procedure has a body, create a Boolean local variable that is set
			//to true if the procedure returns exceptional
			this.exceptionalReturnFlag = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(this.methodLocation,
							GlobalsCache.v().getPf().getBoolType(), "$ex_return", false,
							false, false);
			
			this.fakeLocals.add(this.exceptionalReturnFlag);			
		} else {
			this.exceptionalUnitGraph = null;
		}
	}
	
	public IdentifierExpression getExceptionalReturnFlag() {
		return this.exceptionalReturnFlag;
	}
	
	public String getBoogieName() {
		return cleanName;
	}
	
	public List<SootClass> getThrowsClasses() {
		return this.sootMethod.getExceptions();
	}

	public IdentifierExpression[] getLocalVariables() {
		HashSet<IdentifierExpression> alllocals = new HashSet<IdentifierExpression>(
				this.localVariable.values());
		alllocals.addAll(this.fakeLocals);
		return alllocals.toArray(new IdentifierExpression[alllocals.size()]);
	}

	public IdentifierExpression[] getInParamters() {
		return this.inParameters
				.toArray(new IdentifierExpression[this.inParameters.size()]);
	}

	public IdentifierExpression[] getOutParamters() {
		return this.outParameters
				.toArray(new IdentifierExpression[this.outParameters.size()]);
	}

	public IdentifierExpression getReturnVariable() {
		return this.returnVariable;
	}

	public IdentifierExpression getExceptionVariable() {
		return this.exceptionVariable;
	}

	public IdentifierExpression getThisReference() {
		return thisVariable;
	}

	public IdentifierExpression getContainingClassVariable() {
		return containingClassVariable;
	}

	public ExceptionalUnitGraph getExceptionalUnitGraph() {
		return exceptionalUnitGraph;
	}

	public boolean isStatic() {
		return this.sootMethod.isStatic();
	}

	public IdentifierExpression lookupLocalVariable(Local local) {
		if (!this.localVariable.containsKey(local)) {
			ILocation loc = TranslationHelpers.createDummyLocation();
			BoogieType type = GlobalsCache.v().getBoogieType(local.getType());
			String cleanname = TranslationHelpers.getQualifiedName(local);
			IdentifierExpression id = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, type, cleanname, false, false,
							false);
			this.localVariable.put(local, id);
		}
		return this.localVariable.get(local);
	}

	private int fakeLocalCount = 0;
	private HashSet<IdentifierExpression> fakeLocals = new HashSet<IdentifierExpression>();

	public IdentifierExpression createLocalVariable(BoogieType type) {
		ILocation loc = TranslationHelpers.createDummyLocation();
		IdentifierExpression id = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, type,
						"$fakelocal_" + (fakeLocalCount++), false, false, false);
		this.fakeLocals.add(id);
		return id;
	}

	public IdentifierExpression lookupParameterVariable(ParameterRef param) {
		return this.inParameters.get(param.getIndex()
				+ ((this.sootMethod.isStatic()) ? 0 : 1));
	}

	public ProcedureDeclaration getProcedureDeclaration() {
		return this.procedureDeclaration;
	}

	public Implementation getBoogieProcedure() {
		return boogieProcedure;
	}

	public void setProcedureImplementation(Implementation impl) {
		this.boogieProcedure = impl;
	}

}
