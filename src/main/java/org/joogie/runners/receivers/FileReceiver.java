/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.runners.receivers;

import java.io.FileWriter;
import java.io.IOException;

import util.Log;

/**
 * File Output Receiver of a Runner
 * 
 * @author schaef
 */
public class FileReceiver implements Receiver {

	/**
	 * FileWriter
	 */
	private FileWriter fileWriter;

	/**
	 * C-tor
	 * 
	 * @param fileWriter
	 *            FileWriter
	 */
	public FileReceiver(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}

	@Override
	public void receive(String text) {
		try {
			fileWriter.write(text);
		} catch (IOException e) {
			Log.error(e.toString());
		}
	}

	@Override
	public void onBegin() {
		// do nothing
	}

	@Override
	public void onEnd() {
		try {
			fileWriter.flush();
		} catch (IOException e) {
			Log.error(e.toString());
		}

	}

}
