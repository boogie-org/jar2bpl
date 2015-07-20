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

package org.joogie.runners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.joogie.GlobalsCache;
import org.joogie.Options;
import org.joogie.soot.SootBodyTransformer;
import org.joogie.util.Log;

import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;

/**
 * The Soot Runner
 * 
 * @author schaef
 */
public class SootRunner extends Runner {

	/**
	 * stderr print stream
	 */
	private PrintStream stderr;

	/**
	 * stdout print stream
	 */
	private PrintStream stdout;

	/**
	 * Runs Soot by using a JAR file
	 * 
	 * @param jarFile
	 *            JAR file
	 * @param boogieFile
	 *            Boogie file
	 */
	public void runWithJar(String jarFile, String boogieFile) {
		try {
			// command-line arguments for Soot
			List<String> args = new ArrayList<String>();
			fillSootArgs(args);

			// extract dependent JARs
			List<File> jarFiles = new ArrayList<File>();
			extractClassPath(new File(jarFile), jarFiles);
			jarFiles.add(new File(jarFile));
			fillClassPath(jarFiles);

			// additional classpath available?
			String cp = buildClassPath(jarFiles);
			if (Options.v().hasClasspath()) {
				cp += File.pathSeparatorChar + Options.v().getClasspath();
			}

			// set soot-class-path
			args.add("-cp");
			args.add(cp);

			// set classes
			enumClasses(new File(jarFile), args);

			// finally, run soot
			run(args, boogieFile);

		} catch (Exception e) {
			Log.error(e.toString());
		}
	}

	
	/**
	 * Runs Soot by using a class (e.g., from Joogie)
	 * 
	 * @param classFile
	 *            Class file
	 * @param boogieFile
	 *            Boogie file
	 */
	public void runWithClass(String classFile, String boogieFile) {
		try {
			// dependent JAR files
			List<File> jarFiles = new ArrayList<File>();
			fillClassPath(jarFiles);

			// additional classpath available?
			String cp = buildClassPath(jarFiles);
			if (Options.v().hasClasspath()) {
				cp += File.pathSeparatorChar + Options.v().getClasspath();
			}

			// command-line arguments for Soot
			List<String> args = new ArrayList<String>();
			fillSootArgs(args);

			// add soot-class-path
			args.add("-cp");
			args.add(cp);
			args.add(classFile);		
			
			// finally, run soot
			run(args, boogieFile);

		} catch (Exception e) {
			Log.error(e.toString());
		}
	}

	public void runWithApk(String apkFile, String boogieFile) {
		try {
			// command-line arguments for Soot
			
			List<String> args = new ArrayList<String>();
			fillSootArgs(args);

			String cp = apkFile;
			
			//enforce android
			args.add("-src-prec");
			args.add("apk");	
			//add path to android framework stubs
			args.add("-android-jars");
			String android_path = Options.v().getAndroidStubPath();
			if (android_path==null) {
				throw new RuntimeException("Need to specify -android-jars when analyzing apk's.");
			}
			args.add(android_path);
			// add soot-class-path
			args.add("-cp");
			args.add(cp);
			
			//add process-dir
			args.add("-process-dir");
			args.add(apkFile);
						
			// finally, run soot
			run(args, boogieFile);

		} catch (Exception e) {
			Log.error(e.toString());
		}		
	}
	
	
	/**
	 * Runs Soot by using a path (e.g., from Joogie)
	 * 
	 * @param path
	 *            Path
	 * @param boogieFile
	 *            Boogie file
	 */
	public void runWithPath(String path, String boogieFile) {
		try {
			// dependent JAR files
			List<File> jarFiles = new ArrayList<File>();
			fillClassPath(jarFiles);

			// additional classpath available?
			String cp = buildClassPath(jarFiles);
			if (Options.v().hasClasspath()) {
				cp += File.pathSeparatorChar + Options.v().getClasspath();
			}

			// command-line arguments for Soot
			List<String> args = new ArrayList<String>();
			fillSootArgs(args);

			// add soot-class-path
			args.add("-cp");
			args.add(cp);
			
			args.add("-src-prec");
			args.add("class");
			
			// add path to be processed
			args.add("-process-path");
			args.add(path);

			
			
			// finally, run soot
			run(args, boogieFile);

		} catch (Exception e) {
			Log.error(e.toString());
		}
	}

