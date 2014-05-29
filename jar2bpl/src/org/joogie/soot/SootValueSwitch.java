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

import org.joogie.GlobalsCache;
import org.joogie.util.Log;
import org.joogie.util.TranslationHelpers;

import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.Local;
import soot.NullType;
import soot.RefType;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.JimpleValueSwitch;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import boogie.ProgramFactory;
import boogie.declaration.FunctionDeclaration;
import boogie.enums.BinaryOperator;
import boogie.enums.UnaryOperator;
import boogie.expression.Expression;
import boogie.location.ILocation;
import boogie.type.BoogieType;

/**
 * @author schaef
 */
public class SootValueSwitch implements JimpleValueSwitch {

	private SootProcedureInfo procInfo;
	private SootStmtSwitch stmtSwitch;
	private ProgramFactory pf;

	public SootValueSwitch(SootProcedureInfo pinfo, SootStmtSwitch stmtswitch) {
		this.procInfo = pinfo;
		this.pf = GlobalsCache.v().getPf();
		this.stmtSwitch = stmtswitch;
	}

	private LinkedList<Expression> expressionStack = new LinkedList<Expression>();

	public Expression getExpression() {
		return this.expressionStack.pop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant arg0) {		
		this.expressionStack.push(GlobalsCache.v().lookupClassConstant(arg0));	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant arg0) {
		// ILocation loc = TranslationHelpers.createDummyLocation();
		// expressionStack.push(this.pf.mkRealLiteral(loc,
		// String.valueOf(arg0.value)));
		this.expressionStack.push(GlobalsCache
				.createDummyExpression(GlobalsCache.v().getPf().getIntType()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant arg0) {
		// ILocation loc = TranslationHelpers.createDummyLocation();
		// expressionStack.push(this.pf.mkRealLiteral(loc, String.valueOf(
		// (double)(arg0.value))));
		this.expressionStack.push(GlobalsCache
				.createDummyExpression(GlobalsCache.v().getPf().getIntType()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant arg0) {
		ILocation loc = TranslationHelpers.createDummyLocation();
		expressionStack.push(this.pf.mkIntLiteral(loc,
				String.valueOf(arg0.value)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant arg0) {
		ILocation loc = TranslationHelpers.createDummyLocation();
		expressionStack.push(this.pf.mkIntLiteral(loc,
				String.valueOf(arg0.value)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant arg0) {
		expressionStack.push(SootPrelude.v().getNullConstant());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant arg0) {
		// This is only called when string constants are used in method
		// calls
		// in any other case this is handled in the SootStmtSwitch
		this.expressionStack.push(GlobalsCache.createDummyExpression(arg0
				.getType()));
	}

	private void translateBinOp(BinopExpr arg0) {
		arg0.getOp1().apply(this);
		Expression lhs = this.expressionStack.pop();
		arg0.getOp2().apply(this);
		Expression rhs = this.expressionStack.pop();

		// cast if necessary. This has to be done because soot treats boolean as
		// integers
		if (lhs.getType() != rhs.getType()) {
			rhs = TranslationHelpers.castBoogieTypes(rhs, lhs.getType());
		}

		if (arg0.getType() instanceof FloatType
				|| arg0.getType() instanceof DoubleType) {
			FunctionDeclaration fun = SootPrelude.v().lookupRealOperator(
					arg0.getSymbol());
			Expression[] arguments = { lhs, rhs };
			this.expressionStack.push(this.pf.mkFunctionApplication(
					lhs.getLocation(), fun, arguments));
			return;
		}
		// if it is not Float or Double, proceed normally.
		createBinOp(arg0.getSymbol(), lhs, rhs);
	}

	public void createBinOp(String op, Expression left, Expression right) {
		BinaryOperator operator;
		BoogieType rettype;
		op = op.trim();
		if (op.compareTo("+") == 0) {
			rettype = left.getType();
			operator = BinaryOperator.ARITHPLUS;
		} else if (op.compareTo("-") == 0) {
			rettype = left.getType();
			operator = BinaryOperator.ARITHMINUS;
		} else if (op.compareTo("*") == 0) {
			rettype = left.getType();
			operator = BinaryOperator.ARITHMUL;
		} else if (op.compareTo("/") == 0) {	
			//make sure that "right" is an Integer
			//then assert that it is different from 0
			this.stmtSwitch.getErrorModel().createDivByZeroGuard(right);
			rettype = left.getType();
			operator = BinaryOperator.ARITHDIV;						
		} else if (op.compareTo("%") == 0) {
			this.stmtSwitch.getErrorModel().createDivByZeroGuard(right);
			rettype = left.getType();
			operator = BinaryOperator.ARITHMOD;
		} else if (op.compareTo("cmp") == 0 || op.compareTo("cmpl") == 0
				|| op.compareTo("cmpg") == 0) {
			this.expressionStack.push(SootPrelude.v().compareExpr(left, right));
			return;
		} else if (op.compareTo("==") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPEQ;
		} else if (op.compareTo("<") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPLT;
		} else if (op.compareTo(">") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPGT;
		} else if (op.compareTo("<=") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPLEQ;
		} else if (op.compareTo(">=") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPGEQ;
		} else if (op.compareTo("!=") == 0) {
			rettype = this.pf.getBoolType();
			operator = BinaryOperator.COMPNEQ;
		} else if (op.compareTo("&") == 0) {
			this.expressionStack.push(SootPrelude.v().bitAndExpr(left, right));
			return;
		} else if (op.compareTo("|") == 0) {
			this.expressionStack.push(SootPrelude.v().bitOrExpr(left, right));
			return;
		} else if (op.compareTo("<<") == 0) { // Shiftl
			this.expressionStack.push(SootPrelude.v().shiftLeft(left, right));
			return;
		} else if (op.compareTo(">>") == 0) { // Shiftr
			this.expressionStack.push(SootPrelude.v().shiftRight(left, right));
			return;
		} else if (op.compareTo(">>>") == 0) { // UShiftr
			this.expressionStack.push(SootPrelude.v().uShiftRight(left, right));
			return;
		} else if (op.compareTo("^") == 0) { // XOR
			this.expressionStack.push(SootPrelude.v().xorExpr(left, right));
			return;
		} else {
			throw new RuntimeException("UNKNOWN Jimple operator " + op);
		}
		this.expressionStack.push(this.pf.mkBinaryExpression(
				left.getLocation(), rettype, operator, left, right));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 */
	@Override
	public void caseAddExpr(AddExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 */
	@Override
	public void caseAndExpr(AndExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 */
	@Override
	public void caseCastExpr(CastExpr arg0) {
		arg0.getOp().apply(this);
		Expression exp = this.getExpression();

//        System.out.println("Cast " + arg0.getOp().getType() + " to " + arg0.getCastType());

		/*
		if (exp == SootPrelude.v().getNullConstant()) {
			// TODO: what shall we do if someone tries to cast Null?
			if (arg0.getOp() instanceof NullConstant) {
				Log.error("No idea how to cast Null");
			} else {
				Log.error("WARNING: this should not be null!");

			}
			this.expressionStack.push(exp);
			return;
		}*/

		// if the jimple types are the same, just return the expression
		if (arg0.getOp().getType() == arg0.getCastType()) {
			this.expressionStack.push(exp);
			return;
		}
		
		BoogieType boogieTargetType = GlobalsCache.v().getBoogieType(arg0.getCastType());
		
		////////////////////////////////////////////////////////////////////////
		if (boogieTargetType == this.pf.getIntType() ||
		    boogieTargetType == this.pf.getRealType() ||
		    boogieTargetType == this.pf.getBoolType()) {
			// in that case, exp is also of primitive type
			// otherwise, java or soot would translate it
			// into a more complex expression that uses the
			// appropriate java methods.
			this.expressionStack.push(TranslationHelpers.castBoogieTypes(exp,
					boogieTargetType));
			return;
			
        ////////////////////////////////////////////////////////////////////////
		} else if (arg0.getCastType() instanceof RefType) {
		  
		  final RefType targetType = (RefType)arg0.getCastType();
		  
		  if (targetType.getClassName() == "java.lang.Object") {
		    // should always be ok, nothing to assert
            this.expressionStack.push(exp);
		  } else if (arg0.getOp().getType() instanceof RefType) {
            // Guard that typeof(exp) <: targetType             
            this.stmtSwitch.getErrorModel().createClassCastGuard(
                  this.getClassTypeFromExpression(exp, false),
                  GlobalsCache.v().lookupClassVariable(targetType.getSootClass()));
            this.expressionStack.push(exp);
		  } else if (arg0.getOp().getType() instanceof NullType) {
            // should always be ok, nothing to assert
            this.expressionStack.push(exp);
		  } else {
            /*Log.error*/
            System.out.println("Don't know how to cast from " +
                               arg0.getOp().getType() + " to " +
                               arg0.getCastType());
            this.expressionStack.push(GlobalsCache.createDummyExpression(boogieTargetType));
		  }
		  
		  return;
		  
		////////////////////////////////////////////////////////////////////////
		} else if (arg0.getCastType() instanceof ArrayType) {

          final ArrayType targetType = (ArrayType)arg0.getCastType();
          this.stmtSwitch.getErrorModel().createClassCastGuard(
              this.getClassTypeFromExpression(exp, false),
              GlobalsCache.v().lookupArrayType(targetType));
          
          this.expressionStack.push(exp);

          return;
          
        ////////////////////////////////////////////////////////////////////////
        // old, should be removed at some point
		} else if (boogieTargetType == SootPrelude.v().getReferenceType()) {
			// ILocation loc = exp.getLocation();
			if (arg0.getCastType() instanceof RefType) {
				RefType rtype = (RefType) arg0.getCastType();
				// Guard that typeof(exp) <: targetType				
				this.stmtSwitch.getErrorModel().createClassCastGuard(
								this.getClassTypeFromExpression(exp, false),
								GlobalsCache.v().lookupClassVariable(
										rtype.getSootClass()));
				this.expressionStack.push(exp);
				return;
			} else {
			  throw new RuntimeException("Cast from " + arg0.getOp().getType() + " to "
						+ arg0.getCastType() + " not implemented");
			}
		}
		
		throw new RuntimeException("Cast from " + arg0.getOp().getType() + " to "
				+ arg0.getCastType() + " not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 */
	@Override
	public void caseCmpExpr(CmpExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 */
	@Override
	public void caseCmplExpr(CmplExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 */
	@Override
	public void caseDivExpr(DivExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 */
	@Override
	public void caseEqExpr(EqExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 */
	@Override
	public void caseGeExpr(GeExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 */
	@Override
	public void caseGtExpr(GtExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr arg0) {
		arg0.getOp().apply(this);
		Expression lhs = this.getExpression();
		if (arg0.getCheckType() instanceof RefType) {
			RefType rtype = (RefType) arg0.getCheckType();
			Expression rhs = GlobalsCache.v().lookupClassVariable(
					rtype.getSootClass());
			this.expressionStack
					.push(this.pf.mkBinaryExpression(rhs.getLocation(),
							this.pf.getBoolType(), BinaryOperator.COMPPO,
							this.getClassTypeFromExpression(lhs, false), rhs));
		} else {
			Log.error("instanceof for arrays not implemented");
			this.expressionStack.push(GlobalsCache.createDummyExpression(arg0
					.getType()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.
	 * InterfaceInvokeExpr)
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr arg0) {
		throw new RuntimeException("This must be handeled by SootStmtSwitch!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 */
	@Override
	public void caseLeExpr(LeExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 */
	@Override
	public void caseLengthExpr(LengthExpr arg0) {
		arg0.getOp().apply(this);
		Expression base = this.getExpression();
		this.stmtSwitch.getErrorModel().createNonNullGuard(base);
		this.expressionStack.push(GlobalsCache.v().getArraySizeExpression(
				base));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 */
	@Override
	public void caseLtExpr(LtExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 */
	@Override
	public void caseMulExpr(MulExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 */
	@Override
	public void caseNeExpr(NeExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 */
	@Override
	public void caseNegExpr(NegExpr arg0) {
		arg0.getOp().apply(this);
		Expression e = this.expressionStack.pop();
		ILocation loc = TranslationHelpers.createDummyLocation();
		if (e.getType() == this.pf.getIntType()) {
			e = SootPrelude.v().intToBool(e);
		}
		this.expressionStack.push(this.pf.mkUnaryExpression(loc,
				this.pf.getBoolType(), UnaryOperator.LOGICNEG, e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr arg0) {
		throw new RuntimeException("Must be handeled in SootStmtSwitch");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr arg0) {
		throw new RuntimeException("Must be handeled in SootStmtSwitch");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr
	 * )
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr arg0) {
		throw new RuntimeException("Must be handeled in SootStmtSwitch");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 */
	@Override
	public void caseOrExpr(OrExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 */
	@Override
	public void caseRemExpr(RemExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 */
	@Override
	public void caseShlExpr(ShlExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 */
	@Override
	public void caseShrExpr(ShrExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr
	 * )
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr arg0) {
		throw new RuntimeException("This must be handeled by SootStmtSwitch!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr arg0) {
		throw new RuntimeException("This must be handeled by SootStmtSwitch!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 */
	@Override
	public void caseSubExpr(SubExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 */
	@Override
	public void caseUshrExpr(UshrExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr
	 * )
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr arg0) {
		throw new RuntimeException("This must be handeled by SootStmtSwitch!");
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr arg0) {
		throw new RuntimeException("This must be handeled by SootStmtSwitch!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 */
	@Override
	public void caseXorExpr(XorExpr arg0) {
		translateBinOp(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@Override
	public void caseArrayRef(ArrayRef arg0) {
		ILocation loc = TranslationHelpers.createDummyLocation();
		BoogieType btype = GlobalsCache.v().getBoogieType(arg0.getType());
		Expression arrayvar;
		BoogieType arrtype;
		if (btype == this.pf.getIntType()) {
			arrayvar = SootPrelude.v().getIntArrHeapVariable();
			arrtype = SootPrelude.v().getIntArrType();
		} else if (btype == this.pf.getBoolType()) {
			arrayvar = SootPrelude.v().getBoolArrHeapVariable();
			arrtype = SootPrelude.v().getBoolArrType();
			// } else if (btype == this.pf.getRealType() ) {
			// arrayvar = SootPrelude.v().getRealArrHeapVariable();
			// arrtype= SootPrelude.v().getRealArrType();
		} else if (btype == SootPrelude.v().getReferenceType()) {
			arrayvar = SootPrelude.v().getRefArrHeapVariable();
			arrtype = SootPrelude.v().getRefArrType();
		} else {
			throw new RuntimeException("do not understand array of type: "
					+ btype.toString());
		}
		// tranlate base and index
		arg0.getBase().apply(this);
		Expression baseExpression = this.expressionStack.pop();
		arg0.getIndex().apply(this);
		Expression indexExpression = this.expressionStack.pop();
		// guard out-of-bounds exceptions
		this.stmtSwitch.getErrorModel().createArrayBoundGuard(baseExpression, indexExpression);

		// construct the expression
		Expression[] base = { baseExpression };
		Expression array = this.pf.mkArrayAccessExpression(loc, arrtype,
				arrayvar, base);
		Expression[] indices = { indexExpression };
		this.expressionStack.push(this.pf.mkArrayAccessExpression(loc, btype,
				array, indices));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef
	 * )
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef arg0) {
		if (arg0.getType() instanceof RefType) {
			RefType rtype = (RefType) arg0.getType();
			// assume that the exception variable now has the type of the caught
			// exception
			// assume $heap[$exception,$type] <: arg0.getType()
			
			//TODO: do we have to check if this.procInfo.getExceptionVariable() is null?
			Expression typefield = this.getClassTypeFromExpression(this.procInfo.getExceptionVariable(), false);
			this.stmtSwitch
					.addGuardStatement(GlobalsCache.v().assumeSubType(
							typefield,
							GlobalsCache.v().lookupClassVariable(
									rtype.getSootClass())));
			this.expressionStack.push(this.procInfo.getExceptionVariable());
			return;
		}
		throw new RuntimeException(
				"this case of exception handling has not beend implemented");
	}

	private void checkFieldAnnotations(Expression expr, FieldRef fr) {		
		LinkedList<SootAnnotations.Annotation> annot = SootAnnotations.parseFieldTags(fr.getField());
		if (annot.contains(SootAnnotations.Annotation.NonNull)) {
			this.stmtSwitch.addGuardStatement(
					this.stmtSwitch.getErrorModel().createAssumeNonNull(expr) );
		}			
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef arg0) {	
		ILocation loc = TranslationHelpers.createDummyLocation();
		arg0.getBase().apply(this);
		Expression base = this.getExpression();
		Expression field = GlobalsCache.v().lookupSootField(arg0.getField());
		
		//TODO: we are checking if this is a @NonNull field
		//if so, we add an assume to ensure that it actually is
		//not null here. May be better ways to do this.		
		checkFieldAnnotations(this.makeHeapAccessExpression(loc, base,
				field, false), arg0);
		
		this.expressionStack.push(this.makeHeapAccessExpression(loc, base,
				field, true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 */
	@Override
	public void caseParameterRef(ParameterRef arg0) {
		this.expressionStack.push(this.procInfo.lookupParameterVariable(arg0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef arg0) {
		//TODO: we are checking if this is a @NonNull field
		//if so, we add an assume to ensure that it actually is
		//not null here. May be better ways to do this.
		checkFieldAnnotations(GlobalsCache.v().lookupSootField(
				arg0.getField()), arg0);

		this.expressionStack.push(GlobalsCache.v().lookupSootField(
				arg0.getField()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 */
	@Override
	public void caseThisRef(ThisRef arg0) {
		this.expressionStack.push(this.procInfo.getThisReference());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 */
	@Override
	public void caseLocal(Local arg0) {		
		this.expressionStack.push(this.procInfo.lookupLocalVariable(arg0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object arg0) {
		Log.error("BoogieValueSwitch: case not covered");
		assert (false);
	}


	/**
	 * Make a heap access and the necessary assertion
	 * 
	 * @param field
	 * @return
	 */
	public Expression makeHeapAccessExpression(ILocation loc, Expression base,
			Expression field, boolean guarded) {
		if (guarded) {
			this.stmtSwitch.getErrorModel().createNonNullGuard(base);			
		}
		// Assemble the $heap[base, field] expression
		Expression[] indices = { base, field };
		return pf.mkArrayAccessExpression(loc,
				SootPrelude.v().getFieldType(field.getType()), SootPrelude.v()
						.getHeapVariable(), indices);
	}

	/**
	 * gets the field of expr that denotes its Java type
	 * E.g., let's say we have a variable c of type C. The call 
	 * getExprssionJavaClass(c) 
	 * returns
	 * $heap[c, $type] which, in this case, would be C.
	 * @param expr
	 * @param guarded
	 * @return
	 */	
	public Expression getExprssionJavaClass(Expression expr) {
		return getClassTypeFromExpression(expr, true);
	}

	/**
	 * gets the field of expr that denotes its Java type
	 * E.g., let's say we have a variable c of type C. The call 
	 * getExprssionJavaClass(c,false) 
	 * returns
	 * $heap[c, $type] which, in this case, would be C.
	 * @param expr
	 * @param guarded
	 * @return
	 */
	public Expression getClassTypeFromExpression(Expression expr, boolean guarded) {
		return makeHeapAccessExpression(
				TranslationHelpers.createDummyLocation(), expr, SootPrelude.v()
						.getFieldClassVariable(), guarded);
	}	
	
}
