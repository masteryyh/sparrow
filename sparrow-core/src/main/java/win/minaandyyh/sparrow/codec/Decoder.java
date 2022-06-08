package win.minaandyyh.sparrow.codec;

/**
 * Response body decoder interface
 *
 * @author masteryyh
 */
public interface Decoder {
    Object decode(String body, Class<?> clazz) throws Exception;
}