	/**
	 * Runs Soot
	 * 
	 * @param args
	 *            Command-line arguments
	 * @param boogieFile
	 *            Boogie file
	 */
	protected void run(List<String> args, String boogieFile) {
		try {
			// init stream redirection
			initStream();

			// reset & init Soot
			soot.G.reset();
			
			//check if we need fullprogram analysis
			if (Options.v().useSoundThreads()) {
				args.add("-w");

				
				args.add("-p");
				args.add("cg.spark");
				args.add("enabled:true");

				//enable field RW analysis		
				args.add("-p");
				//args.add("jap.fieldrw");
				args.add("jap.sea");
				args.add("enabled:true");
				
				//enable MayHappenInParallel analysis
				args.add("-p");
				args.add("wjtp.mhp");
				args.add("enabled:true");
				
			} else {
				// Iterator Hack
				Scene.v().addBasicClass("org.eclipse.jdt.core.compiler.CategorizedProblem",SootClass.HIERARCHY);
				Scene.v().addBasicClass("java.lang.Iterable",SootClass.SIGNATURES);			
				Scene.v().addBasicClass("java.util.Iterator",SootClass.SIGNATURES);
				Scene.v().addBasicClass("java.lang.reflect.Array",SootClass.SIGNATURES);
				
			}
			
			Pack pack = PackManager.v().getPack("jtp");
			
			//pack.add(new Transform("jtp.NullCheckEliminator",new NullCheckEliminator()));
			
			pack.add(new Transform("jtp.BoogieTransform",
					new SootBodyTransformer()));
			
			StringBuilder sb = new StringBuilder();
			for (String s : args) {
				sb.append(" "+s);
			}
			Log.info("Running soot with "+sb.toString());


			//log everything soot has to say
		    PrintStream origOut = System.out;
		    PrintStream origErr = System.err;		    
		    System.setOut(new Interceptor(origOut));
		    System.setErr(new Interceptor(origErr));
			// Finally, run Soot		    
			soot.Main.main(args.toArray(new String[args.size()]));

			//reset the pipes
			System.setOut(origOut);
		    System.setErr(origErr);
			
			//CallGraph cg = Scene.v().getCallGraph();
//			StringBuilder sb = new StringBuilder();
//			sb.append("Entrypoints: \n");
//			for (SootMethod m : Scene.v().getEntryPoints()) {
//				sb.append("\t");
//				sb.append(m.getName());
//				sb.append("\n");
//			}
//			Log.error(sb);
			
			// write boogie program to file
			if (null != boogieFile && !boogieFile.isEmpty()) {
				GlobalsCache.v().getPf().toFile(boogieFile);
			}
			
			if (Options.v().getRunTypeChecker()) {
				GlobalsCache.v().getPf().runTypeChecker();
			}
			Log.info("Done parsing.");
		} catch (Exception e) {
			Log.error(e);
		} finally {
			// reset stream redirection
			resetStream();
		}
	}

	
	private static class Interceptor extends PrintStream
	{	
	    public Interceptor(OutputStream out) throws UnsupportedEncodingException
	    {
	        super(out, true, "UTF-8");
	    }
	    @Override
	    public void print(String s)
	    {
	    	Log.info(s);
	    }
	}	
	
	/**
	 * Fills a list with the standard command-line arguments needed by Soot
	 * 
	 * @param args
	 *            Command-line arguments
	 */
	protected void fillSootArgs(List<String> args) {
		args.add("-keep-line-number");
//		args.add("-keep-offset");
		args.add("-pp");		
//		args.add("-print-tags");
//		args.add("true");
		if (org.joogie.Options.v().isDebug()) {
			args.add("-f");
			args.add("jimple");
			args.add("-d");
			args.add("./dump");
		} else {
			args.add("-output-format");
			args.add("none");
		}
		args.add("-allow-phantom-refs");
//		args.add("true");
		//args.add("-w");		
	}

