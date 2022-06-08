package win.minaandyyh.sparrow.chain;

import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object process chain
 *
 * @author masteryyh
 */
public class RequestObjectProcessChain {
    private final List<RequestObjectProcessor> processors;

    private RequestObjectProcessChain() {
        this.processors = new ArrayList<>();
        this.processors.add(new BasicRequestObjectProcessor());
        this.processors.add(new HttpValueRequestObjectProcessor());
        this.processors.add(new MiscHeaderRequestObjectProcessor());
    }

    private static volatile RequestObjectProcessChain instance;

    public static RequestObjectProcessChain getInstance() {
        if (instance == null) {
            synchronized (RequestObjectProcessChain.class) {
                if (instance == null) {
                    instance = new RequestObjectProcessChain();
                }
            }
        }
        return instance;
    }

    public ProcessorChainContext doChain(ProcessorChainContext context) throws Exception {
        int pos = context.chainPos();
        if (pos + 1 >= processors.size()) {
            return context;
        }

        int newPos = pos + 1;
        return processors.get(newPos).process(context.chainPos(newPos));
    }

    public ProcessorChainContext startProcess(Method method, Object[] args, Encoder encoder, Decoder decoder) throws Exception {
        ProcessorChainContext context = new ProcessorChainContext()
                .method(method)
                .args(args)
                .chainPos(0)
                .encoder(encoder)
                .decoder(decoder);
        return processors.get(0).process(context);
    }
}
