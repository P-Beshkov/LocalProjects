package performance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class TestSaveBinary {

	private static final String ACTUAL = "actual2.txt";
	private static final String ACTUAL_TRIMMED = "trimmedActual2.txt";
	private static final String EXPECTED = "expected2.txt";
	private static final String EXPECTED_TRIMMED = "trimmedExpected2.txt";

	static List<String> expected;
	static List<String> actual;

	public static void test() throws IOException {
		expected = FileUtils.readLines(new File(EXPECTED_TRIMMED));
		actual = FileUtils.readLines(new File(ACTUAL_TRIMMED));

		//		trimmFiles();
		compare();
	}

	private static void compare() {
		Set<String> actualSet = new HashSet<String>();
		Set<String> expectedSet = new HashSet<String>(expected);
		for (String link : actual) {
			if (actualSet.contains(link)) {
				System.out.println("Duplicate link: " + link);
			}
			actualSet.add(link);
		}
		for (String link : expectedSet) {
			actualSet.remove(link);
		}
		for (String link : actualSet) {
			System.out.println("Leftover link: " + link);
		}
	}

	private static void trimmFiles() throws IOException {
		File trimmedFile = new File(EXPECTED_TRIMMED);
		//				trimmedFile.createNewFile();
		List<String> trimmedLines = new ArrayList<String>();
		//		for (String lines : expected) {
		//			int indexOfHttp = lines.indexOf("http");
		//			trimmedLines.add(lines.substring(indexOfHttp));
		//		}
		//		FileUtils.writeLines(trimmedFile, trimmedLines);

		trimmedFile = new File(ACTUAL_TRIMMED);
		trimmedFile.createNewFile();
		trimmedLines = new ArrayList<String>();
		for (String lines : actual) {
			String line = lines.replace(", caller: function e(c) {", "");
			int indexOfHttp = lines.indexOf("http");
			line = line.substring(indexOfHttp);
			trimmedLines.add(line);
		}
		FileUtils.writeLines(trimmedFile, trimmedLines);
	}

}
