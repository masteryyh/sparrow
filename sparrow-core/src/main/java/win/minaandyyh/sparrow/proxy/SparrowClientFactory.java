package win.minaandyyh.sparrow.proxy;

import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;

/**
 * Sparrow client factory interface
 *
 * @author masteryyh
 */
public interface SparrowClientFactory {
    <T> T getClient(Class<T> clazz, RequestHandler handler, Encoder encoder, Decoder decoder);
}
