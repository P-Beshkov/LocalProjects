package performance;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class Collections {

	public static void main(String[] args) {
		HashMap<String, Integer> occurences = new HashMap<>();
		int itemTypes = 23;
		for (int i = 0; i < itemTypes; i++) {
			occurences.put(String.valueOf(i), 0);
		}

		int entitiesCount = 1000000;
		List<String> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < entitiesCount; i++) {
			entities.add(String.valueOf(random.nextInt(itemTypes)));
		}
		int repeatCount = 100;
		int[] occurencesArr = new int[23];
		long start = System.currentTimeMillis();
		for (int i = 0; i < repeatCount; i++) {
			insertIntoCollection(entities, occurences);
		}

		long end = System.currentTimeMillis();
		System.out.println("Collection based, took: " + (end - start));
		start = System.currentTimeMillis();
		for (int i = 0; i < repeatCount; i++) {
			insertIntoArray(entities, occurencesArr);
		}
		end = System.currentTimeMillis();
		System.out.println("Array based, took: " + (end - start));

		for (Entry<String, Integer> i : occurences.entrySet()) {
			System.out.println(i.getKey() + " - " + i.getValue());
		}
		for (int i = 0; i < occurencesArr.length; i++) {
			System.out.println(i + " - " + occurencesArr[i]);
		}
	}

	private static void insertIntoArray(List<String> entities, int[] occurencesArr) {
		for (String integer : entities) {
			occurencesArr[Integer.valueOf(integer)]++;
		}
	}

	private static void insertIntoCollection(List<String> entities, HashMap<String, Integer> occurences) {
		for (String integer : entities) {
			Integer count = occurences.get(integer);
			occurences.put(integer, count + 1);
		}
	}
}
