/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.runners;

import java.util.ArrayList;
import java.util.List;

import org.joogie.runners.receivers.Receiver;

/**
 * An abstract Runner
 * 
 * @author schaef
 */
public abstract class Runner {

	/**
	 * List of Receivers
	 */
	protected List<Receiver> receivers = new ArrayList<Receiver>();

	/**
	 * Adds a new Receiver
	 * 
	 * @param receiver
	 *            Receiver
	 */
	public void addReceiver(Receiver receiver) {
		receivers.add(receiver);
		receiver.onBegin();
	}

	/**
	 * Removes all receivers
	 */
	public void clearReceivers() {
		for (Receiver receiver : receivers) {
			receiver.onEnd();
		}
		receivers.clear();
	}

	/**
	 * Notifies all receivers
	 * 
	 * @param text
	 *            Text
	 */
	public void notifyReceivers(String text) {
		for (Receiver receiver : receivers) {
			receiver.receive(text);
		}
	}

}
