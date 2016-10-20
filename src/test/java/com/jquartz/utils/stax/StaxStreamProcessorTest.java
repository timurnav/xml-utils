package com.jquartz.utils.stax;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import static org.junit.Assert.*;

/**
 * @author timurnav
 *         on 21.10.2016.
 */
public class StaxStreamProcessorTest {

    @Test
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(
                Resources.getResource(StaxStreamProcessor.class, "/payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("City".equals(reader.getLocalName())) {
                        System.out.println(reader.getElementText());
                    }
                }
            }
        }
    }

    @Test
    public void readCity() throws Exception {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(
                Resources.getResource(StaxStreamProcessor.class, "/payload.xml").openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }
}