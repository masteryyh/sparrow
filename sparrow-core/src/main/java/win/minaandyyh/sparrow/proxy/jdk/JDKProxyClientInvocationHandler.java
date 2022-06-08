package win.minaandyyh.sparrow.proxy.jdk;

import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.proxy.AbstractInvocationInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK proxied client method invocation handler
 *
 * @author masteryyh
 */
public class JDKProxyClientInvocationHandler extends AbstractInvocationInterceptor implements InvocationHandler {
    public JDKProxyClientInvocationHandler(RequestHandler handler, Encoder encoder, Decoder decoder) {
        super(handler, encoder, decoder);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        if (isNormalMethod(method)) {
            return method.invoke(proxy, args);
        }

        return doRequest(method, args);
    }
}
