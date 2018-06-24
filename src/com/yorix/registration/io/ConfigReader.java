package com.yorix.registration.io;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class ConfigReader {

    public static String[] read() {
        String[] pathes = new String[3];
        DefaultHandler handler = new DefaultHandler() {
            boolean dataPath;
            boolean reportOutPath;
            boolean reportOutAltPath;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("dataPath"))
                    dataPath = true;
                if (qName.equals("reportOutPath"))
                    reportOutPath = true;
                if (qName.equals("reportOutAltPath"))
                    reportOutAltPath = true;
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (dataPath) {
                    dataPath = false;
                    pathes[0] = new String(ch, start, length);
                }
                if (reportOutPath) {
                    reportOutPath = false;
                    pathes[1] = new String(ch, start, length);
                }
                if (reportOutAltPath) {
                    reportOutAltPath = false;
                    pathes[2] = new String(ch, start, length);
                }
            }
        };

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse("config.xml", handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return pathes;
    }
}
