package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class EditDialogController implements Initializable {
    @FXML
    private TextField txtTime;
    @FXML
    private DatePicker currentDate;
    @FXML
    private TextArea txtAdditionalInformation;
    @FXML
    private ChoiceBox<String> chbCountryCode;
    @FXML
    private TextField txtCarId, txtPhoneNum, txtConsignee, txtDecId;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;

    private InOut inOut;
    private Carriage currentCarriage;
    private CarriagesList carriagesList;

    private Stage currentStage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        inOut = new InOut();
        chbCountryCode.setItems(FXCollections.observableArrayList("+380", "+373"));
        chbCountryCode.getSelectionModel().selectFirst();
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
        initListeners();
    }

    private void initListeners() {
        txtPhoneNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && !newValue.matches(
                    "[1-9]?|" +
                            "\\d{2}\\s?|" +
                            "\\d{2}\\s\\d{1,3}|" +
                            "\\d{2}\\s\\d{3}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{1,2}|" +
                            "\\d{2}\\s\\d{3}\\s\\d{2}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{1,2}|" +
                            "\\d{10}")) {
                txtPhoneNum.setText(oldValue);
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && !newValue.matches(
                    "[1-9]?|" +
                            "\\d{2}\\s?|" +
                            "\\d{2}\\s\\d{1,3}|" +
                            "\\d{2}\\s\\d{3}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{1,3}|" +
                            "\\d{9}")) {
                txtPhoneNum.setText(oldValue);
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches("^\\d{9}$")) {
                txtPhoneNum.setText(newValue.substring(0, 2) + " " +
                        newValue.substring(2, 5) + " " +
                        newValue.substring(5, 7) + " " +
                        newValue.substring(7, 9));
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches("^[0\\s]\\d{9}$")) {
                txtPhoneNum.setText(newValue.substring(1, 3) + " " +
                        newValue.substring(3, 6) + " " +
                        newValue.substring(6, 8) + " " +
                        newValue.substring(8, 10));
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches("^\\d{8}$")) {
                txtPhoneNum.setText(newValue.substring(0, 2) + " " +
                        newValue.substring(2, 5) + " " +
                        newValue.substring(5, 8));
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches("^[0\\s]\\d{8}$")) {
                txtPhoneNum.setText(newValue.substring(1, 3) + " " +
                        newValue.substring(3, 6) + " " +
                        newValue.substring(6, 9));
            }

            if (newValue.matches("^\\+380\\d{9}$")) {
                chbCountryCode.getSelectionModel().select("+380");
                txtPhoneNum.setText(newValue.substring(4, 6) + " " +
                        newValue.substring(6, 9) + " " +
                        newValue.substring(9, 11) + " " +
                        newValue.substring(11, 13));
            }

            if (newValue.matches("^\\+373\\d{8}$")) {
                chbCountryCode.getSelectionModel().select("+373");
                txtPhoneNum.setText(newValue.substring(4, 6) + " " +
                        newValue.substring(6, 9) + " " +
                        newValue.substring(9, 12));
            }

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 2
                    || oldValue.length() == 6
                    || oldValue.length() == 9))
                txtPhoneNum.setText(newValue.replace(oldValue, oldValue + " "));

            if (chbCountryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 2
                    || oldValue.length() == 6))
                txtPhoneNum.setText(newValue.replace(oldValue, oldValue + " "));
        });

        txtTime.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(
                    "|" +
                            "^[0-2]|" +
                            "^[0,1][0-9]|" +
                            "^2[0-3]|" +
                            "^(([0,1][0-9])|(2[0-3])):|" +
                            "^(([0,1][0-9])|(2[0-3])):[0-5]|" +
                            "^(([0,1][0-9])|(2[0-3])):[0-5][0-9]$"))
                txtTime.setText(oldValue);

            if (newValue.matches("^(([0,1][0-9])|(2[0-3]))") && newValue.length() > oldValue.length())
                txtTime.setText(newValue + ":");
        });

        txtConsignee.textProperty().addListener((observable, oldValue, newValue) -> {
            LinkedList<String> matches = new LinkedList<>();
            for (Carriage carriage : carriagesList.getCarriages()) {
                if (!newValue.equals("")
                        && carriage.getConsignee().toLowerCase().startsWith(newValue.toLowerCase())
                        && newValue.length() > oldValue.length()
                        && !matches.contains(carriage.getConsignee().split(",")[0])) {
                    matches.add(carriage.getConsignee());
                }
            }
            if (matches.size() == 1) {
                txtConsignee.setText(matches.get(0));
            }
        });
    }

    public void execute() {
        if (txtCarId.getText().isEmpty()) {
            PopUp.showAlert(bundle.getString("report.carNumberError"));
            return;
        }

        String pattern = "^$|^(\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{2})$|^(\\d{2}\\s\\d{3}\\s\\d{3})$";
        String phone = txtPhoneNum.getText();

        if (!phone.matches(pattern)) {
            PopUp.showAlert(bundle.getString("report.phoneNumberError"));
            return;
        }

        Broker broker;
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else {
            PopUp.showAlert(bundle.getString("report.emptyBroker"));
            return;
        }

        if (txtTime.getText().length() < 5) {
            PopUp.showAlert(bundle.getString("report.incorrectTime"));
            return;
        }

        String dateTime = currentDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + txtTime.getText();

        boolean isNew = false;
        if (currentCarriage == null) {
            currentCarriage = new Carriage();
            isNew = true;
        }

        currentCarriage.setCarNumber(txtCarId.getText().replaceAll("[ -]", "").toUpperCase());
        currentCarriage.setPhoneNumber(
                txtPhoneNum.getText().length() > 5
                        ? chbCountryCode.getSelectionModel().getSelectedItem() + " " + txtPhoneNum.getText()
                        : ""
        );
        currentCarriage.setConsignee(txtConsignee.getText());
        currentCarriage.setBroker(broker);
        currentCarriage.setDeclarationId(txtDecId.getText());
        currentCarriage.setAdditionalInformation(txtAdditionalInformation.getText());
        currentCarriage.setDate(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        if (carriagesList.getSize() > 0 && currentCarriage.getDate().getYear() != carriagesList.getCarriages().get(0).getDate().getYear()) {
            carriagesList.delete(currentCarriage);
            inOut.write(carriagesList, carriagesList.getCarriages().get(0).getDate().getYear());
            carriagesList = inOut.read(currentCarriage.getDate().getYear());
            carriagesList.add(currentCarriage);
        }

        if (isNew)
            carriagesList.add(currentCarriage);

        carriagesList.getCarriages().sort((o1, o2) -> {
            if (o1.getDate().isAfter(o2.getDate()))
                return 1;
            else if (o1.getDate().isBefore(o2.getDate()))
                return -1;
            else return 0;
        });

        inOut.write(carriagesList, currentCarriage.getDate().getYear());
        clearFields();
        currentStage.hide();
    }

    void clearFields() {
        txtCarId.clear();
        chbCountryCode.getSelectionModel().selectFirst();
        txtPhoneNum.clear();
        txtConsignee.clear();
        txtDecId.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
        txtAdditionalInformation.clear();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        currentDate.setValue(LocalDate.now());
        txtTime.setText(time);
        currentCarriage = null;
    }

    private void fillFields() {
        txtCarId.setText(currentCarriage.getCarNumber());
        chbCountryCode.getSelectionModel().select(
                currentCarriage.getPhoneNumber().isEmpty()
                        ? "+380"
                        : currentCarriage.getPhoneNumber().substring(0, 4));
        txtPhoneNum.setText(
                currentCarriage.getPhoneNumber().isEmpty()
                        ? ""
                        : currentCarriage.getPhoneNumber().substring(5));
        txtConsignee.setText(currentCarriage.getConsignee());
        txtDecId.setText(currentCarriage.getDeclarationId());
        rdbPolitrans.setSelected(currentCarriage.getBroker().equals(Broker.POLITRANS));
        rdbExim.setSelected(currentCarriage.getBroker().equals(Broker.EXIM));
        txtAdditionalInformation.setText(currentCarriage.getAdditionalInformation());
        currentDate.setValue(currentCarriage.getDate().toLocalDate());
        String time = currentCarriage.getDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        txtTime.setText(time);
    }

    public void findPhone() {
        for (Carriage carriage : carriagesList.getCarriages()) {
            if (carriage.getCarNumber().equalsIgnoreCase(txtCarId.getText().replaceAll("[ -]", ""))
                    && !carriage.getPhoneNumber().equals("")) {
                chbCountryCode.getSelectionModel().select(carriage.getPhoneNumber().substring(0, 4));
                txtPhoneNum.setText(carriage.getPhoneNumber().substring(5));
            }
        }
    }

    void setCurrentCarriage(Carriage currentCarriage) {
        this.currentCarriage = currentCarriage;
        fillFields();
    }

    void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    void setCarriages(CarriagesList carriagesList) {
        this.carriagesList = carriagesList;
    }
}
