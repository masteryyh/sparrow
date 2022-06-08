package win.minaandyyh.sparrow.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import win.minaandyyh.sparrow.annotation.SparrowClient;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.proxy.SparrowClientFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CGLib proxy based client factory
 *
 * @author masteryyh
 */
public class CGLibProxySparrowClientFactory implements SparrowClientFactory {
    private CGLibProxySparrowClientFactory() {}

    private static volatile CGLibProxySparrowClientFactory instance;

    private static final Lock LOCK = new ReentrantLock();

    public static CGLibProxySparrowClientFactory getInstance() {
        if (instance == null) {
            LOCK.lock();
            try {
                if (instance == null) {
                    instance = new CGLibProxySparrowClientFactory();
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

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new CGLibClientMethodInterceptor(handler, encoder, decoder));
        return (T) enhancer.create();
    }
}
