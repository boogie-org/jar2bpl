package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

public class JavaTranslationTest {
	
	@Test
	public void testJavaDirectory() {
		//TODO: design one test case for each sort of input to the translation.
		try {
			
			String javaFileDir = "regression/java_input/";
			Options.v().setClasspath(javaFileDir);
			String bplFile = "regression/test_output/java_input.bpl";
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



