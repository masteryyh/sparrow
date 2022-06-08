package win.minaandyyh.sparrow.http.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import win.minaandyyh.sparrow.http.AbstractRequestHandler;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.http.ResponseObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP request handler based on new JDK HttpClient
 *
 * @author masteryyh
 */
public final class JDKHttpClientRequestHandler extends AbstractRequestHandler implements RequestHandler {
    private final HttpClient client;

    private static final String ACCEPT = "Accept";

    private static final String CONTENT_TYPE = "Content-Type";

    public static JDKHttpClientRequestHandler defaultHandler() {
        return new JDKHttpClientRequestHandler(HttpClient.newHttpClient());
    }

    private JDKHttpClientRequestHandler(HttpClient client) {
        this.client = client;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Setter
    @Accessors(chain = true, fluent = true)
    public static final class Builder {
        private HttpClient client;

        public JDKHttpClientRequestHandler build() {
            return new JDKHttpClientRequestHandler(client);
        }
    }

    @Override
    public ResponseObject request(RequestObject requestObject) throws Exception {
        if (client == null) {
            throw new IllegalStateException("HttpClient object not set.");
        }

        String url = requestObject.url() + parseParam(requestObject.params());
        url = renderUrl(url, requestObject.urlVariables());
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url))
                .header(ACCEPT, requestObject.consumes().getValue());
        HttpRequest.BodyPublisher body;
        if (StringUtils.isEmpty(requestObject.body())) {
            body = HttpRequest.BodyPublishers.noBody();
        } else {
            body = HttpRequest.BodyPublishers.ofString(requestObject.body());
        }
        switch (requestObject.method()) {
            case GET -> requestBuilder.GET();
            case POST -> requestBuilder.POST(body)
                    .header(CONTENT_TYPE, requestObject.produces().getValue());
            case PATCH -> requestBuilder.method("PATCH", body)
                    .header(CONTENT_TYPE, requestObject.produces().getValue());
            case PUT -> requestBuilder.PUT(body)
                    .header(CONTENT_TYPE, requestObject.produces().getValue());
            case DELETE -> requestBuilder.method("DELETE", body)
                    .header(CONTENT_TYPE, requestObject.produces().getValue());
        }
        Map<String, String> headers = toRawHeaders(integrateHeader(requestObject.headers(), requestObject.cookies()));
        headers.forEach(requestBuilder::header);

        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return new ResponseObject()
                .code(response.statusCode())
                .headers(response.headers().map())
                .body(response.body());
    }
}
