package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

public class TerpwordTranslationTest {

	
	@Test
	public void testJarInputFile() {
		//TODO: design one test case for each sort of input to the translation.
		try {
			String javaFileDir = "regression/terpword/src";
			Options.v().setClasspath(javaFileDir);
			//Options.v().setSoundThreads(true);
			String bplFile = "regression/test_output/terpword.bpl";
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



