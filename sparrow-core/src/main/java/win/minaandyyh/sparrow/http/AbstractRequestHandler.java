package win.minaandyyh.sparrow.http;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Abstract HTTP request handler
 *
 * @author masteryyh
 */
public abstract class AbstractRequestHandler implements RequestHandler {
    protected String parseParam(Map<String, List<String>> params) {
        if (MapUtils.isEmpty(params)) {
            return StringUtils.EMPTY;
        }

        StringJoiner joiner = new StringJoiner("&", "?", "");
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();

            if (CollectionUtils.isEmpty(values)) {
                joiner.add(name + '=');
            } else if (values.size() == 1) {
                joiner.add(name + '=' + values.get(0));
            } else {
                for (String value : values) {
                    joiner.add(name + '=' + value);
                }
            }
        }

        return joiner.toString();
    }

    protected Map<String, List<String>> integrateHeader(Map<String, List<String>> headers, Map<String, List<String>> cookies) {
        if (MapUtils.isEmpty(cookies)) {
            return headers;
        }
        
        StringJoiner joiner = new StringJoiner("; ");
        for (Map.Entry<String, List<String>> entry : cookies.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            
            if (CollectionUtils.isEmpty(values)) {
                joiner.add(name);
            } else if (values.size() == 1) {
                joiner.add(name + '=' + values.get(0));
            } else {
                joiner.add(name + '=' + String.join(",", values));
            }
        }

        Map<String, List<String>> newHeaders = new LinkedHashMap<>(headers);
        newHeaders.put("Cookie", List.of(joiner.toString()));
        return newHeaders;
    }

    protected String renderUrl(String url, Map<String, String> urlVariables) {
        if (StringUtils.isBlank(url)) {
            throw new NullPointerException("Empty url to render.");
        }

        if (MapUtils.isEmpty(urlVariables)) {
            return url;
        }

        for (Map.Entry<String, String> entry : urlVariables.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            url = url.replaceAll("\\{" + name + "}", value);
        }
        return url;
    }

    protected String prepareUrl(String url, Map<String, List<String>> params, Map<String, String> urlVariables) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Base URL is empty.");
        }
        return renderUrl(url, urlVariables) + parseParam(params);
    }

    protected Map<String, String> toRawHeaders(Map<String, List<String>> headers) {
        if (MapUtils.isEmpty(headers)) {
            return Collections.emptyMap();
        }

        Map<String, String> rawHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            List<String> value = entry.getValue();
            rawHeaders.put(name, String.join("; ", value));
        }
        return rawHeaders;
    }
}
