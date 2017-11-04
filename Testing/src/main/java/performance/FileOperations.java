package performance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperations {

	public static void main(String[] args) throws IOException {
		String testFolderPath = "testCountFiles";
		String testFolderPath2 = "testCountFiles2";
		//createFiles(testFolderPath2);
		long count;

		long start = System.currentTimeMillis();
		count = Files.list(Paths.get(testFolderPath2)).count();
		long end = System.currentTimeMillis();		
		System.out.println("Time elapsed: " + (end - start) + ", count: " + count);

		count = 0;

		start = System.currentTimeMillis();
		count = new File(testFolderPath).list().length;
		end = System.currentTimeMillis();
		System.out.println("Time elapsed: " + (end - start) + ", count: " + count);
	}

	/**
	 * @param testFolderPath
	 * @throws IOException
	 */
	private static void createFiles(String testFolderPath) throws IOException {
		File testFolder = new File(testFolderPath);
		testFolder.mkdir();
		int filesCount = 2000;
		for (int i = 0; i < filesCount; i++) {
			Path path = Files.createFile(Paths.get(testFolderPath + "/testFile - " + i));
			Files.write(path, "dummy test data".getBytes());
		}
	}


}
