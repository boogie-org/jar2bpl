package jar2bpl_test;

import static org.junit.Assert.fail;

import java.io.File;

import org.joogie.Dispatcher;
import org.junit.Test;

import bixie.Bixie;

public class LocalJMeterTest extends AbstractTest {
	
	public LocalJMeterTest() {
		super("jmeter", "jmeter");
		// TODO Auto-generated constructor stub
	}

	private void runModule(String suffix) {
		this.updateNames("jmeter-"+suffix, "jmeter-"+suffix);
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/jmeter/"+suffix;
			String cp = "";

			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			
			Dispatcher.run(javaFileDir, bplFile);
			
		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bixie.Options.v().stopTime = true;
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));		
	}

	@Test
	public void testFunctions() {
		runModule("functions");
	}	
		
	@Test
	public void testReports() {
		runModule("reports");
	}	
	
	@Test
	public void testComponents() {
		runModule("components");
	}
	
	
	@Test
	public void testJorphan() {
		runModule("jorphan");
	}

	@Test
	public void testCore() {
		runModule("core");
	}

	
	
}



