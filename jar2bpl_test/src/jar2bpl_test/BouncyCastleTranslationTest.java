package jar2bpl_test;

import static org.junit.Assert.fail;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

public class BouncyCastleTranslationTest {

	
	@Test
	public void testJarInputFile() {
		//TODO: design one test case for each sort of input to the translation.
		try {
			String javaFileDir = System.getProperty("user.dir")+"/regression/bc/java";
			Options.v().setClasspath(javaFileDir);			
			//Options.v().setSoundThreads(true);
			String bplFile = "regression/test_output/bc.bpl";

			Dispatcher.run(javaFileDir,
					bplFile);
			
		} catch (Exception e) {			
			fail("Translation Error " + e.toString());
		}
		
		org.junit.Assert.assertTrue(true);
	}
	

	
	
}



