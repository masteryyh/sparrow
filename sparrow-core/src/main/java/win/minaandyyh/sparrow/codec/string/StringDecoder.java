package win.minaandyyh.sparrow.codec.string;

import win.minaandyyh.sparrow.codec.Decoder;

/**
 * Simple body decoder, just casting body string to corresponding object
 *
 * @author masteryyh
 */
public class StringDecoder implements Decoder {
    @Override
    public Object decode(String body, Class<?> clazz) {
        if (body == null) {
            return null;
        }

        return clazz.cast(body);
    }
}
