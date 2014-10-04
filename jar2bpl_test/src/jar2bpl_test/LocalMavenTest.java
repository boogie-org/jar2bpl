package jar2bpl_test;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.joogie.Dispatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import bixie.Bixie;

@RunWith(Parameterized.class)
public class LocalMavenTest extends AbstractTest {
	
	@Parameterized.Parameters (name = "{index}: check ({1})")
	public static Collection<Object[]> data() {
		LinkedList<Object[]> filenames = new LinkedList<Object[]>();
		String dirname = System.getProperty("user.dir")+"/local_tests/maven.3.2.3";
		  File dir = new File(dirname);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {		    	
		    	if (!child.isDirectory() && child.getAbsoluteFile().getName().endsWith(".jar")) {
		    		int idx = child.getName().lastIndexOf('/');
		    		idx = (idx<0)?0:idx;
		    		String sn = child.getName().substring(idx);
		    		filenames.add(new Object[] {child.getAbsolutePath(), sn });		    		
		    	} else {
		    		//Ignore
		    	}
		    }
		  } else {			  
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
			  throw new RuntimeException(dirname + " NOT FOUND!");
		  }				  
	   return filenames;
   }
	
	
	public LocalMavenTest(String input, String shortname) {
		super(input, shortname);
	}


	@Test
	public void testJavaDirectory() {

		try {
			String javaFileDir = this.input;
						
			String cp = "";

			Dispatcher.setClassPath(javaFileDir+File.pathSeparatorChar+cp );
			
			Dispatcher.run(javaFileDir, bplFile);
			
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
//			fail("Translation Error " + e.toString());
		}
		
		Bixie bx = new Bixie();
		bixie.Options.v().stopTime = true;
		bx.run(bplFile, output_file);
		
		org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));
	}
	
}



