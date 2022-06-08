package win.minaandyyh.sparrow.metadata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import win.minaandyyh.sparrow.MediaType;
import win.minaandyyh.sparrow.RequestMethod;

import java.util.List;

/**
 * HTTP request method metadata
 *
 * @author masteryyh
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true, fluent = true)
@EqualsAndHashCode
public class RequestMethodMetadata {
    private String name;

    private String path;

    private RequestMethod method;

    private MediaType consumes;

    private MediaType produces;

    private List<HttpValueMetadata> parameters;

    private Class<?> responseClass;
}
