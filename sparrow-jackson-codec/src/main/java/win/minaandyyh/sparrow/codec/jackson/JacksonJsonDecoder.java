package win.minaandyyh.sparrow.codec.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import win.minaandyyh.sparrow.codec.Decoder;

/**
 * JSON body decoder based on Jackson
 *
 * @author masteryyh
 */
public class JacksonJsonDecoder implements Decoder {
    private final ObjectMapper mapper;

    public JacksonJsonDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonDecoder() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public Object decode(String body, Class<?> clazz) throws Exception {
        return mapper.readValue(body, clazz);
    }
}
