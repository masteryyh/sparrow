package win.minaandyyh.sparrow.annotation.request;

import org.apache.commons.lang3.StringUtils;
import win.minaandyyh.sparrow.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate an annotated method may perform a DELETE request
 *
 * @author masteryyh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delete {
    String path() default StringUtils.EMPTY;

    MediaType consumes() default MediaType.APPLICATION_JSON;

    MediaType produces() default MediaType.TEXT_PLAIN;
}
