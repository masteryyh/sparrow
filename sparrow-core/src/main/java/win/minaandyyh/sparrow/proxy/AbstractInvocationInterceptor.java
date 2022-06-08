package win.minaandyyh.sparrow.proxy;

import win.minaandyyh.sparrow.annotation.SparrowClient;
import win.minaandyyh.sparrow.annotation.request.Delete;
import win.minaandyyh.sparrow.annotation.request.Get;
import win.minaandyyh.sparrow.annotation.request.Patch;
import win.minaandyyh.sparrow.annotation.request.Post;
import win.minaandyyh.sparrow.annotation.request.Put;
import win.minaandyyh.sparrow.chain.RequestObjectProcessChain;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.http.ResponseObject;
import win.minaandyyh.sparrow.metadata.MetadataHolder;
import win.minaandyyh.sparrow.metadata.RequestClientMetadata;
import win.minaandyyh.sparrow.metadata.RequestMethodMetadata;

import java.lang.reflect.Method;

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

    protected RequestObject getRequestObject(Method method, Object[] args) throws Exception {
        return RequestObjectProcessChain.getInstance()
                .startProcess(method, args, encoder, decoder)
                .requestObject();
    }

    protected Object doRequest(Method method, Object[] args) throws Exception {
        Class<?> parentClass = method.getDeclaringClass();
        RequestClientMetadata clientMetadata = metadataHolder.get(parentClass);
        String name = method.getName();
        RequestMethodMetadata methodMetadata = clientMetadata.methods().get(name);

        RequestObject request = getRequestObject(method, args);
        ResponseObject response = handler.request(request);

        // TODO: status code
        return decoder.decode(response.body(), methodMetadata.responseClass());
    }
}
