package jar2bpl_test;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.joogie.Dispatcher;
import org.joogie.Options;

import bixie.Bixie;

/**
 * @author schaef
 * 
 */

public abstract class AbstractTest {

	protected String input, shortname;

	protected String bplFile, output_file, gold_output;

	public AbstractTest(String input, String shortname) {
			updateNames(input, shortname);
	}

	protected void updateNames(String input, String shortname) {
		this.input = input;
		this.shortname = shortname;
		String basename = this.shortname.replace('/', '_');
		bplFile = "test_tmp_boogiefiles/" + basename + ".bpl";
		output_file = "test_output/" + basename + ".txt";
		gold_output = "test_gold/" + basename + ".txt";

	}
	
	protected boolean parseInputProgram(String dir, String cp) throws Exception {
		System.out.println("Test: " + dir);
		System.out.println("  output: " + bplFile);
		try {
			Options.v().setClasspath(cp);
			Dispatcher.run(dir, bplFile);
		} catch (Exception e) {
			throw e;
		}
		
		try {
			Bixie bx = new Bixie();
			bx.run(bplFile, output_file);
			return compareFiles(output_file, gold_output);
		} catch (Exception e) {
			throw e;
		}		
	}

	protected boolean compareFiles(String outputfile, String goldfile) {
		File out = new File(outputfile);
		File gold = new File(goldfile);
		try (FileReader fR1 = new FileReader(out);
				FileReader fR2 = new FileReader(gold);
				BufferedReader reader1 = new BufferedReader(fR1);
				BufferedReader reader2 = new BufferedReader(fR2);) {
			String line1, line2;
			while (true) // Continue while there are equal lines
			{
				line1 = reader1.readLine();
				line2 = reader2.readLine();

				if (line1 == null) // End of file 1
				{
					return (line2 == null ? true : false); // Equal only if file
															// 2 also ended
				}

				if (!line1.equalsIgnoreCase(line2)) // Different lines, or end
													// of file 2
				{
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

	}

}
