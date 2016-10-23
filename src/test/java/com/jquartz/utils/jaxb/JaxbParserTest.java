package com.jquartz.utils.jaxb;

import com.jquartz.schema.ObjectFactory;
import com.jquartz.schema.Payload;
import com.jquartz.utils.AbstractMarshalUnmarshalTest;

import java.io.InputStream;

/**
 * @author timurnav
 */
public class JaxbParserTest extends AbstractMarshalUnmarshalTest {

    private final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Override
    protected String marshal(Payload payload) throws Exception {
        String marshaled = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(marshaled);
        return marshaled;
    }

    @Override
    protected Payload unmarshal(InputStream inputStream) throws Exception {
        return JAXB_PARSER.unmarshal(inputStream);
    }

    @Override
    protected String getFileName() {
        return "JAXB";
    }
}