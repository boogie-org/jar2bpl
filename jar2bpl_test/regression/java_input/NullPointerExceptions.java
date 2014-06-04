/*
 * Joogie translates Java bytecode to the Boogie intermediate verification language
 * Copyright (C) 2011 Martin Schaef and Stephan Arlt
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

import java.util.LinkedList;
/**




Calling an instance method on the object referred by a null reference.
    Accessing or modifying an instance field of the object referred by a null reference.
    If the reference type is an array type, taking the length of a null reference.
    If the reference type is an array type, accessing or modifying the slots of a null reference.
    If the reference type is a subtype of Throwable, throwing a null reference.

 */


public class NullPointerExceptions {

    private void add() {
    }

    public void test() {
	int choice = 1;
	NullPointerExceptions obj1 = null;

	if (choice == 1) {
	    //obj1 = new NullPointerExceptions();
	    obj1 = null;
	    obj1.add();
	}
	return;
    }


 
    /*
    private void setState(boolean b) {}
    private NullPointerExceptions jcbmiViewToolbarMain;

    public Object getToolBarMain(boolean isShowing) {
	jcbmiViewToolbarMain.setState(isShowing);
	return (Object) null;
    }
    */
    /*
    public int infeasible0(int[] arr) {
	System.out.println("null0");
	arr = new int[2];
	int i = arr.length;
	arr[3]=3;
	return arr[i];
    }
    */
    /*    public void test(LinkedList<Object> list) {
	for (Object o : list) {
	    o.toString();
	}
    }
    */
    /*
	public int infeasible1(Object o) {
	    System.out.println("infeasible1");
		if (o!=null) {
			return o.hashCode(); // INFEASIBLE
		} 
		System.err.println(o.toString() + " does not exist");
		return 2;
	}

	public void infeasible2(int [] arr) {
	    System.out.println("infeasible2");
		for (int i=0; i<=arr.length;i++) {
			arr[i]=i; // INFEASIBLE
		}
	}
	
	public void infeasible3(int a, int b) {
	    System.out.println("infeasible3");
		b=1; // ALL INFEASIBLE
		if (a>0) b--;
		b=1/b;
		if (a<=0) b=1/(1-b);
	}
	
	public boolean infeasible4(Object o) {
	    System.out.println("infeasible4");
		System.err.println(o.toString());
		if (o==null) {
			return false; // INFEASIBLE
		}
		return true;
	}
	
	public void infeasible5() {
	    System.out.println("infeasible5");
		String test="too long";
		if (test.length()==3) {
			System.err.println("unreachable"); // INFEASIBLE
		}
	}
	
	public int infeasible6(int[] arr) {
	    System.out.println("infeasible6");
		return arr[-1] + arr[arr.length]; // INFEASIBLE
	}
    */
}
