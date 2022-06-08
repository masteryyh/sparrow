package win.minaandyyh.sparrow.metadata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Sparrow client metadata
 *
 * @author masteryyh
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@Accessors(fluent = true, chain = true)
@EqualsAndHashCode
public class RequestClientMetadata {
    private String name;

    private String url;

    private Map<String, RequestMethodMetadata> methods;
}
