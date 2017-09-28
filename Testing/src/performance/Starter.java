package performance;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import samba.TestSamba;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
>>>>>>> 745a03311c82745299c713790eb21ccbb80b36d9

@SuppressWarnings("unused")
public class Starter {

<<<<<<< HEAD
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

=======
	public static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws Exception {
		new TestSamba().test();
		//		System.out.println("jcif responseTimeout: " + Config.getLong("jcifs.smb.client.responseTimeout", -1));
		//		Config.setProperty("jcifs.smb.client.responseTimeout", "10000");
		//		Config.setProperty("jcifs.smb.client.soTimeout", "10000");
		//		System.out.println("jcif responseTimeout: " + Config.getLong("jcifs.smb.client.responseTimeout", -1));
		//		System.out.println("jcif so timeout: " + Config.getLong("jcifs.smb.client.soTimeout", -1));

		//		int a = '•';
		//		char b = 8226;
		//		System.out.println(a);
		//		File zipLocal = new File("UnzipIssue.zip");
		//		File tempDir = new File("tmp");
		//		System.out.println(zipLocal.exists() + " " + tempDir.exists());
		//		List<File> unzip = ZipUtils.unzip(zipLocal.getAbsolutePath(), tempDir.getAbsolutePath());
		//		DownloadTest.test();
		//		new TestSamba().test();
		//				TestRemoveLines.test();
		//		TestSaveBinary.test();

	}

	public void connect() throws Exception {
		JSch jsch = new JSch();
		String decodedUsername =
				//				"Hercules";
				"sftp_user"; // GeneralUtil.decodeBase64(sftp_userName);
		String decodedPassword =
				//				"Rel7.xPass!";
				"Rel7.xPass!"; // GeneralUtil.decodeBase64(sftp_password);
		Session session = jsch.getSession(decodedUsername, "10.164.18.96", 22);
		session.setPassword(decodedPassword);
		java.util.Properties configJsch = new java.util.Properties();
		configJsch.put("StrictHostKeyChecking", "no");
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.setConfig(configJsch);
		session.connect();
		/*
		 * channel can be open as 'exec' if you need to execute commands
		 * (like run an antivirus) check Jcraft documentation for more
		 * details.
		 */
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		//		channelSftp.cd("/");
		System.out.println(channelSftp.ls("/statuses/*.xml"));
		channelSftp.cd("statuses\\");
		Collection<String> list = channelSftp.ls("*.xml");

	}

	//	Worker worker = new Worker();
	//	Thread thread1 = new Thread(worker);
	//	thread1.start();
	//	Thread.sleep(1000);
	//	worker.endWork();
	//	thread1.join();

	private static List<Map<String, String>> proceessResponse(InputStream is) throws IOException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		HashMap<String, String> record;
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line = rd.readLine();
		String[] columns = line.split(",");
		while ((line = rd.readLine()) != null) {
			String[] values = line.split(",");
			record = new HashMap<String, String>();
			for (int i = 0; i < values.length && i < columns.length; i++) {
				record.put(columns[i], values[i]);
			}
			result.add(record);
		}
		return result;
	}

	//	CollectionsTest collectionsTest = new CollectionsTest(100000);
	//		collectionsTest.start(50, 20);
	//	String username = "guy.plotnik@verint.com";
	//	String password = "g0P4l0O8t5";
	//	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	//	credentialsProvider.setCredentials(AuthScope.ANY,
	//			new UsernamePasswordCredentials(username, password));
	//	String restUrl = //"http://localhost:8080/FocalCollectRest/entities/getInputEntity";
	//	"http://verint.tryliferaft.com/api/v1/people/posts?channel=twitter&username=jack";
	//	HttpClientBuilder clientBuilder = HttpClientBuilder.create()
	//			.setTargetAuthenticationStrategy(new TargetAuthenticationStrategy())
	//			.setDefaultCredentialsProvider(credentialsProvider);
	//	try (CloseableHttpClient client = clientBuilder.build()) {
	//		//		client.setParams(clientParams);
	//		HttpGet request = new HttpGet(restUrl);
	//		//			StringEntity entity = new StringEntity("{paramName:'SAMBA_DEBUG_SUBDIR'}");
	//		//			entity.setContentType("application/json");
	//		//			request.setEntity(entity);
	//		request.setHeader("Authorization", "Basic Z3V5LnBsb3RuaWtAdmVyaW50LmNvbTpnMFA0bDBPOHQ1");
	//		try (CloseableHttpResponse response = client.execute(request)) {
	//			System.out.println(response);
	//			List<Map<String, String>> records = proceessResponse(response.getEntity().getContent());
	//			records.forEach(record -> System.out.println(record));
	//		}
	//	}
>>>>>>> 745a03311c82745299c713790eb21ccbb80b36d9
}
