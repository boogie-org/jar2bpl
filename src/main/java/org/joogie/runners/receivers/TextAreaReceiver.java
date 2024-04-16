/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.runners.receivers;

import javax.swing.JTextArea;

/**
 * Text Area Output Receiver of a Runner
 * 
 * @author schaef
 */
public class TextAreaReceiver implements Receiver {

	/**
	 * Text Area
	 */
	private JTextArea textArea;

	/**
	 * C-tor
	 * 
	 * @param textArea
	 *            TextArea
	 */
	public TextAreaReceiver(JTextArea textArea) {
		this.textArea = textArea;
		this.textArea.setText(null);
	}

	@Override
	public void receive(String text) {
		textArea.append(text);
	}

	@Override
	public void onBegin() {
		// do nothing
	}

	@Override
	public void onEnd() {
		// do nothing
	}

}
