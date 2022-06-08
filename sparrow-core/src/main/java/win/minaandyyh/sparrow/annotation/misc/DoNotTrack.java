package win.minaandyyh.sparrow.annotation.misc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Conveniently set DNT (Do-Not-Track) header to specific request method or client
 *
 * @author masteryyh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
public @interface DoNotTrack {
    int value() default 1;
}
