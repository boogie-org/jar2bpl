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
import soot.Type;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import boogie.ProgramFactory;
import boogie.ast.Attribute;
import boogie.enums.BinaryOperator;
import boogie.enums.UnaryOperator;
import boogie.expression.Expression;
import boogie.expression.IdentifierExpression;
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
	private boolean inMonitor = false;

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

	public SootProcedureInfo getProcInfo() {
		return this.procInfo;
	}
	
	public LinkedList<Statement> popAll() {
		LinkedList<Statement> ret = new LinkedList<Statement>();
		ret.addAll(this.boogieStatements);
		this.boogieStatements.clear();
		return ret;
	}

	public SootValueSwitch getValueSwitch() {
		return this.valueswitch;
	}
	
	public AbstractErrorModel getErrorModel() {
		return this.errorModel;
	}

	/**
	 * Returns true if we are in a monitor or if the whole method is synchronized.
	 * @return
	 */
	public boolean isInMonitor() {
		return this.inMonitor || this.procInfo.getSootMethod().isSynchronized();
	}
	
	private LinkedList<Statement> boogieStatements = new LinkedList<Statement>();

	/**
	 * this should only be used by the SootValueSwitch if extra guards have to
	 * be created
	 * 
	 * @param guard
	 */
	public void addStatement(Statement guard) {
		this.boogieStatements.add(guard);
	}

	public Stmt getCurrentStatement() {
		return this.currentStatement;
	}

	private void injectLabelStatements(Stmt arg0) {
		this.currentStatement = arg0;
		if (arg0.getBoxesPointingToThis().size() > 0) {
			String label = GlobalsCache.v()
					.getUnitLabel(arg0);
//			if (label.equals("block3")) {
//				System.err.println(arg0);
//			}
			this.boogieStatements.add(this.pf.mkLabel(label));
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
		AssignmentTranslation.translateAssignment(this, arg0.getLeftOp(), arg0.getRightOp(), arg0);
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
		AssignmentTranslation.translateAssignment(this, this.valueswitch.makeHeapAccessExpression(newexpr,
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

		AssignmentTranslation.translateAssignment(this, 
				this.valueswitch.getClassTypeFromExpression(newexpr, false),
				typeRhs);
		return newexpr;
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
		this.inMonitor = true;
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
		this.inMonitor = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
	 */
	@Override
	public void caseGotoStmt(GotoStmt arg0) {		;
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
		AssignmentTranslation.translateAssignment(this, arg0.getLeftOp(), arg0.getRightOp(), arg0);
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
		AssignmentTranslation.translateAssignment(this, null, arg0.getInvokeExpr(), arg0);
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
			AssignmentTranslation.translateAssignment(this, lhs, rhs);
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
		AssignmentTranslation.translateAssignment(this, this.procInfo.getExceptionVariable(), right);
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
