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

package org.joogie.soot;

import boogie.ast.location.ILocation;

/**
 * @author schaef
 * 
 */
public class SootLocation implements ILocation {

	int lineNumber;

	public SootLocation(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getFileName()
	 */
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getStartLine()
	 */
	@Override
	public int getStartLine() {
		return this.lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getEndLine()
	 */
	@Override
	public int getEndLine() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getStartColumn()
	 */
	@Override
	public int getStartColumn() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getEndColumn()
	 */
	@Override
	public int getEndColumn() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#getOrigin()
	 */
	@Override
	public ILocation getOrigin() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boogie.location.ILocation#isLoop()
	 */
	@Override
	public boolean isLoop() {
		// TODO Auto-generated method stub
		return false;
	}

}
