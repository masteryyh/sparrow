package win.minaandyyh.sparrow.http.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import win.minaandyyh.sparrow.MediaType;
import win.minaandyyh.sparrow.RequestMethod;
import win.minaandyyh.sparrow.constant.Constant;
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
    
    private void prepareRequest(String content, RequestMethod method, MediaType mediaType, HttpRequest.Builder builder) {
        HttpRequest.BodyPublisher body;
        if (StringUtils.isEmpty(content)) {
            body = HttpRequest.BodyPublishers.noBody();
        } else {
            body = HttpRequest.BodyPublishers.ofString(content);
        }
        switch (method) {
            case GET -> builder.GET();
            case POST -> builder.POST(body)
                    .header(Constant.CONTENT_TYPE, mediaType.getValue());
            case PATCH -> builder.method("PATCH", body)
                    .header(Constant.CONTENT_TYPE, mediaType.getValue());
            case PUT -> builder.PUT(body)
                    .header(Constant.CONTENT_TYPE, mediaType.getValue());
            case DELETE -> builder.method("DELETE", body)
                    .header(Constant.CONTENT_TYPE, mediaType.getValue());
        }
    }
    
    @Override
    public ResponseObject request(RequestObject requestObject) throws Exception {
        if (client == null) {
            throw new IllegalStateException("HttpClient object not set.");
        }

        String url = prepareUrl(requestObject.url(), requestObject.params(), requestObject.urlVariables());
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url))
                .header(Constant.ACCEPT, requestObject.produces().getValue());
        prepareRequest(requestObject.body(), requestObject.method(), requestObject.consumes(), requestBuilder);
        
        Map<String, String> headers = toRawHeaders(integrateHeader(requestObject.headers(), requestObject.cookies()));
        headers.forEach(requestBuilder::header);

        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return new ResponseObject()
                .code(response.statusCode())
                .headers(response.headers().map())
                .body(response.body());
    }
}
