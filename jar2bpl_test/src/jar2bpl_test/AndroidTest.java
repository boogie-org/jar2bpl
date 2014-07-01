package jar2bpl_test;

import static org.junit.Assert.*;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;

public class AndroidTest {

	@Test
	public void test() {
		try {
			
			String javaFileDir = "regression/android_input/hoozdat.apk";
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
