

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class RemoveLines {

	public static void test() throws IOException {
		String text = FileUtils.readFileToString(new File("emptyLines.txt"));
		System.out.println("Raw input: " + text);
		String adjusted = text.replaceAll("(?m)^[ \t]*\r?\n", "");
		System.out.println("Adjusted: " + adjusted);

	}
}
