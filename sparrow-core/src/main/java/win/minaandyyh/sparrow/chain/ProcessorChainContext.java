package win.minaandyyh.sparrow.chain;

import lombok.Data;
import lombok.experimental.Accessors;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.http.RequestObject;
import win.minaandyyh.sparrow.metadata.RequestClientMetadata;

import java.lang.reflect.Method;

/**
 * Request object process chain context object
 *
 * @author masteryyh
 */
@Data
@Accessors(chain = true, fluent = true)
public class ProcessorChainContext {
    private Method method;

    private Object[] args;

    private RequestObject requestObject;

    private RequestClientMetadata clientMetadata;

    private int chainPos;

    private Encoder encoder;

    private Decoder decoder;
}
