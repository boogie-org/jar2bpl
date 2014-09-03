/**
 * 
 */
package org.joogie.soot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.joogie.GlobalsCache;
import org.joogie.util.Log;
import org.joogie.util.TranslationHelpers;

import soot.Immediate;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import boogie.ProgramFactory;
import boogie.ast.Attribute;
import boogie.ast.VarList;
import boogie.ast.expression.Expression;
import boogie.ast.expression.IdentifierExpression;
import boogie.ast.statement.Statement;
import boogie.enums.BinaryOperator;

/**
 * @author schaef
 * 
 */
public class InvokeTranslation {

	public static void translateInvokeAssignment(SootStmtSwitch ss, Value lhs,
			InvokeExpr ivk, Unit statement) {
		if (specialCaseInvoke(ss, lhs, ivk))
			return;

		SootValueSwitch valueswitch = ss.getValueSwitch();
		SootProcedureInfo procInfo = ss.getProcInfo();

		// translate the left-hand side of the assignment (if there is one).
		LinkedList<IdentifierExpression> lefts = new LinkedList<IdentifierExpression>();
		IdentifierExpression stubbedvar = null;
		Expression left = null;
		if (lhs != null) {
			lhs.apply(valueswitch);
			left = valueswitch.getExpression();
			if (left instanceof IdentifierExpression) {
				lefts.add((IdentifierExpression) left);
			} else {
				/*
				 * boogie doesn't allow you to put an array access as left hand
				 * side for a function call. So, if this happens, we add a fake
				 * local and assign it back after the call statement.
				 */
				stubbedvar = procInfo.createLocalVariable(left.getType());
				lefts.add(stubbedvar);
			}
		}

		SootMethod m = ivk.getMethod();

		// translate the call args
		LinkedList<Expression> args = new LinkedList<Expression>();
		for (int i = 0; i < ivk.getArgs().size(); i++) {
			ivk.getArg(i).apply(valueswitch);
			args.add(valueswitch.getExpression());
		}

		// Translate base, if necessary.
		// SootClass baseClass = null;
		if (ivk instanceof InstanceInvokeExpr) {
			// this include Interface-, Virtual, and SpecialInvokeExpr

			InstanceInvokeExpr iivk = (InstanceInvokeExpr) ivk;
			// baseClass = getBaseClass(iivk.getBase().getType());

			iivk.getBase().apply(valueswitch);
			Expression base = valueswitch.getExpression();
			// add the "this" variable to the list of args
			args.addFirst(base);

			if (iivk.getBase() instanceof Immediate
					&& procInfo.getNullnessAnalysis().isAlwaysNonNullBefore(
							statement, (Immediate) iivk.getBase())) {
				// do not check
			} else {
				ss.getErrorModel().createNonNullViolationException(base);
			}

		} else if (ivk instanceof StaticInvokeExpr) {
			// Do nothing
		} else {
			throw new RuntimeException(
					"Cannot compute instance for static or dynamic call");
		}

		ss.addStatement(createCallStatement(ss, m, lefts,
				args.toArray(new Expression[args.size()])));

		Expression constructorInstance = null;
		if (m.isConstructor()) {
			// if it is a call to a constructor, we have to add
			// the condition that the object which is being constructed
			// will be set to null if the constructor terminates
			// with an exception.
			// That is, for: $exception = Object$init(base)
			// we create
			// if ($exception!=null && base!=this) base=null;
			constructorInstance = args.getFirst(); // the first arg must be the
													// pointer to that object
		}

		// now check if the procedure returned exceptional
		// and jump to the appropriate location
		translateCalleeExceptions(ss, statement, constructorInstance);

		/*
		 * if the left-hand side was an array access and we introduced a helper
		 * variable, we create and assignment here that assigns this variable to
		 * the original LHS.
		 */
		if (stubbedvar != null) {
			AssignmentTranslation.translateAssignment(ss, left, stubbedvar);
		}
	}

	// static private SootClass getBaseClass(Type type) {
	// SootClass c = null;
	// if (type instanceof RefType) {
	// RefType rt = (RefType) type;
	// c = rt.getSootClass();
	// } else if (type instanceof ArrayType) {
	// c = Scene.v().loadClass("java.lang.reflect.Array",
	// SootClass.SIGNATURES);
	// } else {
	// throw new RuntimeException(
	// "Something wrong in translateInvokeAssignment: Expected RefType or ArrayType but found "
	// + type.getClass().toString());
	// }
	// return c;
	// }

