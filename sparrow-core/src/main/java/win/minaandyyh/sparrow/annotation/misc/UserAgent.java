package win.minaandyyh.sparrow.annotation.misc;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Conveniently set User-Agent header to specific request method or client
 *
 * @author masteryyh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
public @interface UserAgent {
    String value() default StringUtils.EMPTY;
}
