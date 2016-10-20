package com.jquartz.utils.jaxb;

import com.google.common.io.Resources;
import com.jquartz.schema.CityType;
import com.jquartz.schema.ObjectFactory;
import com.jquartz.schema.Payload;
import org.junit.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 * @author timurnav
 *         on 29.09.2016.
 */
public class JaxbParserTest {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void testPayload() throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(Resources.getResource(JaxbParserTest.class, "/payload.xml").openStream());
        String stringifiedPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(stringifiedPayload);
        System.out.println(stringifiedPayload);
    }

    @Test
    public void testCity() throws Exception {
        JAXBElement<CityType> cityElementUnmarshal = JAXB_PARSER.unmarshal(Resources.getResource(JaxbParserTest.class, "/city.xml").openStream());
        CityType city = cityElementUnmarshal.getValue();
        JAXBElement<CityType> cityElementToMarshal = new JAXBElement<>(new QName("http://javaops.ru", "City"), CityType.class, city);
        String stringifiedCity = JAXB_PARSER.marshal(cityElementToMarshal);
        JAXB_PARSER.validate(stringifiedCity);
        System.out.println(stringifiedCity);
    }
}