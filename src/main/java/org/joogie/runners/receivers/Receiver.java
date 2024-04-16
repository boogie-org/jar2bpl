/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.runners.receivers;

/**
 * Output Receiver of a Runner
 * 
 * @author schaef
 */
public interface Receiver {

	/**
	 * Receives text from output
	 * 
	 * @param text
	 *            Text
	 */
	public void receive(String text);

	/**
	 * Begin of the reception
	 */
	public void onBegin();

	/**
	 * End of the reception
	 */
	public void onEnd();

}
