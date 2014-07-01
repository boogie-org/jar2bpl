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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.joogie.GlobalsCache;
import org.joogie.errormodel.AbstractErrorModel;
import org.joogie.errormodel.AssertionErrorModel;
import org.joogie.errormodel.ExceptionErrorModel;
import org.joogie.util.Log;
import org.joogie.util.TranslationHelpers;

import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.toolkits.graph.ExceptionalUnitGraph.ExceptionDest;
import boogie.ProgramFactory;
import boogie.ast.Attribute;
import boogie.ast.VarList;
import boogie.enums.BinaryOperator;
import boogie.enums.UnaryOperator;
import boogie.expression.ArrayAccessExpression;
import boogie.expression.Expression;
import boogie.expression.IdentifierExpression;
import boogie.specification.RequiresSpecification;
import boogie.specification.Specification;
import boogie.statement.Statement;

/**
 * @author schaef
 */
public class SootStmtSwitch implements StmtSwitch {

	private SootProcedureInfo procInfo;
	private SootValueSwitch valueswitch;
	private ProgramFactory pf;
	private Stmt currentStatement = null; // needed to identify throw targets of
											// expressions
	private AbstractErrorModel errorModel;

	// used to track Java string constants
	// HashMap<StringConstant, Expression> stringConstantMap = new
	// HashMap<StringConstant, Expression>();

	public SootStmtSwitch(SootProcedureInfo pinfo) {
		this.procInfo = pinfo;
		this.pf = GlobalsCache.v().getPf();
		this.valueswitch = new SootValueSwitch(this.procInfo, this);
		if (org.joogie.Options.v().isExceptionErrorModel()) {
			this.errorModel = new ExceptionErrorModel(this.procInfo, this);
		} else {
			this.errorModel = new AssertionErrorModel(this.procInfo, this);
		}
	}

	public LinkedList<Statement> popAll() {
		LinkedList<Statement> ret = new LinkedList<Statement>();
		ret.addAll(this.boogieStatements);
		this.boogieStatements.clear();
		return ret;
	}

	public AbstractErrorModel getErrorModel() {
		return this.errorModel;
	}

	private LinkedList<Statement> boogieStatements = new LinkedList<Statement>();

	/**
	 * this should only be used by the SootValueSwitch if extra guards have to
	 * be created
	 * 
	 * @param guard
	 */
	public void addGuardStatement(Statement guard) {
		this.boogieStatements.add(guard);
	}

	public Stmt getCurrentStatement() {
		return this.currentStatement;
	}

