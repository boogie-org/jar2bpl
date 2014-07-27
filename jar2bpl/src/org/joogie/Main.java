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

package org.joogie;

import org.joogie.util.Log;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Main-Class
 * 
 * @author arlt
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
			Log.error(e.getMessage());
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
			e.printStackTrace();
		}
	}

}
