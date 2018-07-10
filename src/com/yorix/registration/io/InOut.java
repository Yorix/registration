package com.yorix.registration.io;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;

import javax.xml.stream.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InOut {
    private static String[] pathes;

    static {
        try {
            pathes = ConfigReader.read();
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static String dataPath = pathes[0];
    private static String dataAltPath = pathes[1];
    private static String reportOutPath = pathes[2];
    private static String reportOutPathAlt = pathes[3];


    public static CarriagesList read() {
        CarriagesList carriages;

        carriages = readXml(dataPath);
        if (carriages == null)
            carriages = readXml(dataAltPath);
        if (carriages == null)
            carriages = new CarriagesList();

        return carriages;
    }

    private static CarriagesList readXml(String path) {
        CarriagesList carriages = new CarriagesList();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(path), "UTF-8");

            Carriage carriage = new Carriage();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "date":
                            carriage.setDate(LocalDateTime.parse(reader.getElementText(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                            break;
                        case "carNum":
                            carriage.setCarNumber(reader.getElementText());
                            break;
                        case "phoneNum":
                            carriage.setPhoneNumber(reader.getElementText());
                            break;
                        case "consignee":
                            carriage.setConsignee(reader.getElementText());
                            break;
                        case "broker":
                            carriage.setBroker(Broker.valueOf(reader.getElementText()));
                            break;
                        case "decId":
                            carriage.setDeclarationId(reader.getElementText());
                            break;
                        case "addInfo":
                            carriage.setAdditionalInformation(reader.getElementText());
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("carriage")) {
                    carriages.add(carriage);
                    carriage = new Carriage();
                }
            }

        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }

        return carriages;
    }

    public static void write(CarriagesList carriages) {
        writeXml(carriages, dataPath);

        CarriagesList carriagesAlt = readXml(dataAltPath);
        if (carriagesAlt == null || carriages.getCarriages().size() >= carriagesAlt.getCarriages().size())
            writeXml(carriages, dataAltPath);
    }

    private static void writeXml(CarriagesList carriages, String path) {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = null;
        try {
            writer = factory.createXMLStreamWriter(new FileOutputStream(path), "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement("carriages");

            for (Carriage carriage : carriages.getCarriages()) {
                writer.writeCharacters("\n\t");
                writer.writeStartElement("carriage");

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("date");
                writer.writeCharacters(carriage.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("carNum");
                writer.writeCharacters(carriage.getCarNumber());
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("phoneNum");
                writer.writeCharacters(carriage.getPhoneNumber());
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("consignee");
                writer.writeCharacters(carriage.getConsignee());
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("broker");
                writer.writeCharacters(carriage.getBroker().toString());
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("decId");
                writer.writeCharacters(carriage.getDeclarationId());
                writer.writeEndElement();

                writer.writeCharacters("\n\t\t");
                writer.writeStartElement("addInfo");
                writer.writeCharacters(carriage.getAdditionalInformation());
                writer.writeEndElement();

                writer.writeCharacters("\n\t");
                writer.writeEndElement();
            }

            writer.writeCharacters("\n");
            writer.writeEndElement();
            writer.writeEndDocument();


        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createReport(
            List<Carriage> carriages,
            boolean date,
            boolean carNumber,
            boolean phoneNumber,
            boolean consignee,
            boolean broker,
            boolean declarationId,
            boolean additionalInformation) {
        StringBuilder builder = new StringBuilder();
        carriages.forEach(carriage -> {
            if (date)
                builder.append(carriage.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append(";");
            if (carNumber) builder.append(carriage.getCarNumber()).append(";");
            if (phoneNumber) builder.append(carriage.getPhoneNumber()).append(";");
            if (consignee) builder.append(carriage.getConsignee()).append(";");
            if (broker) builder.append(carriage.getBroker().toString()).append(";");
            if (declarationId) builder.append(carriage.getDeclarationId()).append(";");
            if (additionalInformation) builder.append(carriage.getAdditionalInformation()).append(";");
            if (builder.length() > 0) builder.append("\n");
        });
        builder.append("Всего: ;").append(carriages.size());

        try (OutputStream os = new FileOutputStream(reportOutPath)) {
            os.write(builder.toString().getBytes("Cp1251"));
        } catch (IOException e) {
            try (OutputStream os = new FileOutputStream(reportOutPathAlt)) {
                os.write(builder.toString().getBytes("Cp1251"));
            } catch (IOException e1) {
                e.printStackTrace();
                e1.printStackTrace();
            }
        }
    }
}
