import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ProxySettings;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireMockTest {

	private int port = 8080;
	public WireMockServer wireMock = new WireMockServer(port);

	//	@Test
	public void exampleTest() throws IOException {
		wireMock.start();
		String endPoint = "/my/resource";
		StubMapping mapping = stubFor(get(urlEqualTo(endPoint))
				.withHeader("Accept", equalTo("text/xml"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/jpeg")
						.withBody("<response>Some content</response>".getBytes())));
		wireMock.addStubMapping(mapping);

		URL url = new URL("http://localhost:" + port + endPoint);
		System.out.println(url.getContent());
		//		assertTrue(result.wasSuccessful());
		//
		//		verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
		//				.withRequestBody(matching(".*<message>1234</message>.*"))
		//				.withHeader("Content-Type", notMatching("application/json")));
	}

	@Rule
	public final TemporaryFolder tempDir = new TemporaryFolder();

	@Test
	public void instantiationWithEmptyFileSource() throws IOException {
		Options options = new WireMockConfiguration().port(port)
				.fileSource(new SingleRootFileSource(tempDir.getRoot()));

		WireMockServer wireMockServer = null;
		try {
			wireMockServer = new WireMockServer(options);
			wireMockServer.start();
			String endPoint = "/my/resource";
			wireMockServer.stubFor(get(urlEqualTo(endPoint))
					.willReturn(aResponse()
							.withHeader("Content-Type", "application/jpeg")
							.withBody("adasd".getBytes())
							.withStatus(200)));
			URL url = new URL("http://localhost:" + port + endPoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.connect();
			System.out.println(conn.getContentType());
		} finally {
			if (wireMockServer != null) {
				wireMockServer.stop();
			}
		}
	}

	// https://github.com/tomakehurst/wiremock/issues/193
	//	@Test
	public void supportsRecordingProgrammaticallyWithoutHeaderMatching() {
		WireMockServer wireMockServer = new WireMockServer(Options.DYNAMIC_PORT,
				new SingleRootFileSource(tempDir.getRoot()), false,
				new ProxySettings("proxy.company.com", Options.DYNAMIC_PORT));
		wireMockServer.start();
		wireMockServer.enableRecordMappings(new SingleRootFileSource(tempDir.getRoot() + "/mappings"),
				new SingleRootFileSource(tempDir.getRoot() + "/__files"));
		wireMockServer.stubFor(get(urlEqualTo("/something")).willReturn(aResponse()
				.withHeader("Content-Type", "application/jpeg")
				.withStatus(200)));

		//		WireMockTestClient client = new WireMockTestClient(wireMockServer.port());
		//		assertThat(client.get("http://localhost:" + wireMockServer.port() + "/something").statusCode(), is(200));
	}

}
