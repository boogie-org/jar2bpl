/**
 * 
 */
package jar2bpl_test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.joogie.Dispatcher;
import org.joogie.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.Log;

/**
 * @author schaef
 *
 */
@RunWith(Parameterized.class)
public class GithubTest {

	private static final File testDataFolder = new File("./test_data");

	private final List<File> classDirectories;

	/**
	 * Constructor for unit test that clones a particular revision (gitRef), of
	 * a repository (repositoryUrl), builds it (buildCommand), and then runs all
	 * classes in (classDirectories) through jar2bpl.
	 * 
	 * @param name
	 *            Short name for this test
	 * @param repositoryUrl
	 * @param gitRef
	 * @param buildCommand
	 * @param classDirectories
	 *            directories of the class files relative to the project root.
	 */
	public GithubTest(String name, String repositoryUrl, String gitRef,
			String buildCommand, String[] classDirectories) {
		File projectFolder = new File(testDataFolder + "/" + name);
		this.classDirectories = new LinkedList<File>();

		if (!testDataFolder.isDirectory() && !testDataFolder.mkdir()) {
			Log.error("Don't have permission to download test data.");
			return;
		}

		if (!projectFolder.isDirectory()) {
			if (!exec("git clone " + repositoryUrl + " "
					+ projectFolder.getAbsolutePath(), testDataFolder.getAbsoluteFile())) {
				return;
			}

			if (!exec("git checkout " + gitRef, projectFolder.getAbsoluteFile())) {
				return;
			}
		}
		boolean needsBuild = false;
		for (String classDirName : classDirectories) {
			File classDir = new File(projectFolder.getAbsolutePath() + "/"
					+ classDirName);
			this.classDirectories.add(classDir);
			if (!classDir.isDirectory()) {
				needsBuild = true;
			}
		}

		if (needsBuild) {	
			if (!exec(buildCommand, projectFolder.getAbsoluteFile())) {
				System.err.println("Failed to build " + name);
				System.err.println("If you are running the tests from eclipse this is normal,");
				System.err.println("because your PATH is not set. Run gradle test from command");
				System.err.println("line once. This will generate the test data and everything");
				System.err.println("should run smoothly after that.");
			}	
		}

	}

	private boolean exec(String command, File workingDirectory) {
		ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
		pb.directory(workingDirectory);

		try {
			Process p = pb.start();
			System.out.println("Result " + p.waitFor());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Test
	public void test() {
		for (File dir : this.classDirectories) {
			Options.v().setClasspath(dir.getAbsolutePath());
			Dispatcher.run(dir.getAbsolutePath());
		}
		
		// fail("Not yet implemented");
	}

	@Parameterized.Parameters(name = "{index}: check ({1})")
	public static Collection<Object[]> data() {
		List<Object[]> testData = new LinkedList<Object[]>();

		testData.add(new Object[] { "ant",
				"https://git-wip-us.apache.org/repos/asf/ant.git",
				"963d6c9a0af09690d6b37eb80175887961e7e1d6", "ant build",
				new String[] { "build/classes" } });

//		testData.add(new Object[] {
//				"cassandra",
//				"https://github.com/apache/cassandra",
//				"19c2d22e93c52bc4699a0b5a0fdcdeedd3826449",
//				"ant",
//				new String[] { "build/classes/main", "build/classes/stress",
//						"build/classes/thrift" } });

		return testData;
	}

}
