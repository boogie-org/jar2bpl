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
import org.joogie.util.Log;
import org.joogie.util.StopWatch;

/**
 * Dispatcher
 * 
 * @author arlt
 */
public class Dispatcher {

	/**
	 * Timeout for VC-Generation (in milliseconds)
	 */
	public final static int VCGEN_TIMEOUT = 30000;

	/**
	 * Input
	 */
	private String input;

	/**
	 * Output
	 */
	private String output;

	/**
	 * Soot runner
	 */
	private SootRunner sootRunner;

	/**
	 * StopWatch for Soot
	 */
	private StopWatch swSoot;

	/**
	 * C-tor
	 * 
	 * @param input
	 *            Input
	 * @param output
	 *            Output
	 * @param report
	 *            Report
	 */
	public Dispatcher(String input, String output) {
		this.input = input;
		this.output = output;
		this.sootRunner = new SootRunner();
	}

	/**
	 * Runs the dispatcher
	 */
	public void run() {
		try {
			Log.debug("Running Soot");
			swSoot = StopWatch.getInstanceAndStart();
			runSoot();
			swSoot.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs Soot
	 */
	protected void runSoot() {
		// no input?
		if (null == input || input.isEmpty()) {
			return;
		}

		if (input.endsWith(".jar")) {
			// run with JAR file
			sootRunner.runWithJar(input, output);
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

	/**
	 * Returns the Soot runner
	 * 
	 * @return Soot runner
	 */
	public SootRunner getSootRunner() {
		return sootRunner;
	}

}
