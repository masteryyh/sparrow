package win.minaandyyh.sparrow.codec.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import win.minaandyyh.sparrow.codec.Encoder;

/**
 * JSON body encoder based on Jackson
 *
 * @author masteryyh
 */
public class JacksonJsonEncoder implements Encoder {
    private final ObjectMapper mapper;

    public JacksonJsonEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonEncoder() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public String encode(Object body, Class<?> clazz) throws Exception {
        return mapper.writeValueAsString(body);
    }
}
