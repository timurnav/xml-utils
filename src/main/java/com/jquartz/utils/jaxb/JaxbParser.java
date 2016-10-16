package com.jquartz.utils.jaxb;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.*;


/**
 * XML parsing Facade
 * @author timurnav
 *         on 29.09.2016.
 */
public class JaxbParser {

    protected JaxbMarshaller jaxbMarshaller;
    protected JaxbUnmarshaller jaxbUnmarshaller;
    protected Schema schema;

    public JaxbParser(Class... classesToBeBound) {
        try {
            init(JAXBContext.newInstance(classesToBeBound));
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //    http://stackoverflow.com/questions/30643802/what-is-jaxbcontext-newinstancestring-contextpath
    public JaxbParser(String context) {
        try {
            init(JAXBContext.newInstance(context));
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void init(JAXBContext ctx) throws JAXBException {
        jaxbMarshaller = new JaxbMarshaller(ctx);
        jaxbUnmarshaller = new JaxbUnmarshaller(ctx);
    }

    // Unmarshaller
    public <T> T unmarshal(InputStream is) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(is);
    }

    public <T> T unmarshal(Reader reader) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(reader);
    }

    public <T> T unmarshal(String str) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(str);
    }

    // Marshaller
    public void setMarshallerProperty(String prop, Object value) {
        try {
            jaxbMarshaller.setProperty(prop, value);
        } catch (PropertyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public synchronized String marshal(Object instance) throws JAXBException {
        return jaxbMarshaller.marshal(instance);
    }

    public void marshal(Object instance, Writer writer) throws JAXBException {
        jaxbMarshaller.marshal(instance, writer);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        jaxbUnmarshaller.setSchema(schema);
        jaxbMarshaller.setSchema(schema);
    }

    public void validate(String str) throws IOException, SAXException {
        validate(new StringReader(str));
    }

    public void validate(Reader reader) throws IOException, SAXException {
        schema.newValidator().validate(new StreamSource(reader));
    }
}