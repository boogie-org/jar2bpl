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
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.Tag;
import boogie.expression.Expression;
import boogie.location.ILocation;
import boogie.type.BoogieType;

/**
 * @author martin
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

	private static String replaceIllegalChars(String s) {
		String ret = s.replace("<", "$la$");
		ret = ret.replace(">", "$ra$");
		ret = ret.replace("[", "$lp$");
		ret = ret.replace("]", "$rp$");
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
			return GlobalsCache
					.v()
					.getPf()
					.mkRealLiteral(TranslationHelpers.createDummyLocation(),
							"0.0");
		} else if (expr == SootPrelude.v().getNullConstant()
				&& target == GlobalsCache.v().getPf().getIntType()) {
			return GlobalsCache
					.v()
					.getPf()
					.mkIntLiteral(TranslationHelpers.createDummyLocation(), "0");
		}
		Log.error("Cannot cast " + expr.toString() + "from: " + expr.getType()
				+ " to " + target);
		return expr;
	}

}
