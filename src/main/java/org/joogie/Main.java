/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie;

import org.joogie.util.Log;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Main-Class
 * 
 * @author schaef
 */
public class Main {

	/**
	 * Main method
	 * 
	 * @param args
	 *            Command-line arguments
	 */
	public static void main(String[] args) {
		Options options = Options.v();
		CmdLineParser parser = new CmdLineParser(options);
		
		//soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG cfg = new soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG(); 
		
		
		try {
			// parse command-line arguments
			parser.parseArgument(args);
			runConsoleMode();
		} catch (CmdLineException e) {
			Log.error(e.toString());
			Log.error("java -jar joogie.jar [options...] arguments...");
			parser.printUsage(System.err);
		}
	}

	/**
	 * Launches Joogie in console mode
	 */
	public static void runConsoleMode() {
		try {
			// create dispatcher
			Dispatcher.run(Options.v().getJarFile(),
					Options.v().getBoogieFile());

		} catch (Exception e) {
			Log.error(e.toString());
		}
	}

}
