package win.minaandyyh.sparrow.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Response object
 *
 * @author masteryyh
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@EqualsAndHashCode
public class ResponseObject {
    private int code;

    private Map<String, List<String>> headers;

    private String body;
}
