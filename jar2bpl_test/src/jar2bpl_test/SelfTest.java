package jar2bpl_test;

import static org.junit.Assert.fail;

import java.io.File;

import org.joogie.Dispatcher;
import org.junit.Test;

import bixie.Bixie;

public class SelfTest extends AbstractTest {

	public SelfTest() {
		super("self", "self");
		// TODO Auto-generated constructor stub
	}

	protected String workspaceDir = System.getProperty("user.dir")+"/../../";	
	
	@Test
	public void testJar2bpl() {
		this.updateNames("jar2bpl", "jar2bpl");
		String projectDir = "jar2bpl/jar2bpl/";
		try {
			String javaFileDir = workspaceDir+projectDir+"src";
			System.err.println(javaFileDir);
			String cp = "";
			for (File f : new File(workspaceDir+projectDir+"lib").listFiles()) {
				if (f.getName().endsWith(".jar")) {
					cp += File.pathSeparatorChar+ f.getAbsolutePath();
				}
			}
			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			Dispatcher.run(javaFileDir, bplFile);
		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));
	}

	@Test
	public void testGravy() {
		this.updateNames("gravy", "gravy");
		String projectDir = "gravy/gravy/";
		try {
			String javaFileDir = workspaceDir+projectDir+"src";
			String cp = "";
			for (File f : new File(workspaceDir+projectDir+"lib").listFiles()) {
				if (f.getName().endsWith(".jar")) {
					cp += File.pathSeparatorChar+ f.getAbsolutePath();
				}
			}
			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			Dispatcher.run(javaFileDir, bplFile);
		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));
	}
	

	@Test
	public void testBoogieamp() {
		this.updateNames("boogieamp", "boogieamp");
		String projectDir = "boogieamp/boogieamp/";
		try {
			String javaFileDir = workspaceDir+projectDir+"src";
			String cp = "";
			for (File f : new File(workspaceDir+projectDir+"lib").listFiles()) {
				if (f.getName().endsWith(".jar")) {
					cp += File.pathSeparatorChar+ f.getAbsolutePath();
				}
			}
			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			Dispatcher.run(javaFileDir, bplFile);
		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));
	}
	
}
