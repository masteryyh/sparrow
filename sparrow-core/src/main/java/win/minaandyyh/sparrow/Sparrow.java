package win.minaandyyh.sparrow;

import lombok.Setter;
import lombok.experimental.Accessors;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.impl.JDKHttpClientRequestHandler;
import win.minaandyyh.sparrow.proxy.SparrowClientFactory;
import win.minaandyyh.sparrow.proxy.cglib.CGLibProxySparrowClientFactory;
import win.minaandyyh.sparrow.proxy.jdk.JDKProxySparrowClientFactory;

import java.lang.reflect.Modifier;

/**
 * Sparrow client builder
 *
 * @author masteryyh
 */
@Setter
@Accessors(chain = true, fluent = true)
public class Sparrow<T> {
    private RequestHandler handler;

    private Encoder encoder;

    private Decoder decoder;

    private Class<T> client;

    private static final SparrowClientFactory classFactory = CGLibProxySparrowClientFactory.getInstance();

    private static final SparrowClientFactory interfaceFactory = JDKProxySparrowClientFactory.getInstance();

    private Sparrow() {}

    public static <T> Sparrow<T> newClient() {
        return new Sparrow<>();
    }

    public T build() {
        if (client == null) {
            throw new IllegalArgumentException("Client class not set.");
        }
        if (handler == null) {
            handler = JDKHttpClientRequestHandler.defaultHandler();
        }

        if (client.isInterface()) {
            return interfaceFactory.getClient(client, handler, encoder, decoder);
        }
        int modifier = client.getModifiers();
        if (Modifier.isFinal(modifier) || Modifier.isAbstract(modifier)) {
            throw new IllegalArgumentException("Cannot proxy abstract or final class.");
        }
        return classFactory.getClient(client, handler, encoder, decoder);
    }
}
