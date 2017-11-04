package scripting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

public class XMLHttpRequest {
	private String method;
	private String url;
	private boolean async;
	private String user;
	private String password;
	private Map<String, String> headers;
	private int readyState;
	private String responseText;
	private int status;
	private Object response;
	private boolean withCredentials;
	private int timeout;
	private String responseType;
	private Object statusText;
	private int order;
	private ScriptObjectMirror onReadyStateChange;

	public XMLHttpRequest(int order) {
		//System.out.println("XMLHttpRequest(); order: " + order);
		this.setOrder(order);
		this.readyState = 0;
		this.response = null;
		this.responseType = "";
		this.statusText = null;
		this.timeout = 0; // no timeout by default
		this.withCredentials = false;
		headers = new HashMap<>();
	}

	public void setonReadyStateChange(ScriptObjectMirror handler) {
		onReadyStateChange = handler;
	}

	public ScriptObjectMirror getonReadyStateChange() {
		return onReadyStateChange;
	}

	public void setRequestHeader(String key, String value) {
		headers.put(key, value);
	}

	public void open(String method, String url) {
		open(method, url, false, null, null);
	}

	public void open(String method, String url, boolean async, String user, String password) {
		this.readyState = 1;
		this.method = method;
		this.url = url;
		this.async = async;
		this.user = user == null ? "" : user;
		this.password = password == null ? "" : password;
	}

	public void send(String data) throws IOException, InterruptedException, ExecutionException {
		AsyncHttpClient client = new AsyncHttpClient();
		BoundRequestBuilder requestBuilder = null;
		switch (method) {
		case "POST":
			requestBuilder = client.preparePost(url);
			break;
		default:
			return;
		}
		for (Entry<String, String> entry : headers.entrySet()) {
			requestBuilder.addHeader(entry.getKey(), entry.getValue());
		}

		requestBuilder.setBody(data);
		requestBuilder.execute(new HttpRequestHandler(this));
	}

	public int getStatus() {
		return status;
	}

	public String getResponseText() {
		return responseText;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
}
