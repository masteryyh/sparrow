package win.minaandyyh.sparrow.proxy.jdk;

import win.minaandyyh.sparrow.annotation.SparrowClient;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.proxy.SparrowClientFactory;

import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JDK proxy based client factory
 *
 * @author masteryyh
 */
public class JDKProxySparrowClientFactory implements SparrowClientFactory {
    private JDKProxySparrowClientFactory() {}

    private static volatile JDKProxySparrowClientFactory instance;

    private static final Lock LOCK = new ReentrantLock();

    public static JDKProxySparrowClientFactory getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new JDKProxySparrowClientFactory();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> clazz, RequestHandler handler, Encoder encoder, Decoder decoder) {
        if (clazz == null) {
            throw new NullPointerException("Class object is null.");
        }
        if (!clazz.isAnnotationPresent(SparrowClient.class)) {
            throw new IllegalArgumentException("Not a client: " + clazz.getCanonicalName() + ".");
        }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JDKProxyClientInvocationHandler(handler, encoder, decoder));
    }
}
