/**
 * 
 */
package org.joogie.errormodel;

import java.util.Collection;

import org.joogie.GlobalsCache;
import org.joogie.soot.SootPrelude;
import org.joogie.soot.SootProcedureInfo;
import org.joogie.soot.SootStmtSwitch;
import org.joogie.util.TranslationHelpers;

import soot.Scene;
import soot.SootClass;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph.ExceptionDest;
import boogie.ProgramFactory;
import boogie.ast.Attribute;
import boogie.enums.BinaryOperator;
import boogie.expression.Expression;
import boogie.statement.Statement;


/**
 * @author martin
 *
 */
public abstract class AbstractErrorModel {

	protected SootProcedureInfo procInfo;
	protected SootStmtSwitch stmtSwitch;
	protected ProgramFactory pf;

	public AbstractErrorModel(SootProcedureInfo pinfo, SootStmtSwitch stmtswitch) {
		this.procInfo = pinfo;
		this.pf = GlobalsCache.v().getPf();
		this.stmtSwitch = stmtswitch;
	}
	
	/**
	 * Called if an exception is thrown that also occurs in the throws clause
	 * @param guard the condition which must be violated in order to have the exception thrown. 
	 * May be null if the exception is always thrown.
	 * @param exception
	 */
	abstract public void createdExpectedException(Expression guard, SootClass exception);	

	/**
	 * Called if an exception is thrown that also occurs in NOT the throws clause
	 * @param guard the condition which must be violated in order to have the exception thrown. 
	 * May be null if the exception is always thrown.
	 * @param exception
	 */	
	abstract public void createdUnExpectedException(Expression guard, SootClass exception);
	
	protected void createException(Expression guard, SootClass exception) {
					
		String transferlabel = null;
		//check if there is a trap catching this exception		
		Collection<ExceptionDest> exceptionalSuccs = this.procInfo.getExceptionalUnitGraph().getExceptionDests(this.stmtSwitch.getCurrentStatement());
		
		for (ExceptionDest dest : exceptionalSuccs) {
			if (dest.getTrap()!=null) {
				//check if this trap can actually handle this exception
				if (GlobalsCache.v().isSubTypeOrEqual(exception, dest.getTrap().getException())) {
					transferlabel = GlobalsCache.v().getUnitLabel((Stmt) dest.getTrap().getHandlerUnit());
					break;
				}				
			}
		}		
		if (transferlabel==null) {
			//if not, check if the exception is in the throws clause
			if (GlobalsCache.v().inThrowsClause(exception, this.procInfo)) {
				createdExpectedException(guard, exception);
			} else {
				createdUnExpectedException(guard, exception);
			}
		} else {
			//if the exception is caught, create a goto
			Statement transferStatement = this.pf.mkGotoStatement( transferlabel);
			//now assign the exception variable to make sure that the catch block 
			//is feasible when we transfer there.
			Expression exception_type = GlobalsCache.v().lookupClassVariable(exception);
			Statement raise = SootPrelude.v().newObject(new Attribute[]{}, this.procInfo.getExceptionVariable(), exception_type);
			//if the exception is guarded create a conditional choice, otherwise just throw it.
			if (guard!=null) {				
				Statement[] elsePart = {this.pf.mkAssertStatement(new Attribute[]{pf.mkNoCodeAttribute()}, pf.mkBooleanLiteral(true)), raise, transferStatement};
				Statement[] thenPart = {this.pf.mkAssertStatement(TranslationHelpers.javaLocation2Attribute(this.stmtSwitch.getCurrentStatement().getTags()), pf.mkBooleanLiteral(true)) };		
				this.stmtSwitch.addStatement(this.pf.mkIfStatement( guard, thenPart, elsePart));					
			} else {
				this.stmtSwitch.addStatement(raise);
				this.stmtSwitch.addStatement(transferStatement);
			}
		}
	}
	
	public Statement createAssumeNonNull(Expression expr) {
		Expression guard = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPNEQ, expr,
				SootPrelude.v().getNullConstant());		
		return this.pf.mkAssumeStatement(new Attribute[0], guard);	
	}
	
	public void createUnguardedException(SootClass exception) {
		createException(null, exception);
	}
	
	public void createNonNullViolationException(Expression expr) {
		Expression guard = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPNEQ, expr,
				SootPrelude.v().getNullConstant());
		//TODO: it is actually not a java.lang.RuntimeException ... but I don't have anything usefull to report!
		createException(guard, Scene.v().loadClass("java.lang.RuntimeException", SootClass.SIGNATURES));
	}

	public void createPreconditionViolationException(Expression expr) {
		//TODO: it is actually not a java.lang.RuntimeException ... but I don't have anything usefull to report!
		createException(expr, Scene.v().loadClass("java.lang.RuntimeException", SootClass.SIGNATURES));
	}

	public void createPostconditionViolationException(Expression expr) {
		//TODO: it is actually not a java.lang.RuntimeException ... but I don't have anything usefull to report!
		createException(expr, Scene.v().loadClass("java.lang.RuntimeException", SootClass.SIGNATURES));
	}
	
	
	public void createArrayBoundGuard(Expression baseExpression, Expression indexExpression) {		
		Expression upperbound = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPLT, indexExpression,
				GlobalsCache.v().getArraySizeExpression(baseExpression));

		Expression lowerbound = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPGEQ, indexExpression,
				this.pf.mkIntLiteral( "0"));

		Expression guard = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.LOGICAND, upperbound,
				lowerbound);
		createException(guard, Scene.v().loadClass("java.lang.ArrayIndexOutOfBoundsException", SootClass.SIGNATURES));
	}
	
	public void createNonNullGuard(Expression expr) {
		Expression guard = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPNEQ, expr,
				SootPrelude.v().getNullConstant());
		createException(guard, Scene.v().loadClass("java.lang.NullPointerException", SootClass.SIGNATURES));
	}
	
	public void createDivByZeroGuard(Expression expr) {
		Expression guard = this.pf.mkBinaryExpression(
				this.pf.getBoolType(), BinaryOperator.COMPNEQ, expr,
				this.pf.mkIntLiteral( "0"));
		createException(guard, Scene.v().loadClass("java.lang.ArithmeticException", SootClass.SIGNATURES));
	}
	
	public void createClassCastGuard(Expression subtype, Expression supertype) {		
		Expression guard = GlobalsCache.v().compareTypeExpressions(subtype, supertype);
		createException(guard, Scene.v().loadClass("java.lang.ClassCastException", SootClass.SIGNATURES));
	}
}
