package win.minaandyyh.sparrow.proxy;

import win.minaandyyh.sparrow.annotation.SparrowClient;
import win.minaandyyh.sparrow.annotation.request.Delete;
import win.minaandyyh.sparrow.annotation.request.Get;
import win.minaandyyh.sparrow.annotation.request.Patch;
import win.minaandyyh.sparrow.annotation.request.Post;
import win.minaandyyh.sparrow.annotation.request.Put;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.http.ResponseObject;
import win.minaandyyh.sparrow.metadata.HttpValueMetadata;
import win.minaandyyh.sparrow.metadata.MetadataHolder;
import win.minaandyyh.sparrow.metadata.RequestClientMetadata;
import win.minaandyyh.sparrow.metadata.RequestMethodMetadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract request method invocation interceptor
 *
 * @author masteryyh
 */
public abstract class AbstractInvocationInterceptor {
    protected final RequestHandler handler;

    protected final Encoder encoder;

    protected final Decoder decoder;

    private static final MetadataHolder metadataHolder = MetadataHolder.getInstance();

    protected AbstractInvocationInterceptor(RequestHandler handler, Encoder encoder, Decoder decoder) {
        this.handler = handler;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    protected boolean isNormalMethod(Method method) {
        if (!method.getDeclaringClass().isAnnotationPresent(SparrowClient.class)) {
            return true;
        }

        return !method.isAnnotationPresent(Get.class) && !method.isAnnotationPresent(Post.class) &&
                !method.isAnnotationPresent(Put.class) && !method.isAnnotationPresent(Patch.class) &&
                !method.isAnnotationPresent(Delete.class);
    }

    private List<String> readValue(Object arg, Class<?> type) {
        if (arg == null || type.equals(Void.class)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        if (Collection.class.isAssignableFrom(type)) {
            Collection<?> coll = (Collection<?>) arg;
            coll.forEach(elem -> result.add(elem.toString()));
            return result;
        }

        return List.of(arg.toString());
    }

    private String encodeBody(Object body, Class<?> bodyClass) throws Exception {
        return encoder.encode(body, bodyClass);
    }

    protected RequestObject getRequestObject(RequestClientMetadata clientMetadata, RequestMethodMetadata methodMetadata, Object[] args) throws Exception {
        RequestObject request = new RequestObject()
                .url(clientMetadata.url() + methodMetadata.path())
                .method(methodMetadata.method())
                .consumes(methodMetadata.consumes())
                .produces(methodMetadata.produces());

        Map<String, List<String>> headers = new LinkedHashMap<>();
        Map<String, List<String>> cookies = new LinkedHashMap<>();
        Map<String, List<String>> params = new LinkedHashMap<>();
        Map<String, String> urlVariables = new LinkedHashMap<>();
        List<HttpValueMetadata> parameterMetadata = methodMetadata.parameters();
        for (int i = 0; i < parameterMetadata.size(); i++) {
            HttpValueMetadata valueMetadata = parameterMetadata.get(i);
            switch (valueMetadata.type()) {
                case BODY -> request.body(encodeBody(args[i], valueMetadata.valueClass()));
                case PARAM -> {
                    List<String> value = readValue(args[i], valueMetadata.valueClass());
                    params.put(valueMetadata.name(), value);
                }
                case HEADER -> {
                    List<String> value = readValue(args[i], valueMetadata.valueClass());
                    headers.put(valueMetadata.name(), value);
                }
                case COOKIE -> {
                    List<String> value = readValue(args[i], valueMetadata.valueClass());
                    cookies.put(valueMetadata.name(), value);
                }
                case URL_VARIABLE -> urlVariables.put(valueMetadata.name(), args[i].toString());
            }
        }

        return request.params(params)
                .headers(headers)
                .cookies(cookies)
                .urlVariables(urlVariables);
    }

    protected Object doRequest(Method method, Object[] args) throws Exception {
        Class<?> parentClass = method.getDeclaringClass();
        RequestClientMetadata clientMetadata = metadataHolder.get(parentClass);
        String name = method.getName();
        RequestMethodMetadata methodMetadata = clientMetadata.methods().get(name);

        RequestObject request = getRequestObject(clientMetadata, methodMetadata, args);
        ResponseObject response = handler.request(request);

        // TODO: status code
        return decoder.decode(response.body(), methodMetadata.responseClass());
    }
}
