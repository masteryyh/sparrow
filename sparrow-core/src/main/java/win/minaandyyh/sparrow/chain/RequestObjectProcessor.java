package win.minaandyyh.sparrow.chain;

/**
 * Request object processor interface
 *
 * @author masteryyh
 */
@FunctionalInterface
public interface RequestObjectProcessor {
    ProcessorChainContext process(ProcessorChainContext context) throws Exception;
}
