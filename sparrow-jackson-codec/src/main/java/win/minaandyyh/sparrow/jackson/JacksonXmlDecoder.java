package win.minaandyyh.sparrow.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import win.minaandyyh.sparrow.codec.Decoder;

/**
 * XML body decoder based on Jackson
 *
 * @author masteryyh
 */
public class JacksonXmlDecoder implements Decoder {
    private final XmlMapper mapper;

    public JacksonXmlDecoder(XmlMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonXmlDecoder() {
        this.mapper = new XmlMapper();
    }

    @Override
    public Object decode(String body, Class<?> clazz) throws Exception {
        return mapper.readValue(body, clazz);
    }
}
