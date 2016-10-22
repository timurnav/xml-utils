package com.jquartz.utils.xpath;

import com.google.common.io.Resources;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.io.InputStream;
import java.util.stream.IntStream;

/**
 * @author timurnav
 */
public class XPathProcessorTest {

    @Test
    public void xPathTest() throws Exception {
        try (InputStream is = Resources.getResource(XPathProcessorTest.class, "/payload.xml").openStream()) {
            NodeList nodes = new XPathProcessor(is)
                    .evaluate(
                            XPathProcessor.getExpression("//*[name()='City']/text()")
                            , XPathConstants.NODESET);
            IntStream.range(0, nodes.getLength())
                    .mapToObj(nodes::item)
                    .map(Node::getNodeValue)
                    .forEach(System.out::println);
        }
    }
}