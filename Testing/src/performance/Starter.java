package performance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class Starter {

	private static final int ENTITIES_COUNT = 10000;
	private static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws InterruptedException {
		Map<String, Integer> collection = new ConcurrentHashMap<>(ENTITIES_COUNT * THREADS_COUNT);
		List<Entry<String, Integer>> values = new ArrayList<>();
		populateValues(values);
		BiFunction<Integer, Integer, Integer> operation = (input1, input2) -> {
			return input1 + input2;
		};
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < THREADS_COUNT; i++) {
			threads.add(new Thread(new CollectionFiller<>(collection, values, operation)));
		}
		long start = System.currentTimeMillis();
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		long end = System.currentTimeMillis();
		int entitesCountActual = 0;
		for (Entry<String, Integer> entry : collection.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
			entitesCountActual += entry.getValue();
		}
		int entitiesCountExpected = ENTITIES_COUNT * THREADS_COUNT;
		System.out.println("Entites lost = " + (entitiesCountExpected - entitesCountActual)
				/ (double) entitiesCountExpected + "%.");
		System.out.println("Time elapsed: " + (end - start));
	}

	private static void populateValues(List<Entry<String, Integer>> values) {
		Random random = new Random();
		for (int i = 0; i < ENTITIES_COUNT; i++) {
			values.add(new SimpleEntry<String, Integer>(String.valueOf(random.nextInt(23)), 1));
		}
	}

}
