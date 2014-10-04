package jar2bpl_test;

import static org.junit.Assert.fail;

import java.io.File;

import org.joogie.Dispatcher;
import org.junit.Test;

import bixie.Bixie;

public class LocalCrunchTest extends AbstractTest {
	
	public LocalCrunchTest() {
		super("crunch", "crunch");
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testSpark() {
		super.updateNames("crunch-spark", "crunch-spark");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/crunch/crunch-spark/target/classes";
			String cp = "";
//			for (File f : new File(System.getProperty("user.dir")+"/local_tests/cassandra/build/lib").listFiles()) {
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
	public void testHive() {
		super.updateNames("crunch-hive", "crunch-hive");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/crunch/crunch-hive/target/classes";
			String cp = "";
//			for (File f : new File(System.getProperty("user.dir")+"/local_tests/cassandra/build/lib").listFiles()) {
//				if (f.getName().endsWith(".jar")) {
//					cp += File.pathSeparatorChar+ f.getAbsolutePath();
//				}
//			}
			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			Dispatcher.run(javaFileDir, bplFile);
		} catch (Exception e) {
			e.printStackTrace();
			//fail("Translation Error " + e.toString());
		}
		Bixie bx = new Bixie();
		bixie.Options.v().stopTime = true;
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));		
	}
	
	
	@Test
	public void testHBase() {
		super.updateNames("crunch-hbase", "crunch-hbase");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/crunch/crunch-hbase/target/classes";
			String cp = "";
//			for (File f : new File(System.getProperty("user.dir")+"/local_tests/cassandra/build/lib").listFiles()) {
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
	public void testCore() {
		super.updateNames("crunch-core", "crunch-core");
		try {
			String javaFileDir = System.getProperty("user.dir")+"/local_tests/crunch/crunch-core/target/classes";
			String cp = "";
//			for (File f : new File(System.getProperty("user.dir")+"/local_tests/cassandra/build/lib").listFiles()) {
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



