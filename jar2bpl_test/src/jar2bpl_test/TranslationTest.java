package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

public class TranslationTest {

	
//	@Test
//	public void testJarInputFile() {
//		//TODO: design one test case for each sort of input to the translation.
//		try {
//			String javaFileDir = "regression/jar_input/args4j-2.0.18.jar";
//			String bplFile = "regression/test_output/args4j.bpl";
//			// create dispatcher
//			Dispatcher dispatcher = new Dispatcher(javaFileDir,
//					bplFile);
//
//			// run dispatcher
//			dispatcher.run();
//
//		} catch (Exception e) {			
//			fail("Translation Error " + e.toString());
//		}
//		
//		org.junit.Assert.assertTrue(true);
//	}
	
	
	@Test
	public void testJavaDirectory() {
		//TODO: design one test case for each sort of input to the translation.
		try {
			
			String javaFileDir = "regression/java_input/";
			Options.v().setClasspath(javaFileDir);
			String bplFile = "regression/test_output/NullPointerExceptions.bpl";
			// create dispatcher
			Dispatcher dispatcher = new Dispatcher(javaFileDir,
					bplFile);

			// run dispatcher
			dispatcher.run();

		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		
		org.junit.Assert.assertTrue(true);
	}

	
	
}



