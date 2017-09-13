package performance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Starter {

	private static final int ENTITIES_COUNT = 10000;
	private static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws Exception {

		new Starter().getDocument();
		//		NicoVideo.downloadNicoOld();
		//		Map<String, Integer> collection = new ConcurrentHashMap<>(ENTITIES_COUNT * THREADS_COUNT);
		//
		//		List<Entry<String, Integer>> values = new ArrayList<>();
		//		populateValues(values);
		//		BiFunction<Integer, Integer, Integer> operation = (input1, input2) -> {
		//			return input1 + input2;
		//		};
		//		List<Thread> threads = new ArrayList<>();
		//		for (int i = 0; i < THREADS_COUNT; i++) {
		//			threads.add(new Thread(new CollectionFiller<>(collection, values, operation)));
		//		}
		//		long start = System.currentTimeMillis();
		//		for (Thread thread : threads) {
		//			thread.start();
		//		}
		//		for (Thread thread : threads) {
		//			thread.join();
		//		}
		//		long end = System.currentTimeMillis();
		//		int entitesCountActual = 0;
		//		for (Entry<String, Integer> entry : collection.entrySet()) {
		//			System.out.println(entry.getKey() + " - " + entry.getValue());
		//			entitesCountActual += entry.getValue();
		//		}
		//		int entitiesCountExpected = ENTITIES_COUNT * THREADS_COUNT;
		//		System.out.println("Entites lost = " + (entitiesCountExpected - entitesCountActual)
		//				/ (double) entitiesCountExpected + "%.");
		//		System.out.println("Time elapsed: " + (end - start));
	}

	public void getDocument() {

		Entry root = new Entry();

		String[] recordWords = new String[] { "This is simple text for testing",
				"This might be simple cool",
				"Spring Security provides comprehensive security services for Java EE­based",
				"enterprise software applications. People use Spring Security for many",
				//				"reasons, but most are drawn to the project after finding the security features",
				//				"of Java EE’s Servlet Specification or EJB Specification lack the depth",
				//				"required for typical enterprise application scenarios. Whilst mentioning these",
				//				"standards, it’s important to recognise that they are not portable at a WAR or",
				//				"EAR level. Therefore, if you switch server environments, it is typically a lot of",
				//				"work to reconfigure your application’s security in the new target environment.",
				//				"Using Spring Security overcomes these problems, and also brings you",
				//				"dozens of other useful, customisable security features"
		};
		int id = 1;
		for (String recordWord : recordWords) {
			addRecord(root, new Record(recordWord, "" + id));
			id++;
		}

		String[] searchWords = new String[] { "simple", "cool", "useful", "are", "dozenss", "dozens" };
		for (String searchWord : searchWords) {
			Entry current = root;
			for (int i = 0; i < searchWord.length() && current != null; i++) {
				char currentKey = searchWord.charAt(i);
				current = current.searchNode(currentKey);
			}
			if (current == null) {
				System.out.println("Word [" + searchWord + "] not found");
			} else {
				System.out.println("Word [" + searchWord + "], id[" + current.ids + "]");
			}
		}
		System.out.println("Object size: " + ObjectSizeFetcher.getObjectSize(root));
		System.out.println(root);
	}

	public void addRecord(Entry root, Record record) {
		String[] words = record.body.split("\\W+");
		for (String word : words) {
			Entry current = root;
			for (int i = 0; i < word.length(); i++) {
				char key = word.charAt(i);
				Entry node = current.getNode(key);
				node.addRecord(record.id);
				current = node;
			}
		}
	}

	private class Entry {

		@Override
		public String toString() {
			return "Entry [key=" + key + ", nodes=" + nodes + ", records=" + ids + "]";
		}

		public Entry searchNode(char currentKey) {
			return nodes.get(currentKey);
		}

		public char key;
		public Map<Character, Entry> nodes;
		public Set<String> ids;

		public Entry() {
			nodes = new HashMap<>();
			ids = new HashSet<>();
		}

		public Entry getNode(char key) {
			Entry entry = nodes.get(key);
			if (entry == null) {
				entry = new Entry();
				entry.key = key;
				nodes.put(key, entry);
			}
			return entry;
		}

		public void addRecord(String id) {
			ids.add(id);
		}
	}

	private class Record {
		public Record(String recordWord, String id) {
			this.body = recordWord;
			this.id = id;
		}

		public String id;
		public String body;
	}

	//	private static void populateValues(List<Entry<String, Integer>> values) {
	//		Random random = new Random();
	//		for (int i = 0; i < ENTITIES_COUNT; i++) {
	//			values.add(new SimpleEntry<String, Integer>(String.valueOf(random.nextInt(23)), 1));
	//		}
	//	}

}