	static private boolean specialCaseInvoke(SootStmtSwitch ss, Value lhs,
			InvokeExpr ivk) {
		SootValueSwitch valueswitch = ss.getValueSwitch();
		ProgramFactory pf = GlobalsCache.v().getPf();
		// java.lang.String.length is treated as a special case:
		if (ivk.getMethod().getSignature()
				.contains("<java.lang.String: int length()>")
				&& lhs != null) {
			if (ivk instanceof SpecialInvokeExpr) {
				((SpecialInvokeExpr) ivk).getBase().apply(valueswitch);
			} else if (ivk instanceof VirtualInvokeExpr) {
				((VirtualInvokeExpr) ivk).getBase().apply(valueswitch);
			} else {
				throw new RuntimeException("Bad usage of String.length?");
			}
			Expression[] indices = { valueswitch.getExpression() };
			Expression right = pf.mkArrayAccessExpression(pf.getIntType(),
					SootPrelude.v().getStringSizeHeapVariable(), indices);

			lhs.apply(valueswitch);
			Expression left = valueswitch.getExpression();
			AssignmentTranslation.translateAssignment(ss, left, right);
			return true;
		}

		if (ivk.getMethod().getSignature()
				.contains("<java.lang.System: void exit(int)>")) {
			Log.info("Surppressing false positive from call to System.exit");
			// this is not a return statement, it actually ends the application.
			// ss.addStatement(pf.mkAssumeStatement(new
			// Attribute[]{pf.mkNoVerifyAttribute()},
			// pf.mkBooleanLiteral(false)));
			ss.addStatement(pf.mkReturnStatement());
			return true;
		}
		return false;
	}

