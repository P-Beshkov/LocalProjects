package performance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.apache.commons.lang3.time.StopWatch;

public class CollectionsNestedTest {
	public int entitiesCount;
	Map<Integer, Map<String, Integer>> collection;
	List<Entry<String, Integer>> values;
	List<Thread> threads;
	StopWatch watch;

	public CollectionsNestedTest(int entitiesCount) {
		this.entitiesCount = entitiesCount;
	}

	private void init() {
		collection = new ConcurrentHashMap<>();
		values = new ArrayList<>();
		threads = new ArrayList<>();
		watch = new StopWatch();
	}

	public void start(int warmUpCycles, int testCycles) throws InterruptedException {
		init();
		populateValues(values);

		BiFunction<Integer, Integer, Integer> operation = (input1, input2) -> {
			return input1 + input2;
		};
		watch.start();
		for (int cycle = 0; cycle < warmUpCycles; cycle++) {
			execute(operation);
		}
		watch.reset();
		watch.start();
		for (int i = 0; i < testCycles; i++) {
			execute(operation);
		}
		watch.stop();
		float executionTime = ((float) (watch.getTime())) / (float) testCycles;
		//		int entitesCountActual = 0;
		//		for (Entry<String, Integer> entry : collection.entrySet()) {
		//			System.out.println(entry.getKey() + " - " + entry.getValue());
		//			entitesCountActual += entry.getValue();
		//		}
		//		int entitiesCountExpected = ENTITIES_COUNT * Starter.THREADS_COUNT;
		//		System.out.println("Entites lost = " + (entitiesCountExpected - entitesCountActual)
		//				/ (double) entitiesCountExpected + "%.");
		System.out.println("Execution time: " + executionTime);
	}

	private void execute(BiFunction<Integer, Integer, Integer> operation) throws InterruptedException {
		watch.suspend();
		threads.clear();
		collection.clear();
		for (int i = 0; i < Starter.THREADS_COUNT; i++) {
			threads.add(new Thread(new NestedCollectionFillerRunnable(collection, values, operation)));
		}
		watch.resume();
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
	}

	private void populateValues(List<Entry<String, Integer>> values) {
		Random random = new Random();
		for (int i = 0; i < entitiesCount; i++) {
			values.add(new SimpleEntry<String, Integer>(String.valueOf(random.nextInt(23)), 1));
		}
	}
}
