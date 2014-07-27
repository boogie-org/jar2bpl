/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaeaeaef and Stephan Arlt
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

package org.joogie;

import java.io.File;

import org.joogie.runners.SootRunner;
import org.joogie.soot.SootPrelude;
import org.joogie.util.Log;
import org.joogie.util.MhpInfo;
import org.joogie.util.StopWatch;

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
			Log.debug("Running Soot");
			swSoot = StopWatch.getInstanceAndStart();
			runSoot(input, output);
			swSoot.stop();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GlobalsCache.resetInstance();
			SootPrelude.resetInstance();
			MhpInfo.resetInstance();
			Options.resetInstance();
		}
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
		} else {
			File file = new File(input);
			if (file.isDirectory()) {
				// run with path
				sootRunner.runWithPath(input, output);
			} else {
				// run with class file
				sootRunner.runWithClass(input, output);
			}
		}
	}

}
