package jar2bpl_test;
/**
 * 
 */


import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.LinkedList;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import bixie.Bixie;

/**
 * @author schaef
 *
 */
@RunWith(Parameterized.class)
public class TestFalsePositives  {
	
	@Parameterized.Parameters (name = "{index}: check ({1})")
	public static Collection<Object[]> data() {
		LinkedList<Object[]> filenames = new LinkedList<Object[]>();
		String dirname = System.getProperty("user.dir")+"/regression/false_positives";
		  File dir = new File(dirname);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {		    	
		    	if (child.isDirectory() && child.getAbsolutePath()!=null) {
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
	
    private String input, shortname;
    
    public TestFalsePositives(String input, String shortname) {
        this.input = input;
        this.shortname = shortname;
    }

	
	@Test
	public void test() {
		String basename = this.shortname.replace('/', '_');
		
		String bplFile = "test_tmp_boogiefiles/"+basename+".bpl";
		String output_file = "test_output/"+basename+".txt"; 
		String gold_output = "test_gold/"+basename+".txt";
		
		System.out.println("Test: "+this.input);
		System.out.println("  output: "+bplFile);
		try {			
			String javaFileDir = this.input;
			Options.v().setClasspath(javaFileDir);
			

			Dispatcher.run(javaFileDir,
					bplFile);
		} catch (Exception e) {
			fail("Translation Error " + e.toString());
		}
		
		try {
			Bixie bx = new Bixie();
			bx.run(bplFile, output_file);

			org.junit.Assert.assertTrue(compareFiles(output_file, gold_output));
		} catch (Exception e) {
			fail("Check failed: " + e.toString());
		}
		
		org.junit.Assert.assertTrue(true);
		
	}

	protected boolean compareFiles(String outputfile, String goldfile) {
		File out = new File(outputfile); 
		File gold = new File(goldfile);
		try (FileReader fR1 = new FileReader(out);
				FileReader fR2 = new FileReader(gold);
				BufferedReader reader1 = new BufferedReader(fR1);
				BufferedReader reader2 = new BufferedReader(fR2);)
				{
			String line1, line2;
			while (true) // Continue while there are equal lines
			{
			    line1 = reader1.readLine();
			    line2 = reader2.readLine();

			    if (line1 == null) // End of file 1
			    {
			        return (line2 == null ? true : false); // Equal only if file 2 also ended
			    }

			    if (!line1.equalsIgnoreCase(line2)) // Different lines, or end of file 2
			    {
			        return false;
			    }
			}	
		} catch (Exception e) {
			return false;
		}
		
	}
	
}
