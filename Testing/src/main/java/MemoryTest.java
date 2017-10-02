import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryTest {

	public List<Map<String, Object>> operation() {
		long startFreeMemory = printMemory();

		List<Map<String, Object>> ssEntities = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			Map<String, Object> ssEntitiy = new HashMap<>();
			ssEntitiy.put("url", "http://www.abv.bg" + i);
			ssEntitiy.put("parent_externalId", "gosho");
			ssEntitiy.put("parent_objectType", i);
			ssEntities.add(ssEntitiy);
		}

		long endFreeMemory = printMemory();
		System.out.println("Memory consumed: " + (startFreeMemory - endFreeMemory) / 1024 + " KBs");

		return ssEntities;
	}

	private long printMemory() {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");

		System.out.println(sb.toString());
		return freeMemory;
	}

	private class EntityHolder {
		public String url;
		public Map<String, Object> entities;
	}

}
