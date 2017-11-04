package scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

public class ScriptingTest {

	public static String extensionScript;
	public static ScriptEngine engine;
	public static Invocable invocable;
	private static String customActionScript;
	private static String paramsAll;
	int DefaultentitiesCount = 10000;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		invocable = (Invocable) engine;
		String scriptPath = "WebAlertScript.js";
		byte[] encoded = Files.readAllBytes(Paths.get(scriptPath));
		extensionScript = new String(encoded);

		scriptPath = "CustomAction.js";
		encoded = Files.readAllBytes(Paths.get(scriptPath));
		customActionScript = new String(encoded);

		scriptPath = "ConfigParams.txt";
		encoded = Files.readAllBytes(Paths.get(scriptPath));
		paramsAll = new String(encoded);
	}

	@Test
	public void runDemo() throws Exception {
		executeScript();
	}

	public void executeScript() throws Exception {
		Executor executor = new Executor();
		engine.eval(extensionScript);
		engine.eval(customActionScript);

		Map<String, String> runtimeEntity = new HashMap<String, String>();
		Map<String, String> inputEntityMap = new HashMap<String, String>();
		Map<String, String> outputEntity = null;
		inputEntityMap.put("params", paramsAll);
		inputEntityMap.put("imageUrl", "http://www.tutorialspoint.com/jsp/images/jsp_life_cycle.jpg");

		Object jsExecutor = invocable.invokeFunction("init", executor);
		invocable.invokeFunction("main", runtimeEntity, inputEntityMap, outputEntity, jsExecutor);

		synchronized (executor) {
			if (!executor.ready) {
				executor.wait(10000);
			}
			if (executor.ready) {
				System.out.println(runtimeEntity);
			} else {
				System.out.println("Custom Action timed out!");
			}
		}

	}

	//	AsyncHttpClient client = new AsyncHttpClient();
	//	BoundRequestBuilder post = client.preparePost(
	//			"http://10.164.12.82:8080/FocalCollectRest/configurationParameters/getConfig");
	//	post.setBody("{\"paramName\": \"WEBINT_COLLECT_REST_URL\"}");
	//	post.addHeader("Content-Type", "application/json");
	//	ListenableFuture<Response> listenableFuture = post.execute();
	//	Response response = listenableFuture.get();
	//	System.out.println(response.getResponseBody());
	//@Test
	public void runV8() throws IOException {
		V8 runtime = V8.createV8Runtime();
		V8Object executor = new V8Object(runtime);
		executor.add("name", "dummy name");
		runtime.add("executor", executor);
		String scriptPath = "v8Script.js";
		byte[] encoded = Files.readAllBytes(Paths.get(scriptPath));
		String v8Script = new String(encoded);
		JavaVoidCallback callback = new JavaVoidCallback() {
			public void invoke(final V8Object receiver, final V8Array parameters) {
				if (parameters.length() > 0) {
					Object arg1 = parameters.get(0);
					System.out.println(arg1);
					if (arg1 instanceof Releasable) {
						((Releasable) arg1).release();
					}
				}
			}
		};
		runtime.registerJavaMethod(callback, "flush");
		runtime.executeVoidScript(v8Script);
		System.out.println("Script executed");
		System.out.println(executor.get("name"));
		executor.release();
		runtime.release();
	}

	//@Test
	public void callRest() throws Exception {
		executeScript();

		//		AsyncHttpClient client = new AsyncHttpClient();
		//		BoundRequestBuilder post = client
		//				.preparePost("http://10.164.12.82:8080/FocalCollectRest/configurationParameters/getConfig");
		//		post.setBody("{\"paramName\": \"WEBINT_COLLECT_REST_URL\"}");
		//		post.addHeader("Content-Type", "application/json");
		//		ListenableFuture<Response> listenableFuture = post.execute();
		//		Response response = listenableFuture.get();
		//		System.out.println(response.getResponseBody());
	}

	//@Test
	public void runPhantom() throws IOException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setJavascriptEnabled(true);
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"resources/phantomjs.exe");
		PhantomJSDriver driver = new PhantomJSDriver(desiredCapabilities);

		Object scriptResult = driver.executePhantomJS(extensionScript, DefaultentitiesCount);
		System.out.println(scriptResult);
	}

	//@Test
	public void runScriptMultipleTimes() throws Exception {
		int[] entitiesCount = new int[] { 10, 100, 1000, 10_000, 100_000 };
		for (int count : entitiesCount) {
			long start = System.currentTimeMillis();
			executeScript(count);
			long end = System.currentTimeMillis();
			long firstRunTime = end - start;
			System.out.println(MessageFormat.format("Script entities: {0}, cold start time: {1}",
					count, end - start));
			int runs = 50;
			start = System.currentTimeMillis();
			for (int i = 0; i < runs - 1; i++) {
				executeScript(count);
			}
			end = System.currentTimeMillis();
			System.out.println(MessageFormat.format("Script entities: {2}, average time: {1}",
					firstRunTime, (end - start) / (runs - 1), count));
		}
	}

	//@Test
	public void runScriptDifferentEntitiesCount() throws Exception {
		int[] entitiesCount = new int[] { 10, 100, 1000, 10_000, 100_000 };
		Runtime runtime = Runtime.getRuntime();

		for (int count : entitiesCount) {
			System.gc();
			long allocatedMemoryStart = (runtime.totalMemory() / 1024) / 1024;
			long freeMemoryStart = (runtime.freeMemory() / 1024) / 1024;
			executeScript(count);
			long allocatedMemoryEnd = (runtime.totalMemory() / 1024) / 1024;
			long freeMemoryEnd = (runtime.freeMemory() / 1024) / 1024;
			System.out.println(MessageFormat.format(
					"Entities: {0} Allocated start: {1}MB, Allocated end: {2}MB\nFree start: {3}, Free end: {4}",
					count, allocatedMemoryStart, allocatedMemoryEnd, freeMemoryStart, freeMemoryEnd));
			//			System.out.println(MessageFormat.format("Start memory: {0}{1}End memory: {2}", allocatedMemoryStart,
			//					System.lineSeparator(), allocatedMemoryEnd));			
		}
	}

	void executeScript(int count) {

	}
	//		String scriptPath = "v8Script.js";
	//		byte[] encoded = Files.readAllBytes(Paths.get(scriptPath));
	//		String v8Script = new String(encoded);
	//		engine.eval(v8Script);
	//long evalFinished = System.currentTimeMillis();		
	//System.out.println("Script result: " + caResult);

	//script += "function getRandomValue(value, index) {  if (typeof value == 'string')    return value + index;}";
}
