/*
 * jimple2boogie - Translates Jimple (or Java) Programs to Boogie
 * Copyright (C) 2013 Martin Schaef and Stephan Arlt
 * 
 * This code is distributed under the terms of the MIT license. See the
 * LICENSE file for details.
 */

package org.joogie.util;

/**
 * @author schaef
 */
public class StopWatch {

	/**
	 * Start Time
	 */
	private long startTime;

	/**
	 * Stop Time
	 */
	private long stopTime;

	/**
	 * Enabled or Disabled
	 */
	private boolean enabled;

	/**
	 * Returns a started instance of a stop watch
	 * 
	 * @return Stop watch
	 */
	public static StopWatch getInstanceAndStart() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		return stopWatch;
	}

	/**
	 * Start stop watch
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		enabled = true;
	}

	/**
	 * Stops the stop watch
	 * 
	 * @return Time
	 */
	public long stop() {
		if (!isEnabled())
			return 0;

		stopTime = System.currentTimeMillis();
		enabled = false;

		return getTime();
	}

	/**
	 * Returns the time
	 * 
	 * @return Time
	 */
	public long getTime() {
		if (isEnabled())
			return System.currentTimeMillis() - startTime;

		return stopTime - startTime;
	}

	/**
	 * Checks whether the stop watch is enabled
	 * 
	 * @return true = stop watch is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

}
