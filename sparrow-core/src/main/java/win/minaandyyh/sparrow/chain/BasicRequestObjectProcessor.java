package win.minaandyyh.sparrow.chain;

import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.metadata.MetadataHolder;
import win.minaandyyh.sparrow.metadata.RequestClientMetadata;
import win.minaandyyh.sparrow.metadata.RequestMethodMetadata;

/**
 * HTTP basic elements processor
 *
 * @author masteryyh
 */
public class BasicRequestObjectProcessor implements RequestObjectProcessor {
    private final MetadataHolder metadataHolder = MetadataHolder.getInstance();

    @Override
    public ProcessorChainContext process(ProcessorChainContext context) throws Exception {
        if (context == null) {
            throw new NullPointerException("Context object is null.");
        }

        RequestClientMetadata clientMetadata = metadataHolder.get(context.method().getDeclaringClass());
        RequestMethodMetadata methodMetadata = clientMetadata.methods().get(context.method().getName());
        context.clientMetadata(clientMetadata);

        RequestObject requestObject = context.requestObject();
        if (requestObject == null) {
            requestObject = new RequestObject()
                    .url(clientMetadata.url() + methodMetadata.path())
                    .method(methodMetadata.method())
                    .consumes(methodMetadata.consumes())
                    .produces(methodMetadata.produces());
        }

        return RequestObjectProcessChain.getInstance().doChain(context.requestObject(requestObject));
    }
}