	private void injectLabelStatements(Stmt arg0) {
		this.currentStatement = arg0;
		if (arg0.getBoxesPointingToThis().size() > 0) {
			this.boogieStatements.add(this.pf.mkLabel(GlobalsCache.v()
					.getUnitLabel(arg0)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseAssignStmt(soot.jimple.AssignStmt)
	 */
	@Override
	public void caseAssignStmt(AssignStmt arg0) {
		injectLabelStatements(arg0);
		translateAssignment(arg0.getLeftOp(), arg0.getRightOp(), arg0);
	}

	public IdentifierExpression createAllocatedVariable(Type sootType) {
		// create fresh local variable for "right"
		Attribute[] attributes = {};
		if (this.currentStatement != null) {
			TranslationHelpers.javaLocation2Attribute(this.currentStatement
					.getTags());
		}

		IdentifierExpression newexpr = this.procInfo
				.createLocalVariable(SootPrelude.v().getReferenceType());
		// havoc right
		this.boogieStatements
				.add(this.pf.mkHavocStatement(attributes, newexpr));
		// assume $heap[right, $alloc] == false
		this.boogieStatements.add(this.pf.mkAssumeStatement(attributes, this.pf
				.mkUnaryExpression(

				this.pf.getBoolType(), UnaryOperator.LOGICNEG, this.valueswitch
						.makeHeapAccessExpression(newexpr, SootPrelude.v()
								.getFieldAllocVariable(), false))));
		// $heap[right, $alloc] := true
		translateAssignment(this.valueswitch.makeHeapAccessExpression(newexpr,
				SootPrelude.v().getFieldAllocVariable(), false),
				this.pf.mkBooleanLiteral(true));

		// $heap[right, $type] := ...the appropriate type...
		Expression typeRhs;
		if (sootType instanceof RefType) {
			typeRhs = GlobalsCache.v().lookupClassVariable(
					((RefType) sootType).getSootClass());
			if (typeRhs == null) {
				throw new RuntimeException("Not a class variable: "
						+ ((RefType) sootType).getSootClass());
			}
		} else if (sootType instanceof ArrayType) {
			typeRhs = GlobalsCache.v().lookupArrayType((ArrayType) sootType);
			if (typeRhs == null) {
				throw new RuntimeException("Not a type: "
						+ (ArrayType) sootType);
			}

		} else {
			throw new RuntimeException("Translation of Array Access failed!");
		}

		this.boogieStatements.add(this.pf.mkAssumeStatement(attributes, this.pf
				.mkBinaryExpression(this.pf.getBoolType(),
						BinaryOperator.COMPNEQ, newexpr, SootPrelude.v()
								.getNullConstant())));

		translateAssignment(
				this.valueswitch.getClassTypeFromExpression(newexpr, false),
				typeRhs);
		return newexpr;
	}

	private void translateAssignment(Value lhs, Value rhs, Unit statement) {

		if (rhs instanceof InvokeExpr) {
			InvokeExpr ivk = (InvokeExpr) rhs;
			translateInvokeAssignment(lhs, ivk, statement);
			return;
		}
		lhs.apply(this.valueswitch);
		Expression left = this.valueswitch.getExpression();

		Expression right;
		if (rhs instanceof NewExpr) {
			right = createAllocatedVariable(((NewExpr) rhs).getBaseType());
		} else if (rhs instanceof NewArrayExpr) {
			NewArrayExpr nae = (NewArrayExpr) rhs;
			right = createAllocatedVariable(nae.getType());
			nae.getSize().apply(this.valueswitch);
			Expression sizeexp = this.valueswitch.getExpression();
			// add the size expression.
			this.boogieStatements.add(GlobalsCache.v().setArraySizeStatement(
					right, sizeexp));
		} else if (rhs instanceof NewMultiArrayExpr) {
			NewMultiArrayExpr nmae = (NewMultiArrayExpr) rhs;
			for (int i = 0; i < nmae.getSizeCount(); i++) {
				nmae.getSize(i).apply(this.valueswitch);
				// Expression sizeexp = this.valueswitch.getExpression();
				// TODO
				Log.error("Mulit-arrays are not implemented!");
			}
			right = GlobalsCache.v().makeFreshGlobal(
					SootPrelude.v().getReferenceType(), true, true);
		} else if (rhs instanceof StringConstant) {
			StringConstant str = (StringConstant) rhs;

			// right = this.stringConstantMap.get(str);
			right = createAllocatedVariable(rhs.getType());

			Expression[] indices = { right };
			// assign the size of the string to the appropriate field in the
			// $stringSizeHeapVariable array.
			translateAssignment(SootPrelude.v().getStringSizeHeapVariable(),
					this.pf.mkArrayStoreExpression(SootPrelude.v()
							.getStringSizeHeapVariable().getType(), SootPrelude
							.v().getStringSizeHeapVariable(), indices, this.pf
							.mkIntLiteral((new Integer(str.value.length()))
									.toString())));
		} else {
			rhs.apply(this.valueswitch);
			right = this.valueswitch.getExpression();
		}

		translateAssignment(left, right);

	}

	/**
	 * This method creates an assignment. It is used by caseAssignStmt and
	 * caseIdentityStmt
	 * 
	 * @param loc
	 * @param left
	 * @param right
	 */
	private void translateAssignment(Expression left, Expression right) {
		if (left instanceof IdentifierExpression) {
			this.boogieStatements.add(this.pf.mkAssignmentStatement(
					(IdentifierExpression) left,
					TranslationHelpers.castBoogieTypes(right, left.getType())));
		} else if (left instanceof ArrayAccessExpression) {
			ArrayAccessExpression aae = (ArrayAccessExpression) left;
			Expression arraystore = this.pf.mkArrayStoreExpression(aae
					.getArray().getType(), aae.getArray(), aae.getIndices(),
					right);
			translateAssignment(aae.getArray(), arraystore);
		} else {
			throw new RuntimeException("Unknown LHS type: "
					+ ((left == null) ? "null" : left.getClass()));
		}
	}

	/**
	 * create a CfgCallStatement
	 * 
	 * @param m
	 *            the procedure to be called
	 * @param throwsclauses
	 *            a list that contains where the possible exceptions of m a
	 *            added to. this is needed later.
	 * @param lefts
	 *            the left hand side of the call that receives the return values
	 */
	private LinkedList<Statement> createCallStatement(SootMethod m,
			List<SootClass> throwsclauses, List<IdentifierExpression> lefts,
			Expression[] args) {

		Attribute[] attributes = TranslationHelpers
				.javaLocation2Attribute(this.currentStatement.getTags());

		// we have to clone the lefts because we may add thing to it here.
		List<IdentifierExpression> lefts_clone = new LinkedList<IdentifierExpression>();
		for (IdentifierExpression ide : lefts) {
			lefts_clone.add(ide);
		}

		SootProcedureInfo calleeInfo = GlobalsCache.v().lookupProcedure(m);

		mergeThrowsClauses(throwsclauses, calleeInfo.getThrowsClasses());

		if (calleeInfo.getReturnVariable() != null && lefts_clone.size() == 0) {
			lefts_clone.add(this.procInfo.createLocalVariable(calleeInfo
					.getReturnVariable().getType()));
		}
		/*
		 * now add a fake local if the callee may throw an exception
		 */
		if (calleeInfo.getExceptionVariable() != null) {
			lefts_clone.add(this.procInfo.getExceptionVariable());
		}

		HashMap<String, Expression> substitutes = new HashMap<String, Expression>();
		for (int i = 0; i < calleeInfo.getProcedureDeclaration().getInParams().length; i++) {
			VarList vl = calleeInfo.getProcedureDeclaration().getInParams()[i];
			Expression arg = args[i];
			if (vl.getIdentifiers().length != 1) {
				throw new RuntimeException("That aint right!");
			}
			substitutes.put(vl.getIdentifiers()[0], arg);
		}

		for (Specification spec : calleeInfo.getProcedureDeclaration()
				.getSpecification()) {
			if (spec instanceof RequiresSpecification) {
				this.errorModel
						.createPreconditionViolationException(((RequiresSpecification) spec)
								.getFormula().substitute(substitutes));
			}
		}

		Statement s = this.pf.mkCallStatement(attributes, false, lefts_clone
				.toArray(new IdentifierExpression[lefts_clone.size()]),
				calleeInfo.getBoogieName(), args);

		LinkedList<Statement> stmts = new LinkedList<Statement>();
		stmts.add(s);

		return stmts;
	}

	/**
	 * For a given call lefts = c.m(args), this function checks if there are
	 * subclasses of "c" which overloads "m" with another procedure "m*". If so,
	 * we add something like... if (c instanceof C*) call lefts := m*(c, args)
	 * 
	 * @param loc
	 * @param m
	 *            (virtual) SootMethod
	 * @param c
	 *            the SootClass for which the method was called
	 * @param throwsclauses
	 * @param lefts
	 * @param args
	 * @return true if "m" is overloaded and statements have been created, and
	 *         "false" otherwise.
	 */
	private boolean createGuradedCallToVirtualMethods(SootMethod m,
			SootClass c, List<SootClass> throwsclauses,
			List<IdentifierExpression> lefts, Expression[] args) {

		// first find all possible subtypes of c
		@SuppressWarnings("rawtypes")
		Collection possibleClasses = null;
		if (c.isInterface()) {
			possibleClasses = Scene.v().getFastHierarchy()
					.getAllImplementersOfInterface(c);
		} else {
			possibleClasses = Scene.v().getFastHierarchy().getSubclassesOf(c);
		}

		boolean mayBeVirtual = possibleClasses != null
				&& !possibleClasses.isEmpty();

		boolean possiblyOverloaded = false;
		// iterate recursively over the subtypes of c

		if (mayBeVirtual) {
			for (Object o : possibleClasses) {
				SootClass child = (SootClass) o;
				// check if at least one subtype implements this function
				possiblyOverloaded = possiblyOverloaded
						|| createGuradedCallToVirtualMethods(m, child,
								throwsclauses, lefts, args);
			}
		}

		if (!m.isAbstract() && !m.isStatic()) {
			// arg[0] always stores the $this pointer for the call.
			// I.e., for a.foo() arg[0] contains "a"
			// this.valueswitch.getExprssionJavaClass(args[0]) then returns the
			// type variable for "a".
			Expression typeOfBase = this.valueswitch
					.getClassTypeFromExpression(args[0], false);
			Expression typeOfCurrentMethod = GlobalsCache.v()
					.lookupClassVariable(c);

			List<Statement> tmp = createCallStatement(m, throwsclauses, lefts,
					args);

			Statement[] call = tmp.toArray(new Statement[tmp.size()]);
			Statement ite = this.pf.mkIfStatement(GlobalsCache.v()
					.sameTypeExpression(typeOfBase, typeOfCurrentMethod), call,
					new Statement[0]);
			this.boogieStatements.add(ite);
			return true;
		}
		return possiblyOverloaded;
	}

	/**
	 * if actual clause contains sc or a supertype of sc, continue else add sc
	 * to actual clause.
	 * 
	 * @param actualclause
	 * @param addedclause
	 */
	private void mergeThrowsClauses(List<SootClass> actualclause,
			List<SootClass> addedclause) {

		for (SootClass sc : addedclause) {
			LinkedList<SootClass> superclasses = new LinkedList<SootClass>();
			SootClass tmp = sc;
			while (tmp != null) {
				superclasses.add(tmp);
				try {
					if (tmp.getName().equals("java.lang.Object")) {
						tmp = null;
					} else {
						tmp = tmp.getSuperclass();
					}
				} catch (Exception e) {
					throw new RuntimeException(tmp.getName() + " - "
							+ e.toString());
				}
			}

			boolean known = false;
			for (SootClass c : actualclause) {
				if (superclasses.contains(c)) {
					known = true;
					break;
				}

			}
			if (!known)
				actualclause.add(sc);
		}
	}

	private boolean specialCaseInvoke(Value lhs, InvokeExpr ivk) {
		Attribute[] attributes = TranslationHelpers
				.javaLocation2Attribute(this.currentStatement.getTags());
		// java.lang.String.length is treated as a special case:
		if (ivk.getMethod().getSignature()
				.contains("<java.lang.String: int length()>")
				&& lhs != null) {
			if (ivk instanceof SpecialInvokeExpr) {
				((SpecialInvokeExpr) ivk).getBase().apply(this.valueswitch);
			} else if (ivk instanceof VirtualInvokeExpr) {
				((VirtualInvokeExpr) ivk).getBase().apply(this.valueswitch);
			} else {
				throw new RuntimeException("Bad usage of String.length?");
			}
			Expression[] indices = { this.valueswitch.getExpression() };
			Expression right = this.pf.mkArrayAccessExpression(this.pf
					.getIntType(), SootPrelude.v().getStringSizeHeapVariable(),
					indices);

			lhs.apply(this.valueswitch);
			Expression left = this.valueswitch.getExpression();
			this.translateAssignment(left, right);
			return true;
		}

		if (ivk.getMethod().getSignature()
				.contains("<java.lang.System: void exit(int)>")) {
			Log.info("Surppressing false positive from call to System.exit");
			// this is not a return statement, it actually ends the application.
			this.boogieStatements.add(this.pf.mkAssumeStatement(attributes,
					this.pf.mkBooleanLiteral(false)));
			this.boogieStatements.add(this.pf.mkReturnStatement());
			return true;
		}
		return false;
	}

	/**
	 * Translates a jimple assignment that has an invoke on the right-hand side.
	 * We distinguish 2 special cases: String.length and System.exit. If the
	 * "ivk" is neither of them, this procedure translated the "lhs" and the
	 * call arguments and then calls
	 * 
	 * @param loc
	 * @param lhs
	 * @param ivk
	 * @param statement
	 */
	private void translateInvokeAssignment(Value lhs, InvokeExpr ivk,
			Unit statement) {
		// if the call is treated as a special case, return here.
		if (specialCaseInvoke(lhs, ivk))
			return;

		LinkedList<IdentifierExpression> lefts = new LinkedList<IdentifierExpression>();
		IdentifierExpression stubbedvar = null;
		Expression left = null;
		if (lhs != null) {
			lhs.apply(this.valueswitch);
			left = this.valueswitch.getExpression();
			if (left instanceof IdentifierExpression) {
				lefts.add((IdentifierExpression) left);
			} else {
				/*
				 * boogie doesn't allow you to put an array access as left hand
				 * side for a function call. So, if this happens, we add a fake
				 * local and assign it back after the call statement.
				 */
				stubbedvar = this.procInfo.createLocalVariable(left.getType());
				lefts.add(stubbedvar);
			}
		}

		List<SootClass> maxThrowSet = new LinkedList<SootClass>();

		SootMethod m = ivk.getMethod();

		int offset = (m.isStatic()) ? 0 : 1;
		Expression[] args = new Expression[ivk.getArgs().size() + offset];
		if (offset != 0) {
			args[0] = getCalleeInstance(ivk);
		}
		for (int i = 0; i < ivk.getArgs().size(); i++) {
			ivk.getArg(i).apply(this.valueswitch);
			args[i + offset] = this.valueswitch.getExpression();
		}

		SootClass c = null;
		if (!m.isStatic()) {

			if (ivk instanceof InterfaceInvokeExpr) {

				InterfaceInvokeExpr iivk = (InterfaceInvokeExpr) ivk;
				Type basetype = iivk.getBase().getType();

				if (basetype instanceof RefType) {
					RefType rt = (RefType) basetype;
					c = rt.getSootClass();
				} else if (basetype instanceof ArrayType) {
					c = Scene.v().loadClass("java.lang.reflect.Array",
							SootClass.SIGNATURES);
				} else {
					throw new RuntimeException(
							"Something wrong in translateInvokeAssignment: Expected RefType or ArrayType but found "
									+ basetype.getClass().toString());
				}
			} else if (ivk instanceof SpecialInvokeExpr) {
				// special invoke is only used for constructor calls.
				// don't check if the base is defined for constructor calls
				// Shystem.err.println("Special Call to : "+iivk.getMethod().getName());
			} else if (ivk instanceof VirtualInvokeExpr) {
				VirtualInvokeExpr iivk = (VirtualInvokeExpr) ivk;
				Type basetype = iivk.getBase().getType();

				if (basetype instanceof RefType) {
					RefType rt = (RefType) basetype;
					c = rt.getSootClass();
				} else if (basetype instanceof ArrayType) {
					c = Scene.v().loadClass("java.lang.reflect.Array",
							SootClass.SIGNATURES);
				} else {
					throw new RuntimeException(
							"Something wrong in translateInvokeAssignment: Expected RefType or ArrayType but found "
									+ basetype.getClass().toString());
				}
			} else {
				Log.error("Unhandeled invoke type: " + ivk.getType().getClass()
						+ " " + ivk.getMethod().toString());
			}
		}

		if (c != null) {
			if (!createGuradedCallToVirtualMethods(ivk.getMethod(), c,
					maxThrowSet, lefts, args)) {
				// if we cannot find any possible called method we just use the
				// one in ivk.getMethod which might be abstract
				this.boogieStatements.addAll(createCallStatement(
						ivk.getMethod(), maxThrowSet, lefts, args));
			}
		} else {
			// the procedure is static so we call it without checking if other
			// methods can be call depending on the type of c
			this.boogieStatements.addAll(createCallStatement(ivk.getMethod(),
					maxThrowSet, lefts, args));
		}
		// now check if the procedure returned exceptional
		// and jump to the appropriate location
		translateCalleeExceptions(maxThrowSet, statement);

		/*
		 * if the left-hand side was an array access and we introduced a helper
		 * variable, we create and assignment here that assigns this variable to
		 * the original LHS.
		 */
		if (stubbedvar != null) {
			translateAssignment(left, stubbedvar);
		}
	}

	/**
	 * (Only used after procedure calls) Inserts a switch case to check if the
	 * exception variable has been set to any of the elements in maxThrowSet or
	 * any other exception that "statement" may throw according to the Unit
	 * Graph. If so, we add either add a jump to the appropriate handler or
	 * return.
	 * 
	 * @param loc
	 * @param maxThrowSet
	 * @param statement
	 */
	private void translateCalleeExceptions(List<SootClass> maxThrowSet,
			Unit statement) {
		// keep track of the caught exceptions so that we can add the uncaught
		// ones later
		HashSet<SootClass> caughtExceptions = new HashSet<SootClass>();
		// now collect all exceptions that are in the exceptional unit graph
		// and their associated traps
		if (this.procInfo.getExceptionalUnitGraph()
				.getExceptionalSuccsOf(statement).size() != 0) {
			for (ExceptionDest dest : this.procInfo.getExceptionalUnitGraph()
					.getExceptionDests(statement)) {
				if (dest.getTrap() != null
						&& dest.getTrap().getException() != null) {
					caughtExceptions.add(dest.getTrap().getException());
					Statement transitionStmt;
					Expression condition = this.pf.mkBinaryExpression(this.pf
							.getBoolType(), BinaryOperator.COMPNEQ,
							this.procInfo.getExceptionVariable(), SootPrelude
									.v().getNullConstant());
					// the exception is caught somewhere in this procedure
					transitionStmt = this.pf.mkGotoStatement(

					GlobalsCache.v().getUnitLabel(
							(Stmt) dest.getTrap().getHandlerUnit()));
					// add a conjunct to check if that the type of the exception
					// is <: than the one caught
					// by the catch block
					condition = this.pf
							.mkBinaryExpression(

									this.pf.getBoolType(),
									BinaryOperator.LOGICAND,
									condition,
									this.pf.mkBinaryExpression(

											this.pf.getBoolType(),
											BinaryOperator.COMPPO,
											this.valueswitch
													.getClassTypeFromExpression(
															this.procInfo
																	.getExceptionVariable(),
															false),
											GlobalsCache
													.v()
													.lookupClassVariable(
															dest.getTrap()
																	.getException())));
					Statement[] thenPart = { transitionStmt };
					Statement[] elsePart = {};
					this.boogieStatements.add(this.pf.mkIfStatement(condition,
							thenPart, elsePart));
				} else {
					// Log.error("NO CATCH FOR "+ dest);
				}
			}
		}
		// now create a list of all exceptions that are thrown by the callee
		// but not caught by the procedure
		HashSet<SootClass> uncaughtException = new HashSet<SootClass>();
		for (SootClass sc : maxThrowSet) {
			boolean caught = false;
			for (SootClass other : caughtExceptions) {
				if (GlobalsCache.v().isSubTypeOrEqual(sc, other)) {
					caught = true;
					break;
				}
			}
			if (!caught && !uncaughtException.contains(sc)) {
				uncaughtException.add(sc);
			}
		}
		// now always pick the uncaught exception which has
		// no subclass in the hashset, create a conditional choice,
		// and remove it. This ordering is necessary, otherwise,
		// we might create dead code.
		LinkedList<SootClass> todo = new LinkedList<SootClass>(
				uncaughtException);
		while (!todo.isEmpty()) {
			SootClass current = todo.removeFirst();
			boolean good = true;
			for (SootClass other : todo) {
				if (current == other) {
					throw new RuntimeException("can't be");
				}
				if (GlobalsCache.v().isSubTypeOrEqual(other, current)) {
					good = false;
					break;
				}
			}
			if (!good) {
				todo.addLast(current);
				continue;
			}

			if (GlobalsCache.v().inThrowsClause(current, procInfo)
					|| org.joogie.Options.v().isRuntimeExceptionReturns()) {

				Statement transitionStmt;
				Expression condition = this.pf.mkBinaryExpression(this.pf
						.getBoolType(), BinaryOperator.COMPNEQ, this.procInfo
						.getExceptionVariable(), SootPrelude.v()
						.getNullConstant());

				// add a conjunct to check if that the type of the exception
				// is <: than the one caught
				// by the catch block
				condition = this.pf.mkBinaryExpression(this.pf.getBoolType(),
						BinaryOperator.LOGICAND, condition,
						this.pf.mkBinaryExpression(this.pf.getBoolType(),
								BinaryOperator.COMPPO,
								this.valueswitch.getClassTypeFromExpression(
										this.procInfo.getExceptionVariable(),
										false), GlobalsCache.v()
										.lookupClassVariable(current)));

				transitionStmt = this.pf.mkReturnStatement();
				Statement[] thenPart = { transitionStmt };
				Statement[] elsePart = {};
				this.boogieStatements.add(this.pf.mkIfStatement(condition,
						thenPart, elsePart));

			} else {
				Log.debug("The exception: " + current
						+ " is not handeled and treated as RuntimeException.");
			}

		}

	}

	private Expression getCalleeInstance(InvokeExpr ivk) {
		// if (ivk instanceof InstanceInvokeExpr) {
		// InstanceInvokeExpr iivk = (InstanceInvokeExpr) ivk;
		// iivk.getBase().apply(this.valueswitch);
		// Expression base = this.valueswitch.getExpression();
		//
		// return base;
		// } else
		if (ivk instanceof InterfaceInvokeExpr) {
			InterfaceInvokeExpr iivk = (InterfaceInvokeExpr) ivk;
			iivk.getBase().apply(this.valueswitch);
			Expression base = this.valueswitch.getExpression();
			this.errorModel.createNonNullGuard(base);
			return base;
		} else if (ivk instanceof SpecialInvokeExpr) {
			// special invoke is only used for constructor calls
			SpecialInvokeExpr iivk = (SpecialInvokeExpr) ivk;
			iivk.getBase().apply(this.valueswitch);
			return this.valueswitch.getExpression();
		} else if (ivk instanceof VirtualInvokeExpr) {
			VirtualInvokeExpr iivk = (VirtualInvokeExpr) ivk;
			iivk.getBase().apply(this.valueswitch);
			Expression base = this.valueswitch.getExpression();
			this.errorModel.createNonNullGuard(base);
			return base;
		}
		throw new RuntimeException(
				"Cannot compute instance for static or dynamic call");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
	 */
	@Override
	public void caseBreakpointStmt(BreakpointStmt arg0) {
		injectLabelStatements(arg0);
		Log.info("Joogie does not translate BreakpointStmt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
	 * If this is only for synchronization, we don't need to translate it
	 */
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt arg0) {
		injectLabelStatements(arg0);
		Log.info("Joogie does not translate EnterMonitor");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
	 * If this is only for synchronization, we don't need to translate it
	 */
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt arg0) {
		injectLabelStatements(arg0);
		Log.info("Joogie does not translate ExitMonitor");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
	 */
	@Override
	public void caseGotoStmt(GotoStmt arg0) {
		injectLabelStatements(arg0);
		String labelName = GlobalsCache.v().getUnitLabel(
				(Stmt) arg0.getTarget());
		this.boogieStatements.add(this.pf.mkGotoStatement(labelName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseIdentityStmt(soot.jimple.IdentityStmt)
	 */
	@Override
	public void caseIdentityStmt(IdentityStmt arg0) {
		injectLabelStatements(arg0);
		translateAssignment(arg0.getLeftOp(), arg0.getRightOp(), arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseIfStmt(soot.jimple.IfStmt)
	 */
	@Override
	public void caseIfStmt(IfStmt arg0) {
		injectLabelStatements(arg0);
		Statement[] thenPart = { this.pf.mkGotoStatement(GlobalsCache.v()
				.getUnitLabel(arg0.getTarget())) };
		Statement[] elsePart = {};
		arg0.getCondition().apply(this.valueswitch);
		Expression cond = TranslationHelpers.castBoogieTypes(
				this.valueswitch.getExpression(), this.pf.getBoolType());
		this.boogieStatements.add(this.pf.mkIfStatement(cond, thenPart,
				elsePart));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
	 */
	@Override
	public void caseInvokeStmt(InvokeStmt arg0) {
		injectLabelStatements(arg0);
		translateAssignment(null, arg0.getInvokeExpr(), arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt)
	 */
	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt arg0) {
		injectLabelStatements(arg0);
		LinkedList<Expression> cases = new LinkedList<Expression>();
		LinkedList<Statement[]> targets = new LinkedList<Statement[]>();

		arg0.getKey().apply(this.valueswitch);
		Expression key = this.valueswitch.getExpression();
		for (int i = 0; i < arg0.getTargetCount(); i++) {
			Expression cond = this.pf.mkBinaryExpression(

			this.pf.getBoolType(), BinaryOperator.COMPEQ, key, this.pf
					.mkIntLiteral(Integer.toString(arg0.getLookupValue(i))));
			cases.add(cond);
			Statement[] gototarget = { this.pf.mkGotoStatement(GlobalsCache.v()
					.getUnitLabel((Stmt) arg0.getTarget(i))) };
			targets.add(gototarget);
		}
		{
			Statement[] gototarget = { this.pf.mkGotoStatement(

			GlobalsCache.v().getUnitLabel((Stmt) arg0.getDefaultTarget())) };
			targets.add(gototarget);
		}
		translateSwitch(cases, targets);
	}

	/**
	 * note that there is one more target than cases because of the default
	 * cases
	 * 
	 * @param cases
	 * @param targets
	 */
	private void translateSwitch(LinkedList<Expression> cases,
			LinkedList<Statement[]> targets) {
		Statement[] elseblock = targets.getLast();
		Statement ifstatement = null;
		int max = cases.size() - 1;
		for (int i = max; i >= 0; i--) {
			Statement[] thenblock = targets.get(i);
			ifstatement = this.pf.mkIfStatement(cases.get(i), thenblock,
					elseblock);
			elseblock = new Statement[1];
			elseblock[0] = ifstatement;
		}
		if (ifstatement != null) {
			this.boogieStatements.add(ifstatement);
		} else {
			Log.info("Warning: Found empty switch statement (or only default case).");
			for (int i = 0; i < elseblock.length; i++) {
				this.boogieStatements.add(elseblock[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseNopStmt(soot.jimple.NopStmt)
	 */
	@Override
	public void caseNopStmt(NopStmt arg0) {
		injectLabelStatements(arg0);
		// Log.error("NopStmt: " + arg0.toString());
		// assert (false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseRetStmt(soot.jimple.RetStmt)
	 */
	@Override
	public void caseRetStmt(RetStmt arg0) {
		injectLabelStatements(arg0);
		Log.error("This is deprecated: " + arg0.toString());
		throw new RuntimeException(
				"caseRetStmt is not implemented. Contact developers!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
	 */
	@Override
	public void caseReturnStmt(ReturnStmt arg0) {
		injectLabelStatements(arg0);
		if (this.procInfo.getReturnVariable() != null) {
			Expression lhs = this.procInfo.getReturnVariable();
			arg0.getOp().apply(this.valueswitch);
			Expression rhs = this.valueswitch.getExpression();
			translateAssignment(lhs, rhs);
		}
		this.boogieStatements.add(this.pf.mkReturnStatement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
	 */
	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt arg0) {
		injectLabelStatements(arg0);
		this.boogieStatements.add(this.pf.mkReturnStatement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
	 * The TableSwitch is a special case of the LookupSwitch, where all cases
	 * are consecutive.
	 */
	@Override
	public void caseTableSwitchStmt(TableSwitchStmt arg0) {
		injectLabelStatements(arg0);
		LinkedList<Expression> cases = new LinkedList<Expression>();
		LinkedList<Statement[]> targets = new LinkedList<Statement[]>();

		arg0.getKey().apply(this.valueswitch);
		Expression key = this.valueswitch.getExpression();
		int counter = 0;
		for (int i = arg0.getLowIndex(); i <= arg0.getHighIndex(); i++) {
			Expression cond = this.pf.mkBinaryExpression(this.pf.getBoolType(),
					BinaryOperator.COMPEQ, key,
					this.pf.mkIntLiteral(Integer.toString(i)));
			cases.add(cond);
			Statement[] gototarget = { this.pf.mkGotoStatement(

			GlobalsCache.v().getUnitLabel((Stmt) arg0.getTarget(counter))) };
			targets.add(gototarget);
			counter++;
		}
		{
			Statement[] gototarget = { this.pf.mkGotoStatement(

			GlobalsCache.v().getUnitLabel((Stmt) arg0.getDefaultTarget())) };
			targets.add(gototarget);
		}
		translateSwitch(cases, targets);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
	 */
	@Override
	public void caseThrowStmt(ThrowStmt arg0) {
		injectLabelStatements(arg0);
		arg0.getOp().apply(this.valueswitch);
		Expression right = this.valueswitch.getExpression();
		// assign the value from arg0.getOp() to the $exception variable of
		// the current procedure.
		// Note that this only works because soot moves the "new" statement
		// to a new local variable.
		this.translateAssignment(this.procInfo.getExceptionVariable(), right);
		// Add a goto statement to the exceptional successors.
		List<Unit> exc_succ = procInfo.getExceptionalUnitGraph()
				.getExceptionalSuccsOf((Unit) arg0);
		String[] labels = new String[exc_succ.size()];
		if (exc_succ.size() > 0) {
			for (int i = 0; i < exc_succ.size(); i++) {
				labels[i] = GlobalsCache.v().getUnitLabel(
						(Stmt) exc_succ.get(i));
			}
			if (exc_succ.size() > 1) {
				// StringBuilder sb = new StringBuilder();
				// sb.append("Throw statement may jump to more than one location: "+this.procInfo.getBoogieName()
				// + ":"+this.currentStatement+"\n");
				// sb.append("Line "+loc.getStartLine()+"\n");
				// sb.append(arg0.getOp()+"\n");

				for (int i = 0; i < exc_succ.size(); i++) {
					Unit u = exc_succ.get(i);
					// sb.append("Line "+Util.findLineNumber(u.getTags())+
					// ": "+u+"\n");

					if (u instanceof IdentityStmt) {
						// sb.append("IdentityStmt ");
						IdentityStmt istmt = (IdentityStmt) u;
						if (istmt.getRightOp() instanceof CaughtExceptionRef) {
							// sb.append("... catches exception! " +
							// istmt.getLeftOp().getType()+"\n");
							Type caughttype = istmt.getLeftOp().getType();
							if (!(caughttype instanceof RefType)) {
								throw new RuntimeException(
										"Bug in translation of ThrowStmt!");
							}
							RefType caught = (RefType) caughttype;
							Expression cond = GlobalsCache
									.v()
									.compareTypeExpressions(
											this.valueswitch.getClassTypeFromExpression(
													right, false),
											GlobalsCache
													.v()
													.lookupClassVariable(
															caught.getSootClass()));
							Statement[] thenPart = new Statement[] { this.pf
									.mkGotoStatement(labels[i]) };
							Statement ifstmt = this.pf.mkIfStatement(cond,
									thenPart, new Statement[0]);
							// sb.append("created choice: "+ifstmt+"\n");
							this.boogieStatements.add(ifstmt);
						} else {
							throw new RuntimeException(
									"Bug in translation of ThrowStmt!");
						}
					} else {
						throw new RuntimeException(
								"Bug in translation of ThrowStmt!");
					}
				}
				// throw new RuntimeException(sb.toString());
				// Log.error(sb);
				// Make sure that the execution does not continue after the
				// throw statement
				this.boogieStatements.add(this.pf.mkReturnStatement());
			} else {
				this.boogieStatements.add(this.pf.mkGotoStatement(labels[0]));
			}
		} else {

			this.boogieStatements.add(this.pf.mkReturnStatement());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object arg0) {
		assert (false);
	}

}
