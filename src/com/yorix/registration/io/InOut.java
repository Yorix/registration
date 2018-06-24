package com.yorix.registration.io;

import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InOut {
    private static String dataPath = ConfigReader.read()[0];
    private static String reportOutPath = ConfigReader.read()[1];
    private static String reportOutPathAlt = ConfigReader.read()[2];

    public static CarriagesList read() {
        CarriagesList carriages;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataPath))) {
            carriages = (CarriagesList) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            carriages = new CarriagesList(true);
            write(carriages);
        }
        return carriages;
    }

    public static void write(CarriagesList carriages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataPath))) {
            oos.writeObject(carriages);
        } catch (IOException e) {
            e.printStackTrace();
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
