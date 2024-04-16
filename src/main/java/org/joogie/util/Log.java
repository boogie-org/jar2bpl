/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.util;

import org.apache.log4j.Logger;

/**
 * Log
 * 
 * @author schaef
 */
public class Log {

	/**
	 * log4j's Logger object
	 */
	private static Logger logger = null;

	/**
	 * Singleton method
	 * 
	 * @return Logger object
	 */
	public static Logger v() {
		if (null == logger) {
			// create logger
			logger = Logger.getRootLogger();
		}

		return logger;
	}

	/**
	 * Log a message object with the DEBUG Level.
	 * 
	 * @param o
	 *            the message object to log
	 */
	public static void debug(Object o) {
		v().debug(o);
	}

	/**
	 * Log a message object with the INFO Level.
	 * 
	 * @param o
	 *            the message object to log
	 */
	public static void info(Object o) {
		v().info(o);
	}

	/**
	 * Log a message object with the ERROR Level.
	 * 
	 * @param o
	 *            the message object to log
	 */
	public static void error(Object o) {
		v().error(o);
	}

	/**
	 * C-tor
	 */
	private Log() {
		// do nothing
	}
}
