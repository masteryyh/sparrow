package win.minaandyyh.sparrow.springboot.autoconfigure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

/**
 * Default HTTP client and serialization mechanism configuration
 *
 * @author masteryyh
 */
@Configuration
@AutoConfigureBefore(SparrowAutoConfiguration.class)
public class DefaultMechanismAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
