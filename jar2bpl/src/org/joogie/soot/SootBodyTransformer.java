/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaeaeaeaeaeaeaeaeaef and Stephan Arlt
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.joogie.GlobalsCache;
import org.joogie.Options;
import org.joogie.util.Log;
import org.joogie.util.TranslationHelpers;

import soot.Body;
import soot.BodyTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnNamePosTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import boogie.ast.Attribute;
import boogie.ast.declaration.Implementation;
import boogie.ast.statement.Statement;
import boogie.enums.BinaryOperator;

/**
 * Boogie Body Transformer
 * 
 * @author schaef
 */
public class SootBodyTransformer extends BodyTransformer {

	/**
	 * C-tor
	 * 
	 * @param report
	 *            Report
	 */
	public SootBodyTransformer() {
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void internalTransform(Body arg0, String arg1, Map arg2) {
		if (Options.v().hasScope()) {
			SootMethod method = arg0.getMethod();
			SootClass clazz = method.getDeclaringClass();
			String packageName = clazz.getPackageName();

			if (!packageName.startsWith(Options.v().getScope())) {
				return; // ignore current body
			}
		}

		
		// report.addMethod(sootMethod);
		GlobalsCache.v().currentMethod = arg0.getMethod(); 
		transformStmtList(arg0);
		GlobalsCache.v().currentMethod = null;
	}

	/**
	 * Transforms a list of statements
	 * 
	 * @param body
	 *            Body
	 */
	private void transformStmtList(Body body) {
				
		SootProcedureInfo procInfo = GlobalsCache.v().lookupProcedure(
				body.getMethod());

		if (procInfo.getBoogieProcedure()!=null) {
			Log.info("Procedure "+body.getMethod().getBytecodeSignature()+" already known from Prelude");
			return;
		}
		
		
		LinkedList<Statement> boogieStatements = new LinkedList<Statement>();
		
		//now add all assumptions about the types of the in and out parameters
		boogieStatements.addAll(procInfo.typeAssumptions);
		
		ExceptionalUnitGraph tug = procInfo.getExceptionalUnitGraph();
		Iterator<Unit> stmtIt = tug.iterator();
		
		//in a first pass, check if statements have been duplicated
		//in the bytecode, e.g. for finally-blocks, which is used
		//later to generate attributes that suppress false alarms
		//during infeasible code detection.				
		TranslationHelpers.clonedFinallyBlocks = detectDuplicatedFinallyBlocksAndCheckForSynchronizedStuff(stmtIt);
		
		//reset the iterator
		stmtIt = tug.iterator();
		
		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			
			SootStmtSwitch bss = new SootStmtSwitch(procInfo);
			s.apply(bss);
			LinkedList<Statement> stmts = bss.popAll();
			boogieStatements.addAll(stmts);
			
		}

		Attribute[] attributes = TranslationHelpers.javaLocation2Attribute(body.getTags());
		
		if (procInfo.getThisReference()!=null) {
			//for non-static procedures we have to assume that .this is non-null
			boogieStatements.addFirst(
					GlobalsCache.v().getPf().mkAssumeStatement(attributes, 
								GlobalsCache.v().getPf().mkBinaryExpression( 
										GlobalsCache.v().getPf().getBoolType(), 
										BinaryOperator.COMPNEQ, 
											procInfo.getThisReference(), 
											SootPrelude.v().getNullConstant()))						
						);		
		}
		
		//now create the procedure implementation that combines
		//the signature procInfo and the body.
		Implementation proc = GlobalsCache
				.v()
				.getPf()
				.mkProcedure(
						procInfo.getProcedureDeclaration(),
						boogieStatements.toArray(new Statement[boogieStatements
								.size()]), procInfo.getLocalVariables());
				
		procInfo.setProcedureImplementation(proc);
		//GlobalsCache.v().modifiedInMonitor.clear();
	}
	
	/**
	 * In the bytecode, finally-blocks are duplicated. To prevent false positives 
	 * during infeasible code detection it is vital to detect and flag these duplications.
	 * The challenge is that they are not exact clones. Variables might be slightly different,
	 * etc. 
	 * Hence we look for blocks of statements that:
	 * a) each statement has the same java line number
	 * b) the blocks are not connected
	 * c) they have a subsequence of instructions with exactly the same types.
	 * This is certainly unsound but seems to work so far.
	 * @param stmtIt
	 */
	private HashSet<Stmt> detectDuplicatedFinallyBlocksAndCheckForSynchronizedStuff(Iterator<Unit> stmtIt) {
		//TODO: instead of just returning the set of stmts that have duplicates
		//we could group them so that we can still report infeasible code
		//if all duplicates of one statement are infeasible.
		HashSet<Stmt> duplicates = new HashSet<Stmt>();
		
		HashMap<Integer, LinkedList<Stmt>> subprogs = new HashMap<Integer, LinkedList<Stmt>>();
		
		LinkedList<Stmt> subprog = null;
		int old_line = -100; // pick a negative constant that is not a line number
				
		//GlobalsCache.v().modifiedInMonitor = new HashMap<EnterMonitorStmt, HashSet<Value>>();
		
		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			
			//now check for finally blocks
			int line=-2;			
			for (Tag tag : s.getTags()) {
				if (tag instanceof LineNumberTag) {
					LineNumberTag t = (LineNumberTag)tag;					
					line = t.getLineNumber();
				} else if (tag instanceof SourceLnNamePosTag) {
					SourceLnNamePosTag t = (SourceLnNamePosTag)tag;
					line = t.startLn();
				}	
			}
			
//			System.err.println(line+": "+s);
			if (line==old_line) {
				subprog.add(s);
			} else {
				if (subprog!=null) {
					if (!subprogs.containsKey(old_line)) {
						subprogs.put(old_line, subprog);
					} else {
						if (compareSubprogs(subprog, subprogs.get(old_line))) {
//							System.err.println("P1 " + old_line);
							for (Stmt st : subprogs.get(old_line)) {
//								System.err.println("\t"+st);
								duplicates.add(st);
							}
//							System.err.println("P2 " + old_line);
							for (Stmt st : subprog) {
//								System.err.println("\t"+st);
								duplicates.add(st);
							}							
						}
					}
				}
				subprog = new LinkedList<Stmt>();
				subprog.add(s);
				old_line = line;
			}

		}
		return duplicates;
	}
	
	
	private boolean compareSubprogs(LinkedList<Stmt> p1, LinkedList<Stmt> p2) {		
		LinkedList<Stmt> l1, l2;
		if (p1.size()<p2.size()) {
			l2=p1; l1=p2;
		} else {
			l2=p2; l1=p1;
		}
		
		for (int i=0; i<l1.size();i++) {
			
			if (l1.size()-i<l2.size()) {
				//then they cannot be sublists anymore
				return false;
			}
			boolean sublist = true;
						
			for (int j=0; j<l2.size();j++) {
				if (!shallowCompareStatements(l1.get(i+j),l2.get(j))) {
					sublist = false;
					break;
				}
			}
			if (sublist) return true;
		}
		
		return false;
	}
	
	private boolean shallowCompareStatements(Stmt s1, Stmt s2) {
		if (s1.getClass() == s2.getClass()) {
			return true;
		}
		//also consider the case that throw, return, and goto might have been changed in the
		//copies. so do not compare them
		if (isJumpStmt(s1) && isJumpStmt(s2)) {
			return true;
		}
		return false;
	}
	
	private boolean isJumpStmt(Stmt st) {
		return ( st instanceof ThrowStmt || st instanceof GotoStmt || st instanceof ReturnStmt);	
	}
	
}
