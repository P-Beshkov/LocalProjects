package performance;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class CollectionFillerRunnable<K, V> implements Runnable {

	private Map<K, V> collection;
	private List<Entry<K, V>> values;
	private BiFunction<V, V, V> operation;

	public CollectionFillerRunnable(Map<K, V> collection, List<Entry<K, V>> values, BiFunction<V, V, V> operation) {
		this.collection = collection;
		this.values = values;
		this.operation = operation;
	}

	@Override
	public void run() {
		if (collection instanceof ConcurrentHashMap) {
			performConcurentFill();
		} else {
			for (Entry<K, V> entry : values) {
				V value = collection.get(entry.getKey());
				if (value == null) {
					collection.put(entry.getKey(), entry.getValue());
				} else {
					collection.put(entry.getKey(), operation.apply(value, entry.getValue()));
				}
			}
		}
	}

	private void performConcurentFill() {
		ConcurrentHashMap<K, V> concurrentMap = (ConcurrentHashMap) collection;
		for (Entry<K, V> entry : values) {
			concurrentMap.merge(entry.getKey(), entry.getValue(), operation);
		}
	}
}
