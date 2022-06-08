package win.minaandyyh.sparrow.codec.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import win.minaandyyh.sparrow.codec.Encoder;

/**
 * XML body encoder based on Jackson
 *
 * @author masteryyh
 */
public class JacksonXmlEncoder implements Encoder {
    private final XmlMapper mapper;

    public JacksonXmlEncoder(XmlMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonXmlEncoder() {
        this.mapper = new XmlMapper();
    }

    @Override
    public String encode(Object body, Class<?> clazz) throws Exception {
        return mapper.writeValueAsString(body);
    }
}
