package win.minaandyyh.sparrow.metadata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * HTTP request value metadata
 *
 * @author masteryyh
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true, fluent = true)
@EqualsAndHashCode
public class HttpValueMetadata {
    private String name;

    private Class<?> valueClass;

    private ValueType type;
}
