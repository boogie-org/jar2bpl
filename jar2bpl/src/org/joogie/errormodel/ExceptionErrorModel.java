/**
 * 
 */
package org.joogie.errormodel;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.joogie.soot.SootProcedureInfo;
import org.joogie.soot.SootStmtSwitch;

import soot.SootClass;
import soot.jimple.Stmt;
import util.Log;
import boogie.expression.Expression;
import boogie.expression.IdentifierExpression;
import boogie.statement.IfStatement;
import boogie.statement.Statement;

/**
 * @author martin
 *
 */
public class ExceptionErrorModel extends AbstractErrorModel {

	/* exceptionWitnesses stores all the statements that are created when an
	 * exception is thrown. Further, for each such statement, it stores a tuple
	 * of the SootClass of the exception, and the Statement which throws the
	 * exception. That is, once we know which of the Keys in this map are infeasible
	 * we can remove them and all Statements that do not occur in any tuple are
	 * safe. 
	 */
	public HashMap<Statement, Entry<SootClass, Stmt>> exceptionWitnesses = new HashMap<Statement, Entry<SootClass, Stmt>>(); 	
	/*
	 * safetyWitnesses is like exceptionWitnesses but for safe executions. This one
	 * is used later to prove that a statement must throw an exception.
	 */
	public HashMap<Statement, Entry<SootClass, Stmt>> safetyWitnesses = new HashMap<Statement, Entry<SootClass, Stmt>>();
	/*
	 * also part of the hack ... 
	 */
	public HashSet<IfStatement> createdIfStatements = new HashSet<IfStatement>();
	
	public HashMap<Statement, Entry<SootClass, Stmt>> getExceptionWitnesses() {
		return exceptionWitnesses;
	}


	public ExceptionErrorModel(SootProcedureInfo pinfo, SootStmtSwitch stmtswitch) {
		super(pinfo, stmtswitch);
	}
	
	@Override
	public void createdExpectedException(Expression guard, SootClass exception) {		
		this.createGuardedException(guard, exception, true);
	}
	
	@Override
	public void createdUnExpectedException(Expression guard, SootClass exception) {
		this.createGuardedException(guard, exception, false);
	}
		
	protected void createGuardedException(Expression guard, SootClass exception, boolean expected) {		
		if (guard!=null) {
			SootStmtSwitch elsestmts = new SootStmtSwitch(this.procInfo); 				
			IdentifierExpression exceptionvar = elsestmts.createAllocatedVariable( exception.getType());
			Statement transferStatement = this.pf.mkReturnStatement();
			//collect the create statements
			LinkedList<Statement> elseblock = elsestmts.popAll();
			//create the transfer command
			//"goto" if this exception is caught somewhere
			//"return" otherwise.
			Statement assign = this.pf.mkAssignmentStatement( this.procInfo.getExceptionVariable(), exceptionvar);			
			elseblock.add(assign);			

			//this one is only needed to have a statement that we can track in the then part of the exception handling.
			//!! use an identity statement to make sure that no execution is altered here.
			Statement assign_then = this.pf.mkAssignmentStatement(this.procInfo.getExceptionVariable(), this.procInfo.getExceptionVariable());			
			/*
			 * add the exception to exceptionWitnesses and safetyWitnesses only if we
			 * do not expect it (i.e., it is not caught or in the throws clause).
			 */
			if (!expected) {
//				elseblock.add(
//						GlobalsCache.v().getPf().mkAssignmentStatement( 
//								procInfo.getExceptionalReturnFlag(), 
//								GlobalsCache.v().getPf().mkBooleanLiteral(true))
//					);

				//store all statements that are created for throwing an exception
				//if any of those is feasible, then the exception might be thrown.
				for (Statement st : elseblock) {
					exceptionWitnesses.put(st, new AbstractMap.SimpleEntry<SootClass, Stmt>(exception, this.stmtSwitch.getCurrentStatement()));
				}				
				safetyWitnesses.put(assign_then, new AbstractMap.SimpleEntry<SootClass, Stmt>(exception, this.stmtSwitch.getCurrentStatement()));
			}			
			
			//set the flag that an exception was thrown.
			elseblock.add(transferStatement);

			Statement[] thenPart = {assign_then};
			Statement[] elsePart = elseblock.toArray(new Statement[elseblock.size()]);
			
			
			IfStatement ifstmt = (IfStatement) this.pf.mkIfStatement(guard, thenPart, elsePart);
			this.createdIfStatements.add(ifstmt);
			this.stmtSwitch.addStatement(ifstmt);
		} else {
			Log.error("TODO: unguarded exceptions are not really implemented");
			this.stmtSwitch.addStatement(this.pf.mkReturnStatement());
		}
	}
	
}
