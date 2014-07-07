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

package org.joogie;

import org.kohsuke.args4j.Option;

/**
 * Options
 * 
 * @author arlt, schaef
 */
public class Options {

	/**
	 * Use other runtime exception encoding.
	 * Default encodes RuntimeExceptions as assertions, 
	 * -err encodes them by creating actual exception. 
	 */
	@Option(name = "-err", usage = "Use exception error model", required = false)
	private boolean exceptionErrorModel=false;
	public boolean isExceptionErrorModel() {
		return exceptionErrorModel;
	}

	@Option(name = "-rtr", usage = "Also ensure on caller side that procedure calls do not throw runtime exceptions", required = false)
	private boolean runtimeExceptionReturns=false;
	public boolean isRuntimeExceptionReturns() {
		return runtimeExceptionReturns;
	}
	
	@Option(name = "-threads", usage = "havoc variables that may be modified by other threads.", required = false)
	private boolean soundThreads=false;
	public boolean useSoundThreads() {
		return soundThreads;
	}
	
	
	@Option(name = "-tc", usage = "Perfrom type check after translation", required = false)
	private boolean runTypeChecker=false;
	public boolean getRunTypeChecker() {
		return runTypeChecker;
	}
	
	@Option(name = "-android-jars", usage = "Path to the jars that stub the android platform.")
	private String androidStubPath=null;
	
	public String getAndroidStubPath() {
		return androidStubPath;
	}

	public void setAndroidStubPath(String path) {
		this.androidStubPath = path;
	}
	
	
	@Option(name = "-prelude", usage = "Use custom prelude instead of the built-in one.")
	private String preludeFileName=null;
	
	/**
	 * JAR file
	 */
	@Option(name = "-j", usage = "JAR file", required = false)
	private String jarFile;

	/**
	 * Boogie file
	 */
	@Option(name = "-b", usage = "Boogie file")
	private String boogieFile;

	/**
	 * Scope
	 */
	@Option(name = "--scope", usage = "Scope")
	private String scope;

	/**
	 * Source directory
	 */
	@Option(name = "--sourceDir", usage = "Source Directory")
	private String sourceDir;

	/**
	 * Classpath
	 */
	@Option(name = "-cp", usage = "Classpath")
	private String classpath;


	/**
	 * Returns the JAR file
	 * 
	 * @return JAR file
	 */
	public String getJarFile() {
		return jarFile;
	}

	/**
	 * Returns the Boogie file
	 * 
	 * @return Boogie file
	 */
	public String getBoogieFile() {
		return boogieFile;
	}

	/**
	 * Determines, whether Joogie has a scope
	 * 
	 * @return Scope of Joogie
	 */
	public boolean hasScope() {
		return (null != scope);
	}

	/**
	 * Returns the scope of Joogie
	 * 
	 * @return Scope of Joogie
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * Returns the source directory
	 * 
	 * @return Source directory
	 */
	public String getSourceDir() {
		return sourceDir;
	}

	/**
	 * Determines, whether Joogie has an additional classpath
	 * 
	 * @return true = Joogie has an additional classpath
	 */
	public boolean hasClasspath() {
		return (null != classpath);
	}

	/**
	 * Returns the additional classpath
	 * 
	 * @return Additional classpath
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * Assigns the additional classpath
	 * 
	 * @param classpath
	 *            Additional classpath
	 */
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	
	/**
	 * Uses a user provided prelude file instead of the built in one.
	 * 
	 * @param preludeFileName
	 *            optional alternative prelude file
	 */	
	public String getPreludeFileName() {
		return preludeFileName;
	}
	
	
	/**
	 * Option object
	 */
	private static Options options;

	/**
	 * Singleton method
	 * 
	 * @return Options
	 */
	public static Options v() {
		if (null == options) {
			options = new Options();
		}
		return options;
	}

	/**
	 * C-tor
	 */
	private Options() {
		// do nothing
	}

}
