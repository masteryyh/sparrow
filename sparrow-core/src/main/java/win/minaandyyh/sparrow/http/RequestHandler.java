package win.minaandyyh.sparrow.http;

/**
 * HTTP request handler
 *
 * @author masteryyh
 */
public interface RequestHandler {
    ResponseObject request(RequestObject requestObject) throws Exception;
}
