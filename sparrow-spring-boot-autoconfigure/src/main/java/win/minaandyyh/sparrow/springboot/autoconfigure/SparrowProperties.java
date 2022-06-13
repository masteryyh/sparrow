package win.minaandyyh.sparrow.springboot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Sparrow configuration properties
 *
 * @author masteryyh
 */
@ConfigurationProperties(prefix = "spring.sparrow")
public class SparrowProperties {
    public enum RequestHandlerType {
        HTTP_CLIENT_NEW, OKHTTP
    }

    public enum EncoderType {
        STRING, JACKSON
    }

    public enum DecoderType {
        STRING, JACKSON
    }

    private RequestHandlerType handler = RequestHandlerType.HTTP_CLIENT_NEW;

    private EncoderType encoder = EncoderType.JACKSON;

    private DecoderType decoder = DecoderType.JACKSON;

    public RequestHandlerType getHandler() {
        return handler;
    }

    public void setHandler(RequestHandlerType handler) {
        this.handler = handler;
    }

    public EncoderType getEncoder() {
        return encoder;
    }

    public void setEncoder(EncoderType encoder) {
        this.encoder = encoder;
    }

    public DecoderType getDecoder() {
        return decoder;
    }

    public void setDecoder(DecoderType decoder) {
        this.decoder = decoder;
    }
}
