package com.jquartz.utils.xslt;

import com.google.common.io.Resources;
import com.jquartz.schema.Payload;
import com.jquartz.utils.AbstractMarshalUnmarshalTest;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author timurnav
 */
public class XsltProcessorTest extends AbstractMarshalUnmarshalTest {

    @Test
    public void xslTransformTest() throws Exception {
        try (InputStream xslInputStream = Resources.getResource(XsltProcessorTest.class, "/cities.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource(XsltProcessorTest.class, "/payload.xml").openStream()) {

            XsltProcessor xsltProcessor = new XsltProcessor(xslInputStream);

            System.out.println(xsltProcessor.transform(xmlInputStream));
        }
    }

    @Override
    protected String marshal(Payload payload) throws Exception {
        return "";
    }

    @Override
    protected Payload unmarshal(InputStream inputStream) throws Exception {
        return new Payload();
    }

    @Override
    protected String getHtml(InputStream inputStream) throws Exception {
        try (InputStream xslStream = openStream("/groups.xsl")) {
            XsltProcessor processor = new XsltProcessor(xslStream);
            return processor.transform(inputStream);
        }
    }

    @Override
    protected String getFileName() {
        return "XSLT";
    }
}