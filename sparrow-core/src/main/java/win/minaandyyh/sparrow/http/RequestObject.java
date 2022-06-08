package win.minaandyyh.sparrow.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import win.minaandyyh.sparrow.MediaType;
import win.minaandyyh.sparrow.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Request parameters
 *
 * @author masteryyh
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@EqualsAndHashCode
public class RequestObject {
    private String url;

    private RequestMethod method;

    private MediaType consumes;

    private MediaType produces;

    private Map<String, List<String>> params;

    private Map<String, List<String>> headers;

    private Map<String, List<String>> cookies;

    private Map<String, String> urlVariables;

    private String body;
}
