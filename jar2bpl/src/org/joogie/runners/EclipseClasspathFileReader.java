/**
 * 
 */
package org.joogie.runners;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author schaef
 *
 */
public class EclipseClasspathFileReader {
	
	public static EclipseClasspathFileReader classpath2Args(String cpFile) {		
		return new EclipseClasspathFileReader(cpFile);
	}

	private static final String SRC = "src";
	private static final String LIB = "lib";
	private static final String CON = "con";
	
	private String cpDirectory;

	private LinkedList<String> args = new LinkedList<String>();
	
	private LinkedList<String> libEntries = new LinkedList<String>();
	private LinkedList<String> srcEntries = new LinkedList<String>();
	
	public LinkedList<String> getArgs() {
		System.err.println("LIB ");
		for (String s : this.libEntries) {
			System.err.println("\t"+s);
		}
		System.err.println("SRC ");
		for (String s : this.srcEntries) {
			System.err.println("\t"+s);
		}
		
		return this.args;
	}

	public LinkedList<String> getSrcEntries() {
		return this.srcEntries;
	}

	public LinkedList<String> getLibEntries() {
		return this.libEntries;
	}
	
	
	private EclipseClasspathFileReader(String filename) {
		File file = new File(filename);
		if (!filename.endsWith(".classpath") || !file.exists() || file.isDirectory()) {
			System.err.println("This is not an eclipse .classpath file! "+filename);
			return;
		}
		
		cpDirectory = file.getAbsoluteFile().getParentFile().getAbsolutePath();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			Document dom = db.parse(filename);

			NodeList nl = dom.getElementsByTagName("classpathentry");
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {					
					Element el = (Element)nl.item(i);
					parseClasspathEntry(el);
				}
			}
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	private void parseClasspathEntry(Element el){
		String kind = el.getAttribute("kind");

		if (kind!=null) {
			if (kind.equals(SRC)) {
				String dir = el.getAttribute("path");
				boolean isOtherProject = false;
				String combineaccessrules = el.getAttribute("combineaccessrules");
				if (combineaccessrules!=null && !combineaccessrules.isEmpty()) {
					isOtherProject = true;
					if (Boolean.getBoolean(combineaccessrules)) {
						//TODO
					} else {
						//TODO
					}
				}
					
				dir = makeAbsolutePath(dir);
				
				if (!isOtherProject) {
					this.srcEntries.add(dir);
				} else {
					//TODO: check if isOtherProject makes sense.
					EclipseClasspathFileReader nested = new EclipseClasspathFileReader(dir+"/.classpath");
					this.srcEntries.addAll(nested.getSrcEntries());
					this.libEntries.addAll(nested.getLibEntries());
					return;						
				}
			} else if (kind.equals(LIB)) {
				String dir = el.getAttribute("path");
				dir = makeAbsolutePath(dir);
				this.libEntries.add(dir);
			} else if (kind.equals(CON)) {
				String dir = el.getAttribute("path");
				this.libEntries.add(dir);
			} else {
				//Ignore
			}
		} else {
			System.err.println("Unknown element "+el.toString());
		}
	}	
	
	private String makeAbsolutePath(String dir) {
		if (dir.startsWith("/")) {
			File file = new File(dir);
			if (!file.exists() || !file.isDirectory()) {
				//in that case, dir is not absolute but refers to the
				//workspace root which we assume is at ./..
				dir = this.cpDirectory+"/.."+dir;
			} 
		} else {
			dir = this.cpDirectory+"/"+dir;
		}
		return dir;
	}
	
	public static void main(String[] args) {
		if (args.length!=1) {
			System.err.println("This takes only one argument, which is the name of an .classpath file");
			return;
		}
		for (String s : classpath2Args(args[0]).getArgs()) {
			System.err.println(s);
		}
	}
	
	
	
}
