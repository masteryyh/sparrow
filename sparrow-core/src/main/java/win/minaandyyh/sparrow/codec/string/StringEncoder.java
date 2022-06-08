package win.minaandyyh.sparrow.codec.string;

import org.apache.commons.lang3.StringUtils;
import win.minaandyyh.sparrow.codec.Encoder;

/**
 * Simple body encoder, just calling body.toString()
 *
 * @author masteryyh
 */
public class StringEncoder implements Encoder {
    @Override
    public String encode(Object body, Class<?> clazz) {
        if (body == null) {
            return StringUtils.EMPTY;
        }

        return body.toString();
    }
}
