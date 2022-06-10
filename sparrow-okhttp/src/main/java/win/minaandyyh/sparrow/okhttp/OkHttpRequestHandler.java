package win.minaandyyh.sparrow.okhttp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import win.minaandyyh.sparrow.MediaType;
import win.minaandyyh.sparrow.RequestMethod;
import win.minaandyyh.sparrow.constant.Constant;
import win.minaandyyh.sparrow.http.AbstractRequestHandler;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.http.ResponseObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP request handler based on OkHttp
 *
 * @author masteryyh
 */
public class OkHttpRequestHandler extends AbstractRequestHandler implements RequestHandler {
    private final OkHttpClient client;

    private OkHttpRequestHandler(OkHttpClient client) {
        this.client = client;
    }

    private OkHttpRequestHandler() {
        this.client = new OkHttpClient();
    }

    public static OkHttpRequestHandler defaultHandler() {
        return new OkHttpRequestHandler();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private OkHttpClient client;

        public OkHttpRequestHandler build() {
            return new OkHttpRequestHandler(client);
        }
    }

    private void prepareRequest(String content, RequestMethod method, MediaType mediaType, Request.Builder builder) {
        RequestBody body;
        if (StringUtils.isEmpty(content)) {
            body = RequestBody.create(new byte[0], null);
        } else {
            // TODO: form body processing
            body = RequestBody.create(content.getBytes(StandardCharsets.UTF_8), okhttp3.MediaType.parse(mediaType.getValue()));
        }

        switch (method) {
            case GET -> builder.get();
            case POST -> builder.post(body);
            case PUT -> builder.put(body);
            case PATCH -> builder.patch(body);
            case DELETE -> builder.delete(body);
        }

    }

    @Override
    public ResponseObject request(RequestObject requestObject) throws Exception {
        if (client == null) {
            throw new IllegalStateException("OkHttpClient object not set.");
        }

        String url = prepareUrl(requestObject.url(), requestObject.params(), requestObject.urlVariables());
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header(Constant.ACCEPT, requestObject.produces().getValue());
        prepareRequest(requestObject.body(), requestObject.method(), requestObject.consumes(), builder);

        Map<String, String> headers = toRawHeaders(requestObject.headers());
        headers.forEach(builder::header);

        try (Response response = client.newCall(builder.build()).execute()) {
            ResponseBody body = response.body();
            String bodyString;
            if (body == null) {
                bodyString = StringUtils.EMPTY;
            } else {
                bodyString = body.string();
            }

            return new ResponseObject()
                    .code(response.code())
                    .body(bodyString)
                    .headers(response.headers().toMultimap());
        }
    }
}
