package scripting;

import com.ning.http.client.AsyncCompletionHandlerBase;
import com.ning.http.client.Response;

public class HttpRequestHandler extends AsyncCompletionHandlerBase {

	private XMLHttpRequest request;

	public HttpRequestHandler(XMLHttpRequest request) {
		this.request = request;
	}

	@Override
	public Response onCompleted(Response response) throws Exception {
		request.setStatus(response.getStatusCode());
		request.setResponseText(response.getResponseBody("UTF-8"));
		runHandler(request);
		return super.onCompleted(response);
	}

	public static synchronized void runHandler(XMLHttpRequest request) {
		request.getonReadyStateChange().call(request, request);
	}
}