	/**
	 * Create a Boogie call statement. The trick is, that, given a call f(x) we
	 * may have to create stubs for the return value of f and the exception
	 * thrown by f because in Boogie the number of lhs variables has to match
	 * the number of return variables. E.g. if f returns an int and throws
	 * exceptions, the call above would be translated to call ret, exc := f(x);
	 * where ret and exc are fresh local variables.
	 * 
	 * @param ss
	 * @param m
	 * @param throwsclauses
	 * @param lefts
	 * @param args
	 * @return
	 */
	static private Statement createCallStatement(SootStmtSwitch ss,
			SootMethod m, List<IdentifierExpression> lefts, Expression[] args) {

		ProgramFactory pf = GlobalsCache.v().getPf();

		// this is the procInfo for the procedure from which m is called
		SootProcedureInfo procInfo = ss.getProcInfo();
		// this is the procInfo for the called procedure m
		SootProcedureInfo calleeInfo = GlobalsCache.v().lookupProcedure(m);

		Attribute[] attributes = TranslationHelpers.javaLocation2Attribute(ss
				.getCurrentStatement());

		// we have to clone the lefts because we may add thing to it here.
		List<IdentifierExpression> lefts_clone = new LinkedList<IdentifierExpression>();
		for (IdentifierExpression ide : lefts) {
			lefts_clone.add(ide);
		}

		if (calleeInfo.getReturnVariable() != null && lefts_clone.size() == 0) {
			lefts_clone.add(procInfo.createLocalVariable(calleeInfo
					.getReturnVariable().getType()));
		}
		/*
		 * now add a fake local if the callee may throw an exception
		 */
		if (calleeInfo.getExceptionVariable() != null) {
			lefts_clone.add(procInfo.getExceptionVariable());
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

		Statement s = pf.mkCallStatement(attributes, false, lefts_clone
				.toArray(new IdentifierExpression[lefts_clone.size()]),
				calleeInfo.getBoogieName(), args);

		return s;
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
	 * @param constructorInstance
	 *            non-Null if the callee is a constructor. In that case, we have
	 *            to set the instance to Null if the constructor returned an
	 *            exception.
	 */
	// static private void translateCalleeExceptions_old(SootStmtSwitch ss,
	// Unit statement,
	// Expression constructorInstance) {
	// SootValueSwitch valueswitch = ss.getValueSwitch();
	// ProgramFactory pf = GlobalsCache.v().getPf();
	// SootProcedureInfo procInfo = ss.getProcInfo();
	//
	// List<SootClass> maxThrowSet = procInfo.getThrowsClasses();
	//
	// // first we collect all possible exceptions.
	// LinkedList<Statement> statements = new LinkedList<Statement>();
	//
	// // keep track of the caught exceptions so that we can add the uncaught
	// // ones later
	// HashSet<SootClass> caughtExceptions = new HashSet<SootClass>();
	// // now collect all exceptions that are in the exceptional unit graph
	// // and their associated traps
	// if (procInfo.getExceptionalUnitGraph().getExceptionalSuccsOf(statement)
	// .size() != 0) {
	// for (ExceptionDest dest : procInfo.getExceptionalUnitGraph()
	// .getExceptionDests(statement)) {
	// if (dest.getTrap() != null
	// && dest.getTrap().getException() != null) {
	// caughtExceptions.add(dest.getTrap().getException());
	// Statement transitionStmt;
	// // the exception is caught somewhere in this procedure
	// transitionStmt = pf.mkGotoStatement(GlobalsCache.v()
	// .getUnitLabel(
	// (Stmt) dest.getTrap().getHandlerUnit()));
	//
	// // add a conjunct to check if that the type of the exception
	// // is <: than the one caught
	// // by the catch block
	// Expression condition = pf.mkBinaryExpression(
	// pf.getBoolType(),
	// BinaryOperator.COMPPO,
	// valueswitch.getClassTypeFromExpression(
	// procInfo.getExceptionVariable(), false),
	// GlobalsCache.v().lookupClassVariable(
	// dest.getTrap().getException()));
	// Statement[] thenPart = { transitionStmt };
	// Statement[] elsePart = {};
	// statements.add(pf.mkIfStatement(condition, thenPart,
	// elsePart));
	// } else {
	// // Log.error("NO CATCH FOR "+ dest);
	// }
	// }
	// }
	// // now create a list of all exceptions that are thrown by the callee
	// // but not caught by the procedure
	// HashSet<SootClass> uncaughtException = new HashSet<SootClass>();
	// for (SootClass sc : maxThrowSet) {
	// boolean caught = false;
	// for (SootClass other : caughtExceptions) {
	// if (GlobalsCache.v().isSubTypeOrEqual(sc, other)) {
	// caught = true;
	// break;
	// }
	// }
	// if (!caught && !uncaughtException.contains(sc)) {
	// uncaughtException.add(sc);
	// }
	// }
	// // now always pick the uncaught exception which has
	// // no subclass in the hashset, create a conditional choice,
	// // and remove it. This ordering is necessary, otherwise,
	// // we might create dead code.
	// LinkedList<SootClass> todo = new LinkedList<SootClass>(
	// uncaughtException);
	// while (!todo.isEmpty()) {
	// SootClass current = todo.removeFirst();
	// boolean good = true;
	// for (SootClass other : todo) {
	// if (current == other) {
	// throw new RuntimeException("can't be");
	// }
	// if (GlobalsCache.v().isSubTypeOrEqual(other, current)) {
	// good = false;
	// break;
	// }
	// }
	// if (!good) {
	// todo.addLast(current);
	// continue;
	// }
	//
	// if (GlobalsCache.v().inThrowsClause(current, procInfo)
	// || org.joogie.Options.v().isRuntimeExceptionReturns()) {
	//
	// Statement transitionStmt;
	// // add a conjunct to check if that the type of the exception
	// // is <: than the one caught
	// // by the catch block
	// Expression condition = pf.mkBinaryExpression(
	// pf.getBoolType(),
	// BinaryOperator.COMPPO,
	// valueswitch.getClassTypeFromExpression(
	// procInfo.getExceptionVariable(), false),
	// GlobalsCache.v().lookupClassVariable(current));
	//
	// transitionStmt = pf.mkReturnStatement();
	// Statement[] thenPart = { transitionStmt };
	// Statement[] elsePart = {};
	// statements.add(pf.mkIfStatement(condition, thenPart, elsePart));
	//
	// } else {
	// Log.debug("The exception: " + current
	// + " is not handeled and treated as RuntimeException.");
	// }
	// }
	// // TODO: the part above is pretty hacky and can be improved using
	// // TrapManager.
	// if (statements.size()==0) return;
	// // if the call was a constructor, add an assignment that sets
	// // the created variable back to null
	// if (constructorInstance != null
	// && constructorInstance != ss.getProcInfo().getThisReference()) {
	// statements.addFirst(pf.mkAssignmentStatement(constructorInstance,
	// SootPrelude.v().getNullConstant()));
	// }
	//
	// // now add all the exceptional checks in a block where
	// // we ensure that $excpeiton was not null
	// Expression condition = pf.mkBinaryExpression(pf.getBoolType(),
	// BinaryOperator.COMPNEQ, procInfo.getExceptionVariable(),
	// SootPrelude.v().getNullConstant());
	//
	// ss.addStatement(pf.mkIfStatement(condition,
	// statements.toArray(new Statement[statements.size()]),
	// new Statement[] {}));
	//
	// }
	//

	static private void translateCalleeExceptions(SootStmtSwitch ss,
			Unit statement, Expression constructorInstance) {
		SootValueSwitch valueswitch = ss.getValueSwitch();
		ProgramFactory pf = GlobalsCache.v().getPf();
		SootProcedureInfo procInfo = ss.getProcInfo();

		// first we collect all possible exceptions.
		// then we create one statement if($exception!=null) statements
		LinkedList<Statement> statements = new LinkedList<Statement>();
		List<Trap> traps = new LinkedList<Trap>();
		List<Trap> finally_traps = new LinkedList<Trap>(); // TODO: do we have
															// to use them here?
		TranslationHelpers.getReachableTraps(statement,
				procInfo.getSootMethod(), traps, finally_traps);

		List<SootClass> possibleExceptions = procInfo.getThrowsClasses();

		// in case the method throws something unexpected, we
		// add Throwable to the list of possible exceptions.
		// NOTE: this causes a ridiculous blow-up of the boogie program
		// and we don't gain anything for the infeasible code detection,
		// so we're not doing it for now.
		// SootClass throwableException =
		// Scene.v().loadClass("java.lang.Throwable",
		// SootClass.SIGNATURES);
		// if (!possibleExceptions.contains(throwableException)) {
		// possibleExceptions.add(throwableException);
		// }

		for (SootClass c : possibleExceptions) {
			String transferlabel = null;
			// for each possible exception, check if there is a catch block.
			for (Trap trap : new LinkedList<Trap>(traps)) {
				if (GlobalsCache.v().isSubTypeOrEqual(c, trap.getException())) {
					transferlabel = GlobalsCache.v().getUnitLabel(
							(Stmt) trap.getHandlerUnit());
					traps.remove(trap);
					break;
				}
			}
			Statement transferStatement;
			if (transferlabel == null) {
				// if the exception is not caught, leave the procedure
				// that is, re-throw.
				transferStatement = pf.mkReturnStatement();
			} else {
				// if the exception is caught, create a goto
				transferStatement = pf.mkGotoStatement(transferlabel);
			}

			// now make a statement of the form
			// if($exception<:c) transferStatement
			// and add it to the list statements
			Expression condition = pf.mkBinaryExpression(
					pf.getBoolType(),
					BinaryOperator.COMPPO,
					valueswitch.getClassTypeFromExpression(
							procInfo.getExceptionVariable(), false),
					GlobalsCache.v().lookupClassVariable(c));
			Statement[] thenPart = { transferStatement };
			Statement[] elsePart = {};
			statements.add(pf.mkIfStatement(condition, thenPart, elsePart));
		}

		// now check if there are traps of type Exception or Throwable left, or
		// if we might not know the throws clause
		// then we have to create an edge to them as well. Otherwise
		// we might create unreachable catch blocks
		SootClass exception = Scene.v().loadClass("java.lang.Exception",
				SootClass.SIGNATURES);
		SootClass throwable = Scene.v().loadClass("java.lang.Throwable",
				SootClass.SIGNATURES);
		for (Trap trap : traps) {
			if (trap.getException() == exception
					|| trap.getException() == throwable
					|| !procInfo.getSootMethod().hasActiveBody()) {
				Expression condition = pf.mkBinaryExpression(
						pf.getBoolType(),
						BinaryOperator.COMPPO,
						valueswitch.getClassTypeFromExpression(
								procInfo.getExceptionVariable(), false),
						GlobalsCache.v().lookupClassVariable(
								trap.getException()));
				Statement[] thenPart = {
						TranslationHelpers.createClonedAttribAssert(),
						pf.mkGotoStatement(GlobalsCache.v().getUnitLabel(
								(Stmt) trap.getHandlerUnit())) };
				Statement[] elsePart = { TranslationHelpers
						.createClonedAttribAssert() };
				statements.add(pf.mkIfStatement(condition, thenPart, elsePart));
			}
		}

		// finally check if there is a finally_trap that we didn't account for
		// and create an unconditional goto.
		if (finally_traps.size() > 0) {
			if (finally_traps.size() > 1) {
				Log.error("more than one finally trap for "
						+ procInfo.getBoogieName());
			}
			Trap trap = finally_traps.get(0);
			statements.add(pf.mkGotoStatement(GlobalsCache.v().getUnitLabel(
					(Stmt) trap.getHandlerUnit())));
		}

		if (statements.size() == 0)
			return;
		// if the call was a constructor, add an assignment that sets
		// the created variable back to null
		if (constructorInstance != null
				&& constructorInstance != procInfo.getThisReference()) {
			statements.addFirst(pf.mkAssignmentStatement(constructorInstance,
					SootPrelude.v().getNullConstant()));
		}

		// now add all the exceptional checks in a block where
		// we ensure that $excpeiton was not null
		Expression condition = pf.mkBinaryExpression(pf.getBoolType(),
				BinaryOperator.COMPNEQ, procInfo.getExceptionVariable(),
				SootPrelude.v().getNullConstant());

		ss.addStatement(pf.mkIfStatement(condition,
				statements.toArray(new Statement[statements.size()]),
				new Statement[] {}));

	}

}
