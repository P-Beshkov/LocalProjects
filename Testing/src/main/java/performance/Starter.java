package performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Starter {

	public static final int THREADS_COUNT = 8;

	public static void main(String[] args) throws Exception {
		//		new TestSamba().test();
		//		System.out.println("jcif responseTimeout: " + Config.getLong("jcifs.smb.client.responseTimeout", -1));
		//		Config.setProperty("jcifs.smb.client.responseTimeout", "10000");
		//		Config.setProperty("jcifs.smb.client.soTimeout", "10000");
		//		System.out.println("jcif responseTimeout: " + Config.getLong("jcifs.smb.client.responseTimeout", -1));
		//		System.out.println("jcif so timeout: " + Config.getLong("jcifs.smb.client.soTimeout", -1));

		//		int a = '•';
		//		char b = 8226;
		//		System.out.println(a);
		File zipLocal = new File("Distributor2.zip");
		File tempDir = new File("tmp");
		System.out.println(zipLocal.exists() + " " + tempDir.exists());
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
}
