package jar2bpl_test;
/**
 * 
 */


import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author schaef
 *
 */
@RunWith(Parameterized.class)
public class TestCornerCases extends AbstractTest {
	
	@Parameterized.Parameters (name = "{index}: check ({1})")
	public static Collection<Object[]> data() {
		LinkedList<Object[]> filenames = new LinkedList<Object[]>();
		String dirname = System.getProperty("user.dir")+"/regression/corner_cases";
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
    
    public TestCornerCases(String input, String shortname) {
		super(input, shortname);
	}

	@Test
	public void test() {
		try {
			org.junit.Assert.assertTrue(this.parseInputProgram(input, input));
		} catch (Exception e) {
			fail("Check failed: " + e.toString());
		}

	}
	
}
