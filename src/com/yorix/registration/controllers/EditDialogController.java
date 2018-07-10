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
import java.util.ResourceBundle;

public class EditDialogController implements Initializable {
    @FXML
    private TextField txtTime;
    @FXML
    private DatePicker currentDate;
    @FXML
    private TextArea txtAdditionalInformation;
    @FXML
    private ChoiceBox<String> countryCode;
    @FXML
    private TextField carId, phoneNum, consignee, decId;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;

    private Carriage currentCarriage;
    private CarriagesList carriagesList;

    private Stage currentStage;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        countryCode.setItems(FXCollections.observableArrayList("+380", "+373"));
        countryCode.getSelectionModel().selectFirst();
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
        initListeners();
    }

    private void initListeners() {
        phoneNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (countryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && !newValue.matches(
                    "[1-9]?|" +
                            "\\d{2}\\s?|" +
                            "\\d{2}\\s\\d{1,3}|" +
                            "\\d{2}\\s\\d{3}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{1,2}|" +
                            "\\d{2}\\s\\d{3}\\s\\d{2}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{1,2}|" +
                            "\\d{10}")) {
                phoneNum.setText(oldValue);
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && !newValue.matches(
                    "[1-9]?|" +
                            "\\d{2}\\s?|" +
                            "\\d{2}\\s\\d{1,3}|" +
                            "\\d{2}\\s\\d{3}\\s|" +
                            "\\d{2}\\s\\d{3}\\s\\d{1,3}|" +
                            "\\d{9}")) {
                phoneNum.setText(oldValue);
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches("^\\d{9}")) {
                phoneNum.setText(newValue.substring(0, 2) + " " +
                        newValue.substring(2, 5) + " " +
                        newValue.substring(5, 7) + " " +
                        newValue.substring(7, 9));
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches("^0\\d{9}")) {
                phoneNum.setText(newValue.substring(1, 3) + " " +
                        newValue.substring(3, 6) + " " +
                        newValue.substring(6, 8) + " " +
                        newValue.substring(8, 10));
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches("^\\d{8}")) {
                phoneNum.setText(newValue.substring(0, 2) + " " +
                        newValue.substring(2, 5) + " " +
                        newValue.substring(5, 8));
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches("^0\\d{8}")) {
                phoneNum.setText(newValue.substring(1, 3) + " " +
                        newValue.substring(3, 6) + " " +
                        newValue.substring(6, 9));
            }

            if (newValue.matches("^\\+380\\d{9}")) {
                countryCode.getSelectionModel().select("+380");
                phoneNum.setText(newValue.substring(4, 6) + " " +
                        newValue.substring(6, 9) + " " +
                        newValue.substring(9, 11) + " " +
                        newValue.substring(11, 13));
            }

            if (newValue.matches("^\\+373\\d{8}")) {
                countryCode.getSelectionModel().select("+373");
                phoneNum.setText(newValue.substring(4, 6) + " " +
                        newValue.substring(6, 9) + " " +
                        newValue.substring(9, 12));
            }

            if (countryCode.getSelectionModel().getSelectedItem().equals("+380")
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 2
                    || oldValue.length() == 6
                    || oldValue.length() == 9))
                phoneNum.setText(newValue.replace(oldValue, oldValue + " "));

            if (countryCode.getSelectionModel().getSelectedItem().equals("+373")
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 2
                    || oldValue.length() == 6))
                phoneNum.setText(newValue.replace(oldValue, oldValue + " "));
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
    }

    public void execute() {
        if (carId.getText().isEmpty()) {
            showPopup(bundle.getString("report.carNumberError"));
            return;
        }

        String pattern = "^$|^(\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{2})$|^(\\d{2}\\s\\d{3}\\s\\d{3})$";
        String phone = phoneNum.getText();

        if (!phone.matches(pattern)) {
            showPopup(bundle.getString("report.phoneNumberError"));
            return;
        }

        Broker broker;
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else {
            showPopup(bundle.getString("report.emptyBroker"));
            return;
        }

        if (txtTime.getText().length() < 5) {
            showPopup(bundle.getString("report.incorrectTime"));
            return;
        }

        String dateTime = currentDate.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + txtTime.getText();
        if (currentCarriage != null) {
            currentCarriage.setCarNumber(carId.getText());
            currentCarriage.setPhoneNumber(
                    phoneNum.getText().length() > 5
                            ? countryCode.getSelectionModel().getSelectedItem() + " " + phoneNum.getText()
                            : ""
            );
            currentCarriage.setConsignee(consignee.getText());
            currentCarriage.setBroker(broker);
            currentCarriage.setDeclarationId(decId.getText());
            currentCarriage.setAdditionalInformation(txtAdditionalInformation.getText());
            currentCarriage.setDate(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        } else {
            carriagesList.add(
                    new Carriage(
                            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            carId.getText(),
                            countryCode.getSelectionModel().getSelectedItem() + " " + phoneNum.getText(),
                            consignee.getText(),
                            broker,
                            decId.getText(),
                            txtAdditionalInformation.getText()
                    )
            );
        }

        InOut.write(carriagesList);
        clearFields();
        currentStage.hide();
    }

    private void showPopup(String message) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(message);
        dialog.setHeaderText(message);
        dialog.show();
    }

    void clearFields() {
        carId.clear();
        countryCode.getSelectionModel().selectFirst();
        phoneNum.clear();
        consignee.clear();
        decId.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
        txtAdditionalInformation.clear();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        currentDate.setValue(LocalDate.now());
        txtTime.setText(time);
        currentCarriage = null;
    }

    private void fillFields() {
        carId.setText(currentCarriage.getCarNumber());
        countryCode.getSelectionModel().select(
                currentCarriage.getPhoneNumber().isEmpty()
                        ? "+380"
                        : currentCarriage.getPhoneNumber().substring(0, 4));
        phoneNum.setText(
                currentCarriage.getPhoneNumber().isEmpty()
                        ? ""
                        : currentCarriage.getPhoneNumber().substring(5));
        consignee.setText(currentCarriage.getConsignee());
        decId.setText(currentCarriage.getDeclarationId());
        rdbPolitrans.setSelected(currentCarriage.getBroker().equals(Broker.POLITRANS));
        rdbExim.setSelected(currentCarriage.getBroker().equals(Broker.EXIM));
        txtAdditionalInformation.setText(currentCarriage.getAdditionalInformation());
        currentDate.setValue(currentCarriage.getDate().toLocalDate());
        String time = currentCarriage.getDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        txtTime.setText(time);
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
