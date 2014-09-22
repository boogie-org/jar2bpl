/**
 * 
 */
package org.joogie.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.joogie.soot.SootProcedureInfo;

import soot.Trap;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;

/**
 * @author schaef
 * 
 */
public class CommonSubsequences {
	
	public static class JimpleLine {
		public Integer line;
		public Stmt statement;
		
		public JimpleLine(Integer line, Stmt statement) {
			this.line = line;
			this.statement = statement;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.line);
			sb.append("@");
			if (isJumpStmt(this.statement)) {
				sb.append("jump!");
			} else {
				sb.append(this.statement.getClass().toString());
			}
			return sb.toString(); //TODO
		}
		
		@Override
		public boolean equals(Object other) {			
			if (other instanceof CommonSubsequences.JimpleLine) {				
				JimpleLine o = (JimpleLine)other;
				if (!o.line.equals(this.line)) {
					return false;
				}
//				return true;
				return shallowCompareStatements(this.statement, o.statement);
			}
			return false;
		}
		
		private boolean shallowCompareStatements(Stmt s1, Stmt s2) {
			if (s1 instanceof InvokeStmt && s2 instanceof InvokeStmt) {
				return ((InvokeStmt)s1).getInvokeExpr().getMethod().getName().equals(((InvokeStmt)s2).getInvokeExpr().getMethod().getName());
			}
			
			if (s1 instanceof AssignStmt && s2 instanceof AssignStmt) {
				AssignStmt a1 = (AssignStmt)s1;
				AssignStmt a2 = (AssignStmt)s2;
				if (a1.getRightOp() instanceof InvokeExpr && a2.getRightOp() instanceof InvokeExpr) {
					return ((InvokeExpr)a1.getRightOp()).getMethod().equals(((InvokeExpr)a2.getRightOp()).getMethod());
				} else if (a1.getRightOp() instanceof InvokeExpr) {
					return false;
				} else if (a2.getRightOp() instanceof InvokeExpr) {
					return false;
				}
			}
			
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

	// return the longest common prefix of s and t
	public static List<JimpleLine> lcp(List<JimpleLine> s, List<JimpleLine> t) {
		int n = Math.min(s.size(), t.size());
		for (int i = 0; i < n; i++) {
			if (!s.get(i).equals(t.get(i)))
				return s.subList(0, i);
		}
		return s.subList(0, n);
	}

	// return the longest repeated List<JimpleLine> in s
	public static HashSet<Stmt> collectDuplicatedStatements(List<JimpleLine> s, SootProcedureInfo procInfo) {
		HashSet<Stmt> handlerStmts = new HashSet<Stmt>();
		if (procInfo.getSootMethod().getActiveBody()!=null) {
			for (Trap t : procInfo.getSootMethod().getActiveBody().getTraps()) {
				if (t.getHandlerUnit() instanceof Stmt) {
					System.err.println ( ((Stmt)t.getHandlerUnit()).getJavaSourceStartLineNumber() );
					handlerStmts.add((Stmt)t.getHandlerUnit());
				}
			}
		}
		
		HashSet<Stmt> repreatedStatements = new HashSet<Stmt>();
		
		// form the N suffixes
		int N = s.size();
		List<List<JimpleLine>> suffixes = new LinkedList<List<JimpleLine>>();		
		for (int i = 0; i < N; i++) {			
			suffixes.add(s.subList(i, N));
		}

		// sort them
		sort(suffixes);

		// find longest repeated subList<JimpleLine> by comparing adjacent sorted
		// suffixes

		
		List<JimpleLine> lrs = new LinkedList<JimpleLine>();
		for (int i = 0; i < N - 1; i++) {
			List<JimpleLine> x = lcp(suffixes.get(i), suffixes.get(i + 1));
			
			if (x.size()>0 && !allTheSame(x)) {
				repreatedStatements.addAll(collectAllOccurences(s, x));
//				System.err.print("repeated subsequence: ");
//				for (JimpleLine j : x) {
//					System.err.print(j+", ");
//				}
//				System.err.println();
			}
						
			if (x.size() > lrs.size()) {
				lrs = x;
			}
		}
		
		return repreatedStatements;
	}
	
	private static HashSet<Stmt> collectAllOccurences(List<JimpleLine> list, List<JimpleLine> seq) {
		HashSet<Stmt> stmts = new HashSet<Stmt>();
		if (seq.size()<1) return stmts;
		for (int i=0; i<list.size()-seq.size();i++) {
			if (seq.get(0).equals(list.get(i))) {
				List<JimpleLine> sub = list.subList(i, i+seq.size());
				if (compareLists(sub, seq)) {
					for (JimpleLine jl : sub) {
						stmts.add(jl.statement);
					}
				}
			}
		}
		return stmts;
	}
	
	private static boolean compareLists(List<JimpleLine> a, List<JimpleLine> b) {
		if (a.size()!=b.size()) {
			System.err.println("Wrong size");
			return false;
		}
		for (int i=0; i<a.size();i++) {
			if (!a.get(i).equals(b.get(i))) return false;
		}
		return true;
	}
	
	private static boolean allTheSame(List<JimpleLine> list) {
		if (list==null || list.size()<=1) return false;		
		JimpleLine first = list.get(0);
		for (JimpleLine jl : list) {
			if (!jl.equals(first)) return false;
		}
		return true;
	}

	private static void sort(List<List<JimpleLine>> arr) {
		Collections.sort(arr, new Comparator<List<JimpleLine>>() {
			public int compare(List<JimpleLine> l1, List<JimpleLine> l2) {
				String s1="";
				for (JimpleLine i : l1) s1+=i+" ";
				String s2="";
				for (JimpleLine i : l2) s2+=i+" ";
				return s1.compareTo(s2);
			}
		});		
	}
}
