/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.joogie.GlobalsCache;
import org.joogie.util.TranslationHelpers;

import util.Log;
import boogie.declaration.FunctionDeclaration;
import boogie.enums.BinaryOperator;
import boogie.expression.Expression;
import boogie.expression.IdentifierExpression;
import boogie.location.ILocation;
import boogie.type.BoogieType;
import boogie.type.ConstructedType;

/**
 * @author martin
 * 
 */
public class SootPrelude {

	private static SootPrelude instance = null;

	public static SootPrelude v() {
		if (SootPrelude.instance == null) {
			SootPrelude.instance = new SootPrelude();
		}
		return SootPrelude.instance;
	}

	public static void restInstance() {
		if (instance != null) {
			// TODO
		}
		instance = null;
	}

	private BoogieType referenceType;
	private BoogieType voidType;
	private BoogieType fieldType;
	private BoogieType heapType;
	private BoogieType javaClassType;

	private BoogieType intArrType;
	private BoogieType refArrType;
	private BoogieType realArrType;
	private BoogieType boolArrType;

	private IdentifierExpression nullConstant;
	private IdentifierExpression heapVariable;

	private IdentifierExpression fieldAllocVariable;
	private IdentifierExpression fieldClassVariable;

	private FunctionDeclaration arrayTypeConstructor;
	private IdentifierExpression intArrayConstructor;
	private IdentifierExpression byteArrayConstructor;
	private IdentifierExpression charArrayConstructor;
	private IdentifierExpression longArrayConstructor;
	private IdentifierExpression boolArrayConstructor;

	private IdentifierExpression intArrHeapVariable;
	private IdentifierExpression realArrHeapVariable;
	private IdentifierExpression boolArrHeapVariable;
	private IdentifierExpression refArrHeapVariable;

	private IdentifierExpression stringSizeHeapVariable;

	private IdentifierExpression arrSizeHeapVariable;

	private FunctionDeclaration int2bool, bool2int, ref2bool;

	private FunctionDeclaration int2real, real2int;

	private FunctionDeclaration cmpBool, cmpInt, cmpReal, cmpRef;

	private FunctionDeclaration shlInt, shrInt, ushrInt, xorInt;

	private FunctionDeclaration bitAnd, bitOr;

	private String fieldTypeName = "Field";

	
	
	private void loadPreludeFile() {		
		if (org.joogie.Options.v().getPreludeFileName()!=null) {
			try {
				Log.info("Loading user prelude: "+ org.joogie.Options.v().getPreludeFileName());
				GlobalsCache.v().getPf().importBoogieFile(org.joogie.Options.v().getPreludeFileName());
			} catch (Exception e) {
				throw new RuntimeException("Loading prelude failed: "+e.toString());
			}			
		} else {
			//loadPreludeFromResources("/res/basic_prelude.bpl");
			loadPreludeFromResources("/res/java_lang.bpl");
		}
	}
	
	private void loadPreludeFromResources(String name) {
		try {
			InputStream stream = SootPrelude.class.getResourceAsStream(name);			
			GlobalsCache.v().getPf().importBoogieFile(name, stream);
			stream.close();
			Log.error("---------- PRELUDE FOUND -----------" );
		} catch (Exception e1) {							
			throw new RuntimeException("Prelude file not available. Something failed during the build!");
		}
		
	}

