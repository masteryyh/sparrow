package win.minaandyyh.sparrow.springboot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import win.minaandyyh.sparrow.Sparrow;
import win.minaandyyh.sparrow.codec.Decoder;
import win.minaandyyh.sparrow.codec.Encoder;
import win.minaandyyh.sparrow.codec.string.StringDecoder;
import win.minaandyyh.sparrow.codec.string.StringEncoder;
import win.minaandyyh.sparrow.http.RequestHandler;
import win.minaandyyh.sparrow.http.impl.JDKHttpClientRequestHandler;
import win.minaandyyh.sparrow.jackson.JacksonJsonDecoder;
import win.minaandyyh.sparrow.jackson.JacksonJsonEncoder;
import win.minaandyyh.sparrow.okhttp.OkHttpRequestHandler;

import java.net.http.HttpClient;

/**
 * Sparrow autoconfiguration
 *
 * @author masteryyh
 */
@Configuration
@ConditionalOnClass(Sparrow.class)
@EnableConfigurationProperties(SparrowProperties.class)
@AutoConfigureAfter(DefaultMechanismAutoConfiguration.class)
public class SparrowAutoConfiguration {
    private final SparrowProperties properties;

    public SparrowAutoConfiguration(SparrowProperties properties) {
        this.properties = properties;
    }

    private HttpClient jdkHttpClient;

    private ObjectMapper objectMapper;

    @Autowired(required = false)
    public void setJdkHttpClient(HttpClient jdkHttpClient) {
        this.jdkHttpClient = jdkHttpClient;
    }

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestHandler requestHandler() {
        if (properties.getHandler() == SparrowProperties.RequestHandlerType.HTTP_CLIENT_NEW) {
            if (jdkHttpClient == null) {
                return JDKHttpClientRequestHandler.defaultHandler();
            }
            return JDKHttpClientRequestHandler.builder()
                    .client(jdkHttpClient)
                    .build();
        }

        return OkHttpRequestHandler.defaultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public Encoder encoder() {
        if (properties.getEncoder() == SparrowProperties.EncoderType.STRING) {
            return new StringEncoder();
        }

        if (objectMapper != null) {
            return new JacksonJsonEncoder(objectMapper);
        }
        return new JacksonJsonEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public Decoder decoder() {
        if (properties.getDecoder() == SparrowProperties.DecoderType.STRING) {
            return new StringDecoder();
        }

        if (objectMapper != null) {
            return new JacksonJsonDecoder(objectMapper);
        }
        return new JacksonJsonDecoder();
    }
}
