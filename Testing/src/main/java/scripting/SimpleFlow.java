package scripting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

public class SimpleFlow {
	static String scriptPath = "WebAlertScript.js";
	byte[] encoded;
	String script;

	public static void main(String[] args) throws Exception {
		SimpleFlow flow = new SimpleFlow();
		//		flow.encoded = Files.readAllBytes(Paths.get(scriptPath));
		//		flow.script = new String(flow.encoded);
		//flow.runJavaScripting();
		flow.runPhantom();
		//		flow.runV8();
	}

	private void runnV8() throws IOException {
		V8 runtime = V8.createV8Runtime();
		V8Object executor = new V8Object(runtime);
		executor.add("name", "dummy name");
		runtime.add("executor", executor);
		String scriptPath = "v8Script.js";
		byte[] encoded = Files.readAllBytes(Paths.get(scriptPath));
		String v8Script = new String(encoded);
		Thread nameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println(executor.get("name"));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		});
		nameThread.start();
		runtime.executeVoidScript(v8Script);
		//		System.out.println(result);
		executor.release();
		runtime.release();
		nameThread.interrupt();
	}

	private void runPhantom() throws IOException {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setJavascriptEnabled(true);
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"resources/phantomjs.exe");
		PhantomJSDriver driver = new PhantomJSDriver(desiredCapabilities);
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.get(
				"https://code.tutsplus.com/tutorials/headless-functional-testing-with-selenium-and-phantomjs--net-30545");

		File srcFile = driver.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere

		FileUtils.copyFile(srcFile, new File("c:\\tmp\\screenshot.png"));

		//		Object scriptResult = driver.executePhantomJS(script);
		//		System.out.println(scriptResult);
	}

	private void runJavaScripting() throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		long start = System.currentTimeMillis();
		engine.eval(script);
		Invocable invocable = (Invocable) engine;
		Executor executor = new Executor();
		//		AsyncHttpClient client = new AsyncHttpClient();
		//		BoundRequestBuilder post = client.preparePost("http://10.164.12.82:8080/FocalCollectRest/configurationParameters/getConfig");
		//		post.setBody("{\"paramName\": \"WEBINT_COLLECT_REST_URL\"}");
		//		post.addHeader("Content-Type", "application/json");
		//		ListenableFuture<Response> listenableFuture = post.execute();
		//		Response response = listenableFuture.get();
		//		System.out.println(response.getResponseBody());

		Object caResult = invocable.invokeFunction("run", executor);
		long end = System.currentTimeMillis();
		System.out.println("Script executed in " + (end - start));
		//		if (!executor.ready) {
		//			synchronized (executor) {
		//				if (!executor.ready) {
		//					executor.wait(10000);
		//				}
		//			}
		//		}
		//		Timer timer = new Timer();
		//		timer.cancel();
		//		System.out.println(caResult);
	}

	public static String read(String path) {
		System.out.println("Static method read(), param: " + path);
		return "greetings from Java";
	}

	public class ForEachAction implements Action<List<String>> {

		private WebDriver driver;
		private String xPath;

		@Override
		public List<String> execute() {
			return driver.findElements(By.xpath(xPath)).stream().map(element -> {
				if (element.getTagName().equals("a")) {
					return element.getAttribute("href");
				}
				return element.getText();
			}).collect(Collectors.toList());
		}

		public void setXPath(String xPath) {
			this.xPath = xPath;
		}

		public void setDriver(WebDriver driver) {
			this.driver = driver;
		}
	}

	public class ExtractValueByXPath implements Action<String> {
		private WebDriver driver;
		private String xPath;

		@Override
		public String execute() {
			return driver.findElement(By.xpath(xPath)).getText();
		}

		public void setDriver(WebDriver driver) {
			this.driver = driver;
		}

		public void setXPath(String xPath) {
			this.xPath = xPath;
		}
	}

	public interface Action<T> {
		public T execute();
	}
	//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); FileUtils.readFileToString(new File("phantomScript.js"))

	//		driver.get(baseUrl);
	//		ForEachAction forEach = new ForEachAction();
	//		forEach.setXPath(".//*[@id='content']/div[5]/div/div[2]/div[3]/a");
	//		forEach.setDriver(driver);
	//		forEach.execute().stream().limit(10).forEach(webElement -> {
	//			driver.get(webElement);
	//			ExtractValueByXPath extract = new ExtractValueByXPath();
	//			extract.setDriver(driver);
	//			extract.setXPath("/html/body/div[1]/div[5]/h1");
	//			System.out.println(extract.execute());
	//		});
	//	String baseUrl = "http://shop.sky.bg/index.php?route=product/category&path=5_58&sort=p.price&order=ASC&limit=100/";

}