	private SootPrelude() {
		BoogieType bool = GlobalsCache.v().getPf().getBoolType();
		BoogieType integer = GlobalsCache.v().getPf().getIntType();
		BoogieType real = GlobalsCache.v().getPf().getRealType();
		ILocation loc = TranslationHelpers.createDummyLocation();

		this.referenceType = GlobalsCache.v().getPf().getNamedType("ref");
		this.voidType = GlobalsCache.v().getPf().getNamedType("void");

		BoogieType alpha = GlobalsCache.v().getPf().mkPlaceholderType();
		BoogieType[] params = { alpha };
		this.fieldType = GlobalsCache.v().getPf()
				.getNamedType(this.fieldTypeName, params, false);
		BoogieType[] indexTypes = { this.referenceType, fieldType };
		this.heapType = GlobalsCache.v().getPf()
				.getArrayType(indexTypes, alpha);

		this.nullConstant = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, referenceType, "$null", true,
						true, true);
		// this is our heap : <a>[ref, Field alpha]alpha
		this.heapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, heapType, "$heap", false, true,
						false);

		// create helper fields to store the type of an object and a flag if it
		// has been created with "new"
		this.javaClassType = GlobalsCache.v().getPf().getNamedType("javaType");
		BoogieType[] fallocparams = { bool };
		this.fieldAllocVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(
						loc,
						GlobalsCache.v().getPf()
								.mkSubstituteType(this.fieldType, fallocparams),
						"$alloc", true, true, true);
		BoogieType[] fclassparams = { this.javaClassType };
		this.fieldClassVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(
						loc,
						GlobalsCache.v().getPf()
								.mkSubstituteType(this.fieldType, fclassparams),
						"$type", true, true, true);

		// functions to represent Java/Soot array types
		{
			IdentifierExpression[] in = { GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(), "t",
							false, false, false) };
			IdentifierExpression out = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(), "$ret",
							false, false, false);

			this.arrayTypeConstructor = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$arrayType", in, out);

			this.intArrayConstructor = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(),
							"$intArrayType", false, true, false);

			this.byteArrayConstructor = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(),
							"$byteArrayType", false, true, false);

			this.charArrayConstructor = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(),
							"$charArrayType", false, true, false);

			this.longArrayConstructor = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(),
							"$longArrayType", false, true, false);

			this.boolArrayConstructor = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, getJavaClassType(),
							"$boolArrayType", false, true, false);
		}

		// the following creates the heap variables for arrays:
		// each array is represented by one variable of type "ref" on the $heap.
		// this variable refers to the actual array on the array heap of
		// corresponding
		// type. That is:
		// a[x] = 3 translates to a variable a : ref and the read access whould
		// be
		// $intArrHeap[a][x] := 3
		BoogieType[] intarridx = { integer };
		BoogieType[] refarridx = { this.referenceType };
		// int array
		this.intArrType = GlobalsCache.v().getPf()
				.getArrayType(intarridx, GlobalsCache.v().getPf().getIntType());
		BoogieType intarrheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, this.intArrType);
		this.intArrHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, intarrheapidx, "$intArrHeap",
						false, true, false);
		// real array
		// TODO: note that we use Int to abstract Java Float and Double
		// variables
		// This is only sound because we use uninterpreted functions to model
		// operations
		// on float!
		this.realArrType = GlobalsCache.v().getPf()
				.getArrayType(intarridx, GlobalsCache.v().getPf().getIntType());
		BoogieType realarrheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, this.realArrType);
		this.realArrHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, realarrheapidx, "$realArrHeap",
						false, true, false);
		// bool array
		this.boolArrType = GlobalsCache
				.v()
				.getPf()
				.getArrayType(intarridx, GlobalsCache.v().getPf().getBoolType());
		BoogieType boolarrheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, this.boolArrType);
		this.boolArrHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, boolarrheapidx, "$boolArrHeap",
						false, true, false);
		// ref array
		this.refArrType = GlobalsCache.v().getPf()
				.getArrayType(intarridx, this.referenceType);
		BoogieType refarrheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, this.refArrType);
		this.refArrHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, refarrheapidx, "$refArrHeap",
						false, true, false);
		// array size heap
		BoogieType arrsizeheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, integer);
		this.arrSizeHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, arrsizeheapidx, "$arrSizeHeap",
						false, true, false);

		// private IdentifierExpression stringSizeHeapVariable;
		BoogieType stringsizeheapidx = GlobalsCache.v().getPf()
				.getArrayType(refarridx, integer);
		this.stringSizeHeapVariable = GlobalsCache
				.v()
				.getPf()
				.mkIdentifierExpression(loc, stringsizeheapidx,
						"$stringSizeHeap", false, true, false);

		// make int2bool
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			Expression xIs0 = GlobalsCache
					.v()
					.getPf()
					.mkBinaryExpression(loc, bool, BinaryOperator.COMPEQ, x,
							GlobalsCache.v().getPf().mkIntLiteral(loc, "0"));
			Expression ite = GlobalsCache
					.v()
					.getPf()
					.mkIfThenElseExpression(
							loc,
							bool,
							xIs0,
							GlobalsCache.v().getPf()
									.mkBooleanLiteral(loc, false),
							GlobalsCache.v().getPf()
									.mkBooleanLiteral(loc, true));
			IdentifierExpression[] in = { x };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, bool, "$ret", false, false,
							false);
			this.int2bool = GlobalsCache
					.v()
					.getPf()
					.mkFunctionDeclaration(loc, "$intToBool", in, outParam, ite);
		}
		// make bool2int
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, bool, "x", false, false, false);
			Expression xIsTrue = GlobalsCache
					.v()
					.getPf()
					.mkBinaryExpression(
							loc,
							bool,
							BinaryOperator.COMPEQ,
							x,
							GlobalsCache.v().getPf()
									.mkBooleanLiteral(loc, true));
			Expression ite = GlobalsCache
					.v()
					.getPf()
					.mkIfThenElseExpression(loc, integer, xIsTrue,
							GlobalsCache.v().getPf().mkIntLiteral(loc, "1"),
							GlobalsCache.v().getPf().mkIntLiteral(loc, "0"));
			IdentifierExpression[] in = { x };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.bool2int = GlobalsCache
					.v()
					.getPf()
					.mkFunctionDeclaration(loc, "$boolToInt", in, outParam, ite);
		}
		// make ref2bool
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, this.referenceType, "x",
							false, false, false);
			Expression xIsNull = GlobalsCache
					.v()
					.getPf()
					.mkBinaryExpression(loc, bool, BinaryOperator.COMPEQ, x,
							this.nullConstant);
			Expression ite = GlobalsCache
					.v()
					.getPf()
					.mkIfThenElseExpression(
							loc,
							bool,
							xIsNull,
							GlobalsCache.v().getPf()
									.mkBooleanLiteral(loc, false),
							GlobalsCache.v().getPf()
									.mkBooleanLiteral(loc, true));
			IdentifierExpression[] in = { x };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, bool, "$ret", false, false,
							false);
			this.ref2bool = GlobalsCache
					.v()
					.getPf()
					.mkFunctionDeclaration(loc, "$refToBool", in, outParam, ite);
		}
		// make int2real
		{
			Log.debug("int2real is still uninterpreted");
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression[] in = { x };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, real, "$ret", false, false,
							false);
			this.int2real = GlobalsCache
					.v()
					.getPf()
					.mkFunctionDeclaration(loc, "$intToReal", in, outParam,
							null);
		}
		// make real2int
		{
			Log.debug("real2int is still uninterpreted");
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, real, "x", false, false, false);
			IdentifierExpression[] in = { x };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.real2int = GlobalsCache
					.v()
					.getPf()
					.mkFunctionDeclaration(loc, "$realToInt", in, outParam,
							null);
		}
		// make cmpInt
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			// if (x>y) return 1, if (x<y) return -1, otherwise return 0
			Expression ite = GlobalsCache
					.v()
					.getPf()
					.mkIfThenElseExpression(
							loc,
							bool,
							GlobalsCache
									.v()
									.getPf()
									.mkBinaryExpression(loc, bool,
											BinaryOperator.COMPGT, x, y),
							GlobalsCache.v().getPf().mkIntLiteral(loc, "1"),
							GlobalsCache
									.v()
									.getPf()
									.mkIfThenElseExpression(
											loc,
											bool,
											GlobalsCache
													.v()
													.getPf()
													.mkBinaryExpression(
															loc,
															bool,
															BinaryOperator.COMPLT,
															x, y),
											GlobalsCache.v().getPf()
													.mkIntLiteral(loc, "-1"),
											GlobalsCache.v().getPf()
													.mkIntLiteral(loc, "0")));

			this.cmpInt = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$cmpInt", in, outParam, ite);
		}
		// make cmpReal
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, real, "x", false, false, false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, real, "y", false, false, false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			// if (x>y) return 1, if (x<y) return -1, otherwise return 0
			Expression ite = GlobalsCache
					.v()
					.getPf()
					.mkIfThenElseExpression(
							loc,
							bool,
							GlobalsCache
									.v()
									.getPf()
									.mkBinaryExpression(loc, bool,
											BinaryOperator.COMPGT, x, y),
							GlobalsCache.v().getPf().mkIntLiteral(loc, "1"),
							GlobalsCache
									.v()
									.getPf()
									.mkIfThenElseExpression(
											loc,
											bool,
											GlobalsCache
													.v()
													.getPf()
													.mkBinaryExpression(
															loc,
															bool,
															BinaryOperator.COMPLT,
															x, y),
											GlobalsCache.v().getPf()
													.mkIntLiteral(loc, "-1"),
											GlobalsCache.v().getPf()
													.mkIntLiteral(loc, "0")));

			this.cmpReal = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$cmpReal", in, outParam, ite);
		}
		// make cmpRef
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, this.referenceType, "x",
							false, false, false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, this.referenceType, "y",
							false, false, false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.cmpRef = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$cmpRef", in, outParam, null);
		}

		// make cmpBool
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, bool, "x", false, false, false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, bool, "y", false, false, false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.cmpBool = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$cmpBool", in, outParam, null);
		}
		// make shlInt
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.shlInt = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$shlInt", in, outParam, null);
		}
		// make shrInt
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.shrInt = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$shrInt", in, outParam, null);
		}
		// make ushrInt
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.ushrInt = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$ushrInt", in, outParam, null);
		}

		// make xorInt
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.xorInt = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$xorInt", in, outParam, null);
		}

		// make bitAnd
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.bitAnd = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$bitAnd", in, outParam, null);
		}

		// make bitAnd
		{
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.bitOr = GlobalsCache.v().getPf()
					.mkFunctionDeclaration(loc, "$bitOr", in, outParam, null);
		}

		//now load the prelude file.
		loadPreludeFile();
		
	}

	private HashMap<String, FunctionDeclaration> realOperators = new HashMap<String, FunctionDeclaration>();

	/**
	 * TODO: Maybe these guys should be removed later once we have a proper way
	 * of dealing with floats and doubles.
	 * 
	 * @param op
	 * @param left
	 * @param right
	 * @return
	 */
	public FunctionDeclaration lookupRealOperator(String op) {
		if (!this.realOperators.containsKey(op)) {
			ILocation loc = TranslationHelpers.createDummyLocation();
			BoogieType integer = GlobalsCache.v().getPf().getIntType();
			IdentifierExpression x = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "x", false, false,
							false);
			IdentifierExpression y = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "y", false, false,
							false);
			IdentifierExpression[] in = { x, y };
			IdentifierExpression outParam = GlobalsCache
					.v()
					.getPf()
					.mkIdentifierExpression(loc, integer, "$ret", false, false,
							false);
			this.realOperators.put(
					op,
					GlobalsCache
							.v()
							.getPf()
							.mkFunctionDeclaration(loc,
									"$realOp" + op.hashCode(), in, outParam,
									null));
		}
		return this.realOperators.get(op);
	}

	public Expression getNullConstant() {
		return this.nullConstant;
	}

	public Expression getHeapVariable() {
		return this.heapVariable;
	}

	public BoogieType getReferenceType() {
		return this.referenceType;
	}

	public BoogieType getVoidType() {
		return this.voidType;
	}

	public BoogieType getFieldType() {
		return this.fieldType;
	}

	public BoogieType getJavaClassType() {
		return this.javaClassType;
	}

	public IdentifierExpression getFieldAllocVariable() {
		return fieldAllocVariable;
	}

	public IdentifierExpression getFieldClassVariable() {
		return fieldClassVariable;
	}

	public FunctionDeclaration getArrayTypeConstructor() {
		return arrayTypeConstructor;
	}

	public IdentifierExpression getIntArrayConstructor() {
		return intArrayConstructor;
	}

	public IdentifierExpression getByteArrayConstructor() {
		return byteArrayConstructor;
	}

	public IdentifierExpression getCharArrayConstructor() {
		return charArrayConstructor;
	}

	public IdentifierExpression getLongArrayConstructor() {
		return longArrayConstructor;
	}

	public IdentifierExpression getBoolArrayConstructor() {
		return boolArrayConstructor;
	}

	public IdentifierExpression getIntArrHeapVariable() {
		return this.intArrHeapVariable;
	}

	public IdentifierExpression getRealArrHeapVariable() {
		return this.realArrHeapVariable;
	}

	public IdentifierExpression getBoolArrHeapVariable() {
		return this.boolArrHeapVariable;
	}

	public IdentifierExpression getRefArrHeapVariable() {
		return this.refArrHeapVariable;
	}

	public IdentifierExpression getStringSizeHeapVariable() {
		return this.stringSizeHeapVariable;
	}

	public IdentifierExpression getArrSizeHeapVariable() {
		return this.arrSizeHeapVariable;
	}

	public BoogieType getIntArrType() {
		return this.intArrType;
	}

	public BoogieType getRefArrType() {
		return this.refArrType;
	}

	public BoogieType getRealArrType() {
		return this.realArrType;
	}

	public BoogieType getBoolArrType() {
		return this.boolArrType;
	}

	public BoogieType getFieldType(BoogieType type) {
		if (type instanceof ConstructedType) {
			ConstructedType ctype = (ConstructedType) type;
			if (ctype.getConstr().getName() == this.fieldTypeName) {
				if (ctype.getConstr().getParamCount() == 1) {
					return ctype.getParameter(0);
				}
			}
		}
		throw new RuntimeException("The type " + type + " is not a Field type.");
	}

	public Expression intToBool(Expression exp) {
		Expression args[] = { exp };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(exp.getLocation(), this.int2bool, args);
	}

	public Expression boolToInt(Expression exp) {
		Expression args[] = { exp };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(exp.getLocation(), this.bool2int, args);
	}

	public Expression refToBool(Expression exp) {
		Expression args[] = { exp };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(exp.getLocation(), this.ref2bool, args);
	}

	public Expression intToReal(Expression exp) {
		Expression args[] = { exp };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(exp.getLocation(), this.int2real, args);
	}

	public Expression realToInt(Expression exp) {
		Expression args[] = { exp };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(exp.getLocation(), this.real2int, args);
	}

	public Expression compareExpr(Expression left, Expression right) {
		if (left.getType() != right.getType()) {
			throw new RuntimeException(
					"can only compare expression of same type");
		}
		FunctionDeclaration operator;
		if (left.getType() == GlobalsCache.v().getPf().getIntType()) {
			operator = this.cmpInt;
		} else if (left.getType() == GlobalsCache.v().getPf().getRealType()) {
			operator = this.cmpReal;
		} else if (left.getType() == GlobalsCache.v().getPf().getBoolType()) {
			operator = this.cmpBool;
		} else if (left.getType() == this.referenceType) {
			operator = this.cmpRef;
		} else {
			throw new RuntimeException("cannot compare expressions of type "
					+ left.getType());
		}
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), operator, args);
	}

	public Expression shiftLeft(Expression left, Expression right) {
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), this.shlInt, args);
	}

	public Expression shiftRight(Expression left, Expression right) {
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), this.shrInt, args);
	}

	public Expression uShiftRight(Expression left, Expression right) {
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), this.ushrInt, args);
	}

	public Expression xorExpr(Expression left, Expression right) {
		if (left.getType() != right.getType()) {
			throw new RuntimeException(
					"can only compare expression of same type");
		}
		FunctionDeclaration operator;
		if (left.getType() == GlobalsCache.v().getPf().getIntType()) {
			operator = this.xorInt;
		} else {
			throw new RuntimeException("cannot compare expressions of type "
					+ left.getType());
		}
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), operator, args);
	}

	public Expression bitAndExpr(Expression left, Expression right) {
		if (left.getType() != right.getType()) {
			throw new RuntimeException(
					"can only compare expression of same type");
		}
		FunctionDeclaration operator;
		if (left.getType() == GlobalsCache.v().getPf().getIntType()) {
			operator = this.bitAnd;
		} else {
			throw new RuntimeException("cannot compare expressions of type "
					+ left.getType());
		}
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), operator, args);
	}

	public Expression bitOrExpr(Expression left, Expression right) {
		if (left.getType() != right.getType()) {
			throw new RuntimeException(
					"can only compare expression of same type");
		}
		FunctionDeclaration operator;
		if (left.getType() == GlobalsCache.v().getPf().getIntType()) {
			operator = this.bitOr;
		} else {
			throw new RuntimeException("cannot compare expressions of type "
					+ left.getType());
		}
		Expression args[] = { left, right };
		return GlobalsCache.v().getPf()
				.mkFunctionApplication(left.getLocation(), operator, args);
	}

}
