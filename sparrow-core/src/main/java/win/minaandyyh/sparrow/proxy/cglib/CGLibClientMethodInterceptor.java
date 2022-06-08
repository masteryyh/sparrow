package win.minaandyyh.sparrow.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.proxy.AbstractInvocationInterceptor;

import java.lang.reflect.Method;

/**
 * CGLib proxied client method invocation interceptor
 *
 * @author masteryyh
 */
public class CGLibClientMethodInterceptor extends AbstractInvocationInterceptor implements MethodInterceptor {
    public CGLibClientMethodInterceptor(RequestHandler handler, Encoder encoder, Decoder decoder) {
        super(handler, encoder, decoder);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (isNormalMethod(method)) {
            return proxy.invoke(obj, args);
        }

        return doRequest(method, args);
    }
}
