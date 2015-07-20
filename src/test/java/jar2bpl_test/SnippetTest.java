package jar2bpl_test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Abstract class for all test cases that detect inconsistent code.
 * 
 * @author schaef
 * 
 */
@RunWith(Parameterized.class)
public class SnippetTest {

	protected static final String userDir = System.getProperty("user.dir")
			+ "/";
	protected static final String testRoot = userDir + "src/test/resources/jar2bpl/";

	private final File sourceFile;
	private final String name;
//	private File goldenFile;

	@Parameterized.Parameters(name = "{index}: check ({1})")
	public static Collection<Object[]> data() {
		List<Object[]> filenames = new LinkedList<Object[]>();
		final File source_dir = new File(testRoot + "test_snippets/");
		File[] directoryListing = source_dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isFile() && child.getName().endsWith(".java")) {
					filenames.add(new Object[] { child, child.getName() });
				}
			}
		} else {
			throw new RuntimeException("Test data not found!");
		}
		return filenames;
	}

	public SnippetTest(File source, String name) {
		this.sourceFile = source;
		this.name = name;
//		this.goldenFile = new File(source.getAbsolutePath().replace(".java",
//				".gold"));
	}

	@Test
	public void testDefaultConfiguration() {
		runTranslation();
	}

	@Test
	public void test01() {		
		Options o = Options.v();
		
		String name = "jar2bpl.test_snippets." + this.name.replace(".java", "");
		System.out.println("Using main class: " + name);
		o.setMainClassName(name);
		o.setDebug(true);
		o.setExceptionErrorModel(true);
		o.setRuntimeExceptionReturns(true);
		o.setRunTypeChecker(true);
		o.setSoundThreads(true);
		runTranslation();
	}


	
	public void runTranslation() {
		System.out.println("Running test: " + sourceFile.getName());
		File classFileDir = null;
		File outFile = null;

		try {
			outFile = File.createTempFile("jar2bpl_test", ".bpl");
			classFileDir = compileJavaFile(this.sourceFile);
			if (classFileDir == null || !classFileDir.isDirectory()) {
				assertTrue(false);
			}

			Options.v().setClasspath(classFileDir.getAbsolutePath());
			Dispatcher.run(classFileDir.getAbsolutePath(), outFile.getAbsolutePath());

		} catch (Exception e) {
			fail("Translation Error " + e.toString());

		} finally {
			if (classFileDir != null) {
				try {
					delete(classFileDir);
					if (outFile != null && outFile.isFile()) {
						if (!outFile.delete()) {
							System.err.println("Failed to delete file");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		org.junit.Assert.assertTrue(true);
	}
	
	@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "DM_DEFAULT_ENCODING")
	protected String fileToString(File f) {
		StringBuffer sb = new StringBuffer();
		try (FileReader fileRead = new FileReader(f);
				BufferedReader reader = new BufferedReader(fileRead);) {
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null)
					break;
				sb.append(line);
				sb.append("\n");
			}
		} catch (Throwable e) {

		}
		return sb.toString();
	}

	@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "DM_DEFAULT_ENCODING")
	protected boolean compareFiles(File out, File gold) {
		try (FileReader fR1 = new FileReader(out);
				FileReader fR2 = new FileReader(gold);
				BufferedReader reader1 = new BufferedReader(fR1);
				BufferedReader reader2 = new BufferedReader(fR2);) {
			String line1, line2;
			while (true) // Continue while there are equal lines
			{
				line1 = reader1.readLine();
				line2 = reader2.readLine();

				// End of file 1
				if (line1 == null) {
					// Equal only if file 2 also ended
					return (line2 == null ? true : false);
				}

				// Different lines, or end of file 2
				if (!line1.equalsIgnoreCase(line2)) {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected File compileJavaFile(File sourceFile) throws IOException {
		final File tempDir = getTempDir();
		final String javac_command = String.format("javac -g %s -d %s",
				sourceFile.getAbsolutePath(), tempDir.getAbsolutePath());

		ProcessBuilder pb = new ProcessBuilder(javac_command.split(" "));
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);
		Process p = pb.start();

		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		return tempDir;
	}

	protected File getTempDir() throws IOException {
		final File tempDir = File.createTempFile("jar2bpl_test_temp",
				Long.toString(System.nanoTime()));
		if (!(tempDir.delete())) {
			throw new IOException("Could not delete temp file: "
					+ tempDir.getAbsolutePath());
		}
		if (!(tempDir.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ tempDir.getAbsolutePath());
		}
		return tempDir;
	}

	protected void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete()) {
			throw new IOException("Failed to delete file: " + f);
		}
	}

	@AfterClass
	public static void tearDown() {
		org.joogie.GlobalsCache.resetInstance();
	}

}
