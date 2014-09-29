///*
// * Joogie translates Java bytecode to the Boogie intermediate verification language
// * Copyright (C) 2011 Martin Schaef and Stephan Arlt
// * 
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version.
// * 
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
// */
//
import java.util.LinkedList;
//
///**
// * Calling an instance method on the object referred by a null reference.
// * Accessing or modifying an instance field of the object referred by a null
// * reference. If the reference type is an array type, taking the length of a
// * null reference. If the reference type is an array type, accessing or
// * modifying the slots of a null reference. If the reference type is a subtype
// * of Throwable, throwing a null reference.
// */
//
public class NullPointerExceptions {

	public void foo(Integer[] destination) throws Exception {

			for (int i = 0; i <= destination.length; i++)
				destination[i] = i;
			return;

		
	}

	 
}
