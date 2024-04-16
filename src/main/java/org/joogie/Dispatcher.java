/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaeaeaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie;

import java.io.File;

import org.joogie.runners.SootRunner;
import org.joogie.soot.SootPrelude;
import org.joogie.util.Log;
import org.joogie.util.MhpInfo;
import org.joogie.util.StopWatch;

import boogie.ProgramFactory;

/**
 * Dispatcher
 * 
 * @author schaef
 */
public class Dispatcher {

	/**
	 * Timeout for VC-Generation (in milliseconds)
	 */
	public final static int VCGEN_TIMEOUT = 30000;

	/**
	 * StopWatch for Soot
	 */
	static private StopWatch swSoot;

	/**
	 * Runs the dispatcher
	 */
	public static void run(String input, String output) {
		try {
			//SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
			Log.debug("Running Soot");
			swSoot = StopWatch.getInstanceAndStart();
			runSoot(input, output);
			swSoot.stop();

		} catch (Exception e) {
			Log.error(e.toString());
		} finally {			
			GlobalsCache.resetInstance();
			SootPrelude.resetInstance();
			MhpInfo.resetInstance();
			Options.resetInstance();
			soot.G.reset();
		}
	}
	
	public static void setClassPath(String cp) {
		Options.v().setClasspath(cp);
	}

	/**
	 * Use this run function if you plan to use jar2bpl as a library.
	 * It runs soot, creates a Boogie AST and returns the ProgramFactory but
	 * deletes all soot related data from memory.
	 * @param input
	 * @return
	 */
	public static ProgramFactory run(String input) {
		ProgramFactory pf = null;
		try {
			Log.debug("Running Soot");
			swSoot = StopWatch.getInstanceAndStart();
			runSoot(input, null);
			swSoot.stop();
			pf = GlobalsCache.v().getPf();
		} catch (Exception e) {
			Log.error(e.toString());
		} finally {
			GlobalsCache.resetInstance();
			SootPrelude.resetInstance();
			MhpInfo.resetInstance();
			Options.resetInstance();
			soot.G.reset();
		}
		return pf;
	}
	
	
	/**
	 * Runs Soot
	 */
	protected static void runSoot(String input, String output) {
		SootRunner sootRunner = new SootRunner();
		
		if (null == input || input.isEmpty()) {
			return;
		}

		if (input.endsWith(".jar")) {
			// run with JAR file
			sootRunner.runWithJar(input, output);
		} else if (input.endsWith(".apk")) {
			// run with Android file
			sootRunner.runWithApk(input, output);
		} else if (input.endsWith(".xml")) {
			
		} else {
			File file = new File(input);
			if (file.isDirectory()) {
				// run with path
				sootRunner.runWithPath(input, output);
			} else {
				throw new RuntimeException("Don't know what to run on.");
			}
		}
	}

}
