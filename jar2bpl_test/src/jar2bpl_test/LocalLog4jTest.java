package jar2bpl_test;

import static org.junit.Assert.fail;

import java.io.File;

import org.joogie.Dispatcher;
import org.junit.Test;

import bixie.Bixie;

public class LocalLog4jTest extends AbstractTest {
	
	public LocalLog4jTest() {
		super("log4j", "log4j");
		// TODO Auto-generated constructor stub
	}


	@Test
	public void testCore() {
		this.updateNames("log4j-core", "log4j-core");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/log4j/log4j-core/target/classes";
			String cp = "";
//			for (File f : new File("/Users/schaef/Documents/tmp/ant/build/lib").listFiles()) {
//				if (f.getName().endsWith(".jar")) {
//					cp += File.pathSeparatorChar+ f.getAbsolutePath();
//				}
//			}
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
	public void testJavaDirectory() {
		this.updateNames("log4j-slf4j-impl", "log4j-slf4j-impl");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/log4j/log4j-slf4j-impl/target/classes";
			String cp = "";
//			for (File f : new File("/Users/schaef/Documents/tmp/ant/build/lib").listFiles()) {
//				if (f.getName().endsWith(".jar")) {
//					cp += File.pathSeparatorChar+ f.getAbsolutePath();
//				}
//			}
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
			
}



