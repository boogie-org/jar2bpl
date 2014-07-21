/**
 * 
 */
package org.joogie.errormodel;

import org.joogie.soot.SootProcedureInfo;
import org.joogie.soot.SootStmtSwitch;
import org.joogie.util.TranslationHelpers;

import soot.SootClass;
import boogie.ast.Attribute;
import boogie.expression.Expression;
import boogie.statement.Statement;

/**
 * @author martin
 *
 */
public class AssertionErrorModel extends AbstractErrorModel {

	/**
	 * @param pinfo
	 * @param stmtswitch
	 */
	public AssertionErrorModel(SootProcedureInfo pinfo,
			SootStmtSwitch stmtswitch) {
		super(pinfo, stmtswitch);
	}
	
	
	public void createdExpectedException(Expression guard, SootClass exception) {
		//TODO:
		createdUnExpectedException(guard, exception);
	}
	
	public void createdUnExpectedException(Expression guard, SootClass exception) {
		Attribute[] attributes = TranslationHelpers.javaLocation2Attribute(this.stmtSwitch.getCurrentStatement().getTags());
		Statement assertion;
		if (guard!=null) {
			//assertion = this.pf.mkAssertStatement(loc,this.pf.mkUnaryExpression(loc, guard.getType(), UnaryOperator.LOGICNEG, guard));
			assertion = this.pf.mkAssertStatement(attributes,guard);
		} else {
			//assertion = this.pf.mkAssertStatement(loc,this.pf.mkBooleanLiteral(loc, false));
			//TODO:
			System.err.println("unguarded exception " + exception);
			assertion = this.pf.mkReturnStatement();
		}		
		this.stmtSwitch.addStatement(assertion);		
	}
	
	

}