	/**
	 * Fills a list with the standard JAR files needed by Soot
	 * 
	 * @param files
	 *            Standard JAR files needed by Soot
	 */
	protected void fillClassPath(List<File> files) {
		// add Runtime Library
		files.add(new File(new File(System.getProperty("java.home"), "lib"),
				"rt.jar"));

		// add Java Cryptography Extension Library
		files.add(new File(new File(System.getProperty("java.home"), "lib"),
				"jce.jar"));
	}

	/**
	 * Returns the class path argument for Soot
	 * 
	 * @param files
	 *            Files in the class path
	 * @return Class path argument for Soot
	 */
	protected String buildClassPath(List<File> files) {
		StringBuilder sb = new StringBuilder();
		for (File file : files) {
			sb.append(file.getPath() + File.pathSeparatorChar);
		}
		return sb.toString();
	}

	/**
	 * Extracts dependent JARs from the JAR's manifest
	 * 
	 * @param file
	 *            JAR file object
	 * @param jarFiles
	 *            List of dependent JARs
	 */
	protected void extractClassPath(File file, List<File> jarFiles) {
		try {
			// open JAR file
			JarFile jarFile = new JarFile(file);

			// get manifest and their main attributes
			Attributes mainAttributes = jarFile.getManifest()
					.getMainAttributes();
			String classPath = mainAttributes
					.getValue(Attributes.Name.CLASS_PATH);

			// close JAR file
			jarFile.close();

			// empty class path?
			if (null == classPath)
				return;

			// look for dependent JARs
			String[] classPathItems = classPath.split(" ");
			for (String classPathItem : classPathItems) {
				if (classPathItem.endsWith(".jar")) {
					// add jar
					Log.debug("Adding " + classPathItem
							+ " to Soot's class path");
					jarFiles.add(new File(file.getParent(), classPathItem));
				}
			}

		} catch (IOException e) {
			Log.error(e.toString());
		}
	}

	/**
	 * Enumerates all classes in a JAR file
	 * 
	 * @param file
	 *            JAR file object
	 * @param classes
	 *            List of classes
	 */
	protected void enumClasses(File file, List<String> classes) {
		try {
			// open JAR file
			Log.debug("Opening jar " + file.getPath());
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();

			// iterate JAR entries
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();

				if (entryName.endsWith(".class")) {
					// get class
					String className = entryName.substring(0,
							entryName.length() - ".class".length());
					className = className.replace('/', '.');

					// is class in scope?
					if (Options.v().hasScope()) {
						if (!className.startsWith(Options.v().getScope())) {
							continue;
						}
					}

					// add class
					Log.debug("Adding class " + className);
					classes.add(className);
				}
			}

			// close JAR file
			jarFile.close();

		} catch (IOException e) {
			Log.error(e.toString());			
		}
	}

	/**
	 * Initializes stream redirection
	 */
	@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value="DM_DEFAULT_ENCODING")
	protected void initStream() {
		// backup stderr and stdout
		stderr = System.err;
		stdout = System.out;

		// redirect stderr and stdout
		// if there is at least one receiver registered
		if (receivers.size() > 0) {
			try {
				PrintStream ps = new PrintStream(new FilteredStream(
						new ByteArrayOutputStream()),true, "UTF-8");
				System.setErr(ps);
				System.setOut(ps);
			} catch (UnsupportedEncodingException e) {
				Log.error(e.toString());
				stderr = System.err;
				stdout = System.out;
			}
		}
	}

	/**
	 * Resets stream redirection
	 */
	protected void resetStream() {
		// restore stderr and stdout
		System.setErr(stderr);
		System.setOut(stdout);
	}

	/**
	 * FilteredStream
	 * 
	 * @author schaef
	 */

	/**
	 * FilteredStream
	 * 
	 * @author schaef
	 */
	class FilteredStream extends FilterOutputStream {

		private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
		
		/**
		 * C-tor
		 * 
		 * @param stream
		 *            OutputStream
		 */
		public FilteredStream(OutputStream stream) {
			super(stream);
		}

		@Override
		
		public void write(byte b[]) throws IOException {
			String s = new String(b, UTF8_CHARSET);
			SootRunner.this.notifyReceivers(s);
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			String s = new String(b, off, len, UTF8_CHARSET);
			SootRunner.this.notifyReceivers(s);
		}
	}

}
