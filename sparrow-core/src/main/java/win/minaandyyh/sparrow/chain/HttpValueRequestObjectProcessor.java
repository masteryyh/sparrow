package win.minaandyyh.sparrow.chain;

import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.metadata.HttpValueMetadata;
import win.minaandyyh.sparrow.metadata.RequestMethodMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP values processor
 *
 * @author masteryyh
 */
public class HttpValueRequestObjectProcessor implements RequestObjectProcessor {
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

    @Override
    public ProcessorChainContext process(ProcessorChainContext context) throws Exception {
        RequestMethodMetadata methodMetadata = context.clientMetadata().methods().get(context.method().getName());
        RequestObject request = context.requestObject();
        Object[] args = context.args();

        Map<String, List<String>> headers = new LinkedHashMap<>();
        Map<String, List<String>> cookies = new LinkedHashMap<>();
        Map<String, List<String>> params = new LinkedHashMap<>();
        Map<String, String> urlVariables = new LinkedHashMap<>();
        List<HttpValueMetadata> parameterMetadata = methodMetadata.parameters();
        for (int i = 0; i < parameterMetadata.size(); i++) {
            HttpValueMetadata valueMetadata = parameterMetadata.get(i);
            switch (valueMetadata.type()) {
                case BODY -> request.body(context.encoder().encode(args[i], valueMetadata.valueClass()));
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

        request.params(params)
                .headers(headers)
                .cookies(cookies)
                .urlVariables(urlVariables);
        return RequestObjectProcessChain.getInstance().doChain(context);
    }
}
