package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

import bixie.Bixie;

public class JavaTranslationTest {
	
	@Test
	public void testJavaDirectory() {
		//TODO: design one test case for each sort of input to the translation.
		String javaFileDir = "regression/false_positives/fp08/";
		Options.v().setClasspath(javaFileDir);
		String bplFile = "test_tmp_boogiefiles/fp08.bpl";
		String output = "test_output/fp08.txt";

		try {
			Dispatcher.run(javaFileDir,
					bplFile);

		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bx.run(bplFile, output);
		
		org.junit.Assert.assertTrue(true);
	}

	
	
}



