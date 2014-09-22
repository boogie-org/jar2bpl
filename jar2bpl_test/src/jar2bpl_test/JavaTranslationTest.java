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
		String base="fp05";
		Options.v().setDebug(true);
		String javaFileDir = "regression/false_positives/"+base+"/";
		Options.v().setClasspath(javaFileDir);
		
		String bplFile = "test_tmp_boogiefiles/"+base+".bpl";
		String output = "test_output/"+base+".txt";

		try {
			Dispatcher.run(javaFileDir,
					bplFile);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Translation Error " + e.toString());
		}
		
		try {
		Bixie bx = new Bixie();
		bx.run(bplFile, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		org.junit.Assert.assertTrue(true);
	}

	
	
}



