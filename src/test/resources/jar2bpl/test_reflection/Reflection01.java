/**
 * 
 */
package jar2bpl.test_reflection;

import java.lang.reflect.Method;


/**
 * @author schaef
 * Reflection code that triggered an exception earlier.
 */
public class Reflection01 {

	public Object result;
	
	public int foo() {
		System.out.println("Hello");
		return 5;
	}
	
	public void reflectionTest() throws Throwable {	
		Class<?> noparams[] = {};
		
		Class<?> c = Class.forName("Snippet03");
		Object obj = c.newInstance();		
		Method method = c.getDeclaredMethod("foo", noparams);
		
		result = method.invoke(obj);		 
	}

		
}
