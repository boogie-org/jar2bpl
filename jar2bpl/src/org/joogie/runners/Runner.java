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

package org.joogie.runners;

import java.util.ArrayList;
import java.util.List;

import org.joogie.runners.receivers.Receiver;

/**
 * An abstract Runner
 * 
 * @author arlt
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
