package com.yorix.registration.io;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InOut {
    private String dataPath;
    private String dataAltPath;
    private String reportOutPath;

    public InOut() {
        String fileSeparator = System.getProperty("file.separator");
        dataPath = "C:/ProgramData/Registration/";
        dataAltPath = System.getProperty("user.home") + fileSeparator + "Documents" + fileSeparator + "Registration" + fileSeparator;
        reportOutPath = System.getProperty("user.home") + fileSeparator + "Desktop" + fileSeparator + "REPORT.csv";

        File altFolder = new File((dataPath));
        if (!altFolder.exists())
            altFolder.mkdir();

        File folder = new File(dataAltPath);
        if (!folder.exists())
            folder.mkdir();
    }


    public CarriagesList read(int year) {
        CarriagesList carriages;
        carriages = readCsv(dataPath + year + "_carriagesList.csv");
        if (carriages == null)
            carriages = readCsv(dataAltPath +  year + "_carriagesList.csv");
        if (carriages == null)
            carriages = new CarriagesList();
        return carriages;
    }

    private CarriagesList readCsv(String path) {
        CarriagesList carriages;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            carriages = new CarriagesList();
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                Carriage carriage = new Carriage();
                String[] elements = currentLine.split("\\|", 7);

                carriage.setDate(LocalDateTime.parse(elements[0], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                carriage.setCarNumber(elements[1]);
                carriage.setPhoneNumber(elements[2]);
                carriage.setConsignee(elements[3]);
                carriage.setBroker(Broker.valueOf(elements[4]));
                carriage.setDeclarationId(elements[5]);
                carriage.setAdditionalInformation(elements[6]);
                carriages.add(carriage);
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            carriages = null;
        }
        return carriages;
    }

    public void write(CarriagesList carriages, int year) {
        writeCsv(carriages, dataPath +  year + "_carriagesList.csv");

        CarriagesList carriagesAlt = readCsv(dataAltPath +  year + "_carriagesList.csv");
        if (carriagesAlt == null || carriages.getCarriages().size() >= carriagesAlt.getCarriages().size())
            writeCsv(carriages, dataAltPath +  year + "_carriagesList.csv");
    }

    private void writeCsv(CarriagesList carriages, String path) {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            for (Carriage carriage : carriages.getCarriages()) {
                outputStream.write((carriage.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getCarNumber() + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getPhoneNumber() + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getConsignee() + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getBroker() + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getDeclarationId() + "|").getBytes("Cp1251"));
                outputStream.write((carriage.getAdditionalInformation() + "\n").getBytes("Cp1251"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createReport(
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
        builder.append("\nTotal: ;").append(carriages.size());

        try (OutputStream os = new FileOutputStream(reportOutPath)) {
            os.write(builder.toString().getBytes("Cp1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
