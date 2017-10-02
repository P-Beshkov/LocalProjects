package scripting;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Executor {
	public int amount = 8;
	public boolean ready = false;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public Executor() {
		amount = 5;
	}

	public String flush(ScriptObjectMirror entities) {
		System.out.println("Executor->flush(); entities count: " + entities.size());
		return "Executor method flush";
	}

	public void ready() {
		ready = true;
		synchronized (this) {
			notifyAll();
		}
	}

	public void saveBinary(String urlString, ScriptObjectMirror onSuccess, ScriptObjectMirror onError,
			ScriptObjectMirror data) {
		executorService.submit(new SaveBinaryHandler(urlString, onSuccess, onError, data));
	}

	private class SaveBinaryHandler implements Runnable {

		private String urlString;
		private ScriptObjectMirror onSuccess;
		private ScriptObjectMirror onError;
		private ScriptObjectMirror data;

		public SaveBinaryHandler(String urlString, ScriptObjectMirror onSuccess, ScriptObjectMirror onError,
				ScriptObjectMirror data) {
			this.urlString = urlString;
			this.onSuccess = onSuccess;
			this.onError = onError;
			this.data = data;
		}

		@Override
		public void run() {
			try {
				String name = urlString.substring(urlString.lastIndexOf("/") + 1);
				URL url = new URL(urlString);
				String path = "C:\\Temp\\" + name;
				try (InputStream in = new BufferedInputStream(url.openStream());
						FileOutputStream fos = new FileOutputStream(path)) {
					byte[] buf = new byte[1024];
					int n = 0;
					while (n != -1) {
						fos.write(buf, 0, n);
						n = in.read(buf);
					}
				}
				onSuccess.call(this, path, data);
			} catch (IOException e) {
				onError.call(this, e);
			}
		}

	}
}