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

package org.joogie.util;

import java.util.List;

import org.joogie.GlobalsCache;
import org.joogie.soot.SootLocation;
import org.joogie.soot.SootPrelude;

import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.StaticFieldRef;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.Tag;
import boogie.ast.Attribute;
import boogie.ast.expression.Expression;
import boogie.ast.location.ILocation;
import boogie.ast.statement.Statement;
import boogie.type.BoogieType;

/**
 * @author schaef
 * 
 */
public class TranslationHelpers {

	public static ILocation createDummyLocation() {
		return new SootLocation(-1);
	}

	public static ILocation translateLocation(List<Tag> list) {
		int lineNumber = -1;
		for (Tag tag : list) {
			if (tag instanceof LineNumberTag) {
				lineNumber = ((LineNumberTag) tag).getLineNumber();
				break;
			} else if (tag instanceof SourceLnNamePosTag) {
				lineNumber = ((SourceLnNamePosTag) tag).startLn();
				break;
			}
		}
		return new SootLocation(lineNumber);
	}

	public static Statement mkLocationAssertion(List<Tag> list) {
		return GlobalsCache
				.v()
				.getPf()
				.mkAssertStatement(javaLocation2Attribute(list),
						GlobalsCache.v().getPf().mkBooleanLiteral(true));
	}

	public static Attribute[] javaLocation2Attribute(List<Tag> list) {
		// if the taglist is empty return no location
		int startln, endln, startcol, endcol;
		String filename;

		startln = -1;
		endln = -1;
		startcol = -1;
		endcol = -1;
		filename = null;
		
		for (Tag tag : list) {
			if (tag instanceof LineNumberTag) {
				if (GlobalsCache.v().currentMethod!=null) {
					filename = GlobalsCache.v().currentMethod.getDeclaringClass().getName();
				}				
				startln = ((LineNumberTag) tag).getLineNumber();
				break;
			} else if (tag instanceof SourceLnNamePosTag) {				
				startln = ((SourceLnNamePosTag) tag).startLn();
				endln = ((SourceLnNamePosTag) tag).endLn();
				filename = ((SourceLnNamePosTag) tag).getFileName();
				startcol = ((SourceLnNamePosTag) tag).startPos();
				endcol = ((SourceLnNamePosTag) tag).endPos();
				break;
			} else if (tag instanceof SourceFileTag) {
				filename = ((SourceFileTag) tag).getSourceFile();
				break;
			} else {
				Log.debug(tag.getClass().toString() + " "+tag.toString());
			}
		}

		if (filename == null) {
			return new Attribute[0];
		}

		Attribute[] res = { GlobalsCache
				.v()
				.getPf()
				.mkLocationAttribute(filename, startln, endln, startcol, endcol) };
		return res;
	}

	public static String getQualifiedName(SootClass c) {
		StringBuilder sb = new StringBuilder();
		sb.append(c.getName());
		return replaceIllegalChars(sb.toString());
	}

	public static String getQualifiedName(SootMethod m) {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getReturnType().toString() + "$");
		sb.append(m.getDeclaringClass().getName() + "$");
		sb.append(m.getName() + "$");
		sb.append(m.getNumber());
		return replaceIllegalChars(sb.toString());
	}

	public static String getQualifiedName(Local l) {
		// TODO: check if the name is really unique
		StringBuilder sb = new StringBuilder();
		sb.append(l.getName());

		sb.append(l.getNumber());
		return sb.toString();
	}

	public static String getQualifiedName(StaticFieldRef f) {
		return getQualifiedName(f.getField());
	}

	public static String getQualifiedName(SootField f) {
		StringBuilder sb = new StringBuilder();
		sb.append(f.getType() + "$");
		sb.append(f.getDeclaringClass().getName() + "$");
		sb.append(f.getName());
		sb.append(f.getNumber());
		return replaceIllegalChars(sb.toString());
	}

	public static String replaceIllegalChars(String s) {
		String ret = s.replace("<", "$la$");
		ret = ret.replace(">", "$ra$");
		ret = ret.replace("[", "$lp$");
		ret = ret.replace("]", "$rp$");
		ret = ret.replace("/", "$_$");
		ret = ret.replace(";", "$");
		return ret;
	}

	/**
	 * This is a helper function to cast between bool and int if soot does not
	 * distinguish them. TODO: extend to ref if necessary
	 * 
	 * @param expr
	 * @param target
	 * @return
	 */
	public static Expression castBoogieTypes(Expression expr, BoogieType target) {
		if (expr.getType() == target) {
			return expr;
		} else if (expr.getType() == GlobalsCache.v().getPf().getIntType()
				&& target == GlobalsCache.v().getPf().getBoolType()) {
			return SootPrelude.v().intToBool(expr);
		} else if (expr.getType() == GlobalsCache.v().getPf().getBoolType()
				&& target == GlobalsCache.v().getPf().getIntType()) {
			return SootPrelude.v().boolToInt(expr);
		} else if (expr.getType() == GlobalsCache.v().getPf().getIntType()
				&& target == GlobalsCache.v().getPf().getRealType()) {
			return SootPrelude.v().intToReal(expr);
		} else if (expr.getType() == GlobalsCache.v().getPf().getRealType()
				&& target == GlobalsCache.v().getPf().getIntType()) {
			return SootPrelude.v().realToInt(expr);
		} else if (expr.getType() == SootPrelude.v().getReferenceType()
				&& target == GlobalsCache.v().getPf().getBoolType()) {
			return SootPrelude.v().refToBool(expr);
		} else if (expr == SootPrelude.v().getNullConstant()
				&& target == GlobalsCache.v().getPf().getRealType()) {
			return GlobalsCache.v().getPf().mkRealLiteral("0.0");
		} else if (expr == SootPrelude.v().getNullConstant()
				&& target == GlobalsCache.v().getPf().getIntType()) {
			return GlobalsCache.v().getPf().mkIntLiteral("0");
		}

		throw new RuntimeException("Cannot cast "
				+ expr.toString()
				+ " from: "
				+ ((expr.getType() == null) ? "null" : expr.getType()
						.getClass().toString()) + " to "
				+ target.getClass().toString());
		// return expr;
	}

}
