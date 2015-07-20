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

package org.joogie.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File I/O
 * 
 * @author schaef
 */
public class FileIO {

	/**
	 * Reads text from a file
	 * 
	 * @param filename
	 *            File name
	 * @return null = failure
	 */
	public static String fromFile(String filename) {
		String s = null;
		File file = new File(filename);
		try (FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			 InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);) {
			StringBuffer sb = new StringBuffer();

			while (null != (s = br.readLine())) {
				sb.append(s + "\r\n");
			}

			s = sb.toString();
			

		} catch (IOException e) {
			Log.error(e.getMessage());
			s = null;
		}
		return s;
	}

	/**
	 * Writes text to a file
	 * 
	 * @param text
	 *            Text
	 * @param filename
	 *            File name
	 * @return false = failure
	 */
	public static boolean toFile(String text, String filename) {
		boolean success = false;
		try ( OutputStreamWriter osw = new OutputStreamWriter(
			     new FileOutputStream(new File(filename).getAbsolutePath()),
			     Charset.forName("UTF-8").newEncoder());
				BufferedWriter bw = new BufferedWriter(osw);
			){
			bw.write(text);
			success = true;
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
		return success;
	}

	/**
	 * Checks if the directory exists
	 * 
	 * @param dirname
	 *            directory name
	 * @return true = directory does exist
	 */
	public static boolean doesDirectoryExist(String dirname) {
		if (null == dirname) {
			return false;
		}

		File file = new File(dirname);
		return file.exists() && file.isDirectory();
	}

	/**
	 * Checks if the file exists
	 * 
	 * @param filename
	 *            File name
	 * @return true = file does exist
	 */
	public static boolean doesFileExist(String filename) {
		if (null == filename) {
			return false;
		}

		File file = new File(filename);
		return file.exists() && file.isFile();
	}

	/**
	 * Checks if the file's path exists
	 * 
	 * @param filename
	 *            File name
	 * @return true = file's path does exist
	 */
	public static boolean doesDirectoryOfFileExist(String filename) {
		if (null == filename) {
			return false;
		}

		File file = new File(filename);
		File fileParent = file.getParentFile();

		if (null == fileParent) {
			return false;
		}

		return fileParent.exists();
	}

	/**
	 * Finds files in a directory
	 * 
	 * @param dirName
	 *            Directory name
	 * @param fileName
	 *            File name
	 * @return Found files
	 */
	public static String[] findFiles(String dirName, String fileName) {
		List<String> files = new ArrayList<String>();
		findFiles(dirName, fileName, files);
		return files.toArray(new String[files.size()]);
	}

	/**
	 * Finds files in a directory
	 * 
	 * @param dirName
	 *            Directory name
	 * @param fileName
	 *            File name
	 * @param files
	 *            List of files
	 */
	protected static void findFiles(String dirName, String fileName,
			List<String> files) {
		try {
			File dir = new File(dirName);
			File[] filesInDir = dir.listFiles();
			if (filesInDir == null) {
				return;
			}
			for (File file : filesInDir) {
				if (file.isDirectory()) {
					findFiles(file.getPath(), fileName, files);
				} else if (file.isFile()) {
					if (file.getName().equals(fileName)) {
						files.add(file.getPath());
					}
				}
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * Generates a temporary filename
	 * 
	 * @param ext
	 *            Extension of the filename
	 * @return Temporary filename
	 */
	public static String generateTempFileName(String ext) {
		return String.format("%s%s.%s", System.getProperty("java.io.tmpdir"),
				UUID.randomUUID().toString(), ext);
	}

}
