package win.minaandyyh.sparrow.spring;

/**
 * Throws when failed to create sparrow client bean
 *
 * @author masteryyh
 */
public class SparrowCreationException extends RuntimeException {
    public SparrowCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
