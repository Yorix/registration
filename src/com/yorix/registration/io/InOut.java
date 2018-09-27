package com.yorix.registration.io;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.controllers.PopUp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class InOut {
    private String dataPath;
    private String dataAltPath;
    private String reportOutPath;
    private String sprtr;

    private ResourceBundle bundle;


    public InOut(ResourceBundle bundle) {
        this.bundle = bundle;

        sprtr = System.getProperty("file.separator");
        dataPath = "C:" + sprtr + "ProgramData" + sprtr + "Registration" + sprtr;
        dataAltPath = System.getProperty("user.home") + sprtr + "Documents" + sprtr + "Registration" + sprtr;
        reportOutPath = System.getProperty("user.home") + sprtr + "Desktop" + sprtr + "REPORT.csv";

        File folder = new File(dataPath);
        if (!folder.exists())
            folder.mkdir();

        File altFolder = new File((dataAltPath));
        if (!altFolder.exists())
            altFolder.mkdir();
    }


    public CarriagesList read(int year) {
        CarriagesList carriages;
        carriages = readCsv(dataPath + year + "_carriagesList.csv");
        if (carriages == null) {
            carriages = readCsv(dataAltPath + year + "_carriagesList.csv");
            copyBase(dataAltPath, dataPath);
        }
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
        writeCsv(carriages, dataPath + year + "_carriagesList.csv");
        writeCsv(carriages, dataAltPath + year + "_carriagesList.csv");
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

    public void copyBase(String source, String target) {

        if (source == null) source = dataPath;
        if (target == null) target = dataAltPath;

        File folder = new File(target);
        if (!folder.exists())
            folder.mkdir();

        Path pathTarget = new File(target).toPath();

        File sourceFile = new File(source);
        File[] listOfFiles = sourceFile.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.getName().matches("\\d{4}_carriagesList\\.csv")) {
                    try {
                        Files.copy(file.toPath(), pathTarget.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        PopUp.showAlert(bundle.getString("report.saveError"));
                        e.printStackTrace();
                        return;
                    }
                }
            }
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
