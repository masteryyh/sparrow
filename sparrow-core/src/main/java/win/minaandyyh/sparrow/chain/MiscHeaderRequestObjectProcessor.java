package win.minaandyyh.sparrow.chain;

import win.minaandyyh.sparrow.annotation.misc.Connection;
import win.minaandyyh.sparrow.annotation.misc.DoNotTrack;
import win.minaandyyh.sparrow.annotation.misc.UserAgent;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Misc headers processor
 *
 * @author masteryyh
 */
public class MiscHeaderRequestObjectProcessor implements RequestObjectProcessor {
    private void tryUserAgent(Method method, Map<String, List<String>> headers) {
        UserAgent annotation = method.getAnnotation(UserAgent.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(UserAgent.class);
        }

        if (annotation != null) {
            String value = annotation.value();
            headers.put("User-Agent", List.of(value));
        }
    }

    private void tryConnection(Method method, Map<String, List<String>> headers) {
        Connection annotation = method.getAnnotation(Connection.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(Connection.class);
        }

        if (annotation != null) {
            String value = annotation.value();
            headers.put("Connection", List.of(value));
        }
    }

    private void tryDnt(Method method, Map<String, List<String>> headers) {
        DoNotTrack annotation = method.getAnnotation(DoNotTrack.class);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(DoNotTrack.class);
        }

        if (annotation != null) {
            String value = Integer.toString(annotation.value());
            headers.put("DNT", List.of(value));
        }
    }

    @Override
    public ProcessorChainContext process(ProcessorChainContext context) throws Exception {
        Map<String, List<String>> headers = context.requestObject().headers();
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }

        tryUserAgent(context.method(), headers);
        tryConnection(context.method(), headers);
        tryDnt(context.method(), headers);

        context.requestObject().headers(headers);
        return RequestObjectProcessChain.getInstance().doChain(context);
    }
}
