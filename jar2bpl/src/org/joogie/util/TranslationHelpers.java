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

import java.util.HashSet;
import java.util.List;

import org.joogie.GlobalsCache;
import org.joogie.soot.SootLocation;
import org.joogie.soot.SootPrelude;

import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.Tag;
import boogie.ProgramFactory;
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

	public static Statement mkLocationAssertion(Stmt s) {
		return mkLocationAssertion(s, false);
	}
	
	public static Statement mkLocationAssertion(Stmt s, boolean forceCloneAttribute) {
		return GlobalsCache
				.v()
				.getPf()
				.mkAssertStatement(javaLocation2Attribute(s, forceCloneAttribute),
						GlobalsCache.v().getPf().mkBooleanLiteral(true));
	}
	
	public static HashSet<Stmt> clonedFinallyBlocks = new HashSet<Stmt>();

	public static Attribute[] javaLocation2Attribute(Stmt s) {
		return javaLocation2Attribute(s, false);
	}
	
	public static Attribute[] javaLocation2Attribute(Stmt s, boolean forceCloneAttribute) {
		return javaLocation2Attribute(s.getTags(), clonedFinallyBlocks.contains(s) || forceCloneAttribute);
	}
	
	public static Attribute[] javaLocation2Attribute(List<Tag> list) {
		return javaLocation2Attribute(list, false);	
	}
	
	
	private static String getFileName(SootClass sc) {
		if (sc.hasOuterClass()) return getFileName(sc.getOuterClass());
		String filename = null;
		for (Tag t_ : sc.getTags()) {
			if (t_ instanceof SourceFileTag) {							
				filename = ((SourceFileTag)t_).getSourceFile();
				if (((SourceFileTag)t_).getAbsolutePath()!=null) {
					filename = ((SourceFileTag)t_).getAbsolutePath()+filename;
				}
				break;
			} else if (t_ instanceof SourceLnNamePosTag) {
				filename = ((SourceLnNamePosTag)t_).getFileName();
				//don't break, mybe there is still a source file tag.
			}
		}
		return filename;
	}
	
	public static Attribute[] javaLocation2Attribute(List<Tag> list, boolean isCloned) {
		// if the taglist is empty return no location
		int startln, endln, startcol, endcol;
		

		startln = -1;
		endln = -1;
		startcol = -1;
		endcol = -1;
		String filename = null;

		if (GlobalsCache.v().currentMethod!=null) {
			filename = getFileName(GlobalsCache.v().currentMethod.getDeclaringClass());
		}				

		
		for (Tag tag : list) {
			if (tag instanceof LineNumberTag) {
				startln = ((LineNumberTag) tag).getLineNumber();
				break;
			} else if (tag instanceof SourceLnNamePosTag) {
				if (filename==null) {
					filename = ((SourceLnNamePosTag) tag).getFileName();
				}
				startln = ((SourceLnNamePosTag) tag).startLn();
				endln = ((SourceLnNamePosTag) tag).endLn();				
				startcol = ((SourceLnNamePosTag) tag).startPos();
				endcol = ((SourceLnNamePosTag) tag).endPos();
				break;
			} else if (tag instanceof SourceFileTag) {
				if (filename==null) {
					filename = ((SourceFileTag) tag).getAbsolutePath()+((SourceFileTag) tag).getSourceFile();
					if (((SourceFileTag) tag).getAbsolutePath()!=null) {
						filename = ((SourceFileTag)tag).getAbsolutePath()+filename;
					}
				}
				break;
			} else {
//				Log.info("Tag ignored: " + tag.getClass().toString());
			}
		}

		if (filename == null && startln == -1 && endln == -1 && startcol == -1 && endcol == -1) {			
			return new Attribute[0];
		}    
		
		if (filename == null && GlobalsCache.v().currentMethod!=null) {
			filename = GlobalsCache.v().currentMethod.getDeclaringClass().getName();
		}
		
		ProgramFactory pf = GlobalsCache.v().getPf();
		Attribute loc = pf.mkLocationAttribute(filename, startln, endln, startcol, endcol);
		
		Attribute[] res;
		if (isCloned) {			
			res = new Attribute[]{ loc, pf.mkCustomAttribute(ProgramFactory.Cloned) };
		} else {
			res = new Attribute[]{ loc };
		}
 		
		
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
