package com.yorix.registration.io;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConfigReader {

    public static String[] read() throws FileNotFoundException, XMLStreamException {
        String[] pathes = new String[4];

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream("config.xml"));

        while (reader.hasNext()) {
            if (reader.next() == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "dataPath":
                        pathes[0] = reader.getElementText();
                        break;
                    case "dataAltPath":
                        pathes[1] = reader.getElementText();
                        break;
                    case "reportOutPath":
                        pathes[2] = reader.getElementText();
                        break;
                    case "reportOutAltPath":
                        pathes[3] = reader.getElementText();
                }
            }
        }
        return pathes;
    }
}
