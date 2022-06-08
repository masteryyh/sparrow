package win.minaandyyh.sparrow.codec;

/**
 * Request body encoder interface
 *
 * @author masteryyh
 */
public interface Encoder {
    String encode(Object body, Class<?> clazz) throws Exception;
}
