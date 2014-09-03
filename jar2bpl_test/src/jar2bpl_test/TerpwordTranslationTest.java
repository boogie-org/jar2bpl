package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

import bixie.Bixie;

public class TerpwordTranslationTest {

	
	@Test
	public void testJarInputFile() {
		//TODO: design one test case for each sort of input to the translation.
		String bplFile = "regression/test_output/terpword.bpl";
		
		try {
			String javaFileDir = "regression/terpword/src";
			Options.v().setClasspath(javaFileDir);			
			//Options.v().setSoundThreads(true);
			

			Dispatcher.run(javaFileDir,
					bplFile);
			
		} catch (Exception e) {			
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bx.run(bplFile, "regression/test_output/terpword.txt");

		
		org.junit.Assert.assertTrue(true);
	}
	

	
	
}



