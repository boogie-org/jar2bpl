/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaeaeaeaeaeaeaeaeaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie;

import org.kohsuke.args4j.Option;

/**
 * Options
 * 
 * @author schaef, schaef
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

	public void setExceptionErrorModel(boolean b) {
		this.exceptionErrorModel = b;
	}

	
	//TODO: check if this is still needed.
	@Option(name = "-rtr", usage = "Also ensure on caller side that procedure calls do not throw runtime exceptions", required = false)
	private boolean runtimeExceptionReturns=false;
	public boolean isRuntimeExceptionReturns() {
		return runtimeExceptionReturns;
	}
	
	public void setRuntimeExceptionReturns(boolean b) {
		this.runtimeExceptionReturns = b;
	}
	
	

	@Option(name = "-threads", usage = "havoc variables that may be modified by other threads.", required = false)
	private boolean soundThreads=false;
	public boolean useSoundThreads() {
		return soundThreads;
	}
	public void setSoundThreads(boolean st) {
		this.soundThreads = st;
	}


	@Option(name = "-debug-mode", usage = "Debug mode. E.g., prints jimple output to ./dump", required = false)
	private boolean debug=false;
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean val) {
		debug=val;
	}
	
	
	@Option(name = "-vcalls", usage = "Use sound translation for abstract and virtual calls.", required = false)
	private boolean soundCalls=false;
	public boolean useSoundCalls() {
		return soundCalls;
	}
	public void useSoundCalls(boolean b) {
		soundCalls = b;
	}
	
	
	@Option(name = "-main", usage = "Set the Main class for full program analysis.")
	private String mainClassName;
	public String getMainClassName() {
		return mainClassName;
	}
	public void setMainClassName(String name) {
		mainClassName = name;
	}
	
	
	@Option(name = "-tc", usage = "Perfrom type check after translation", required = false)
	private boolean runTypeChecker=false;
	public boolean getRunTypeChecker() {
		return runTypeChecker;
	}
	public void setRunTypeChecker(boolean b) {
		runTypeChecker = b;
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
	private String preludeFileName;
	public String getPreludeFileName() {
		return preludeFileName;
	}
	
	
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
	 * Option object
	 */
	private static Options options;

	public static void resetInstance() {
		options = null;	
	}
	
	
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
