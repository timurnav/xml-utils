package com.jquartz.utils.xslt;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author timurnav
 */
public class XsltProcessorTest {

    @Test
    public void xslTransformTest() throws Exception {
        try (InputStream xslInputStream = Resources.getResource(XsltProcessorTest.class, "/cities.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource(XsltProcessorTest.class, "/payload.xml").openStream()){

            XsltProcessor xsltProcessor = new XsltProcessor(xslInputStream);

            System.out.println(xsltProcessor.transform(xmlInputStream));
        }
    }
}