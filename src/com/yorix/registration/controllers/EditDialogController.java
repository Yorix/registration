package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.event.ActionEvent;
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
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
        initListeners();
    }

    private void initListeners() {
        phoneNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(
                    "\\+380\\s?|" +
                            "\\+380\\s\\d|" +
                            "\\+380\\s\\d{2}\\s?|" +
                            "\\+380\\s\\d{2}\\s\\d{1,3}|" +
                            "\\+380\\s\\d{2}\\s\\d{3}\\s?|" +
                            "\\+380\\s\\d{2}\\s\\d{3}\\s\\d|" +
                            "\\+380\\s\\d{2}\\s\\d{3}\\s\\d{2}\\s?|" +
                            "\\+380\\s\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{1,2}|" +
                            "\\+380\\d{9,10}"))
                phoneNum.setText(oldValue);

            if (newValue.matches("\\+380\\d{9}")) {
                phoneNum.setText(newValue.substring(0, 4) + " " +
                        newValue.substring(4, 6) + " " +
                        newValue.substring(6, 9) + " " +
                        newValue.substring(9, 11) + " " +
                        newValue.substring(11, 13));
            }

            if (newValue.matches("\\+3800\\d{9}")) {
                phoneNum.setText(newValue.substring(0, 4) + " " +
                        newValue.substring(5, 7) + " " +
                        newValue.substring(7, 10) + " " +
                        newValue.substring(10, 12) + " " +
                        newValue.substring(12, 14));
            }

            if (newValue.matches("\\+380\\+380\\d{9}")) {
                phoneNum.setText(newValue.substring(4, 8) + " " +
                        newValue.substring(8, 10) + " " +
                        newValue.substring(10, 13) + " " +
                        newValue.substring(13, 15) + " " +
                        newValue.substring(15, 17));
            }

            if (newValue.startsWith(oldValue)
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 4
                    || oldValue.length() == 7
                    || oldValue.length() == 11
                    || oldValue.length() == 14))
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

    public void execute(ActionEvent actionEvent) {
        if (carId.getText().isEmpty()) {
            showPopup(bundle.getString("report.carNumberError"));
            return;
        }

        String pattern = "\\+380\\s\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{2}|\\+380";
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
            currentCarriage.setPhoneNumber(phoneNum.getText().length() > 5 ? phoneNum.getText() : "");
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
                            phoneNum.getText(),
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

    public void clearFields() {
        carId.clear();
        phoneNum.setText("+380");
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

    public void fillFields() {
        carId.setText(currentCarriage.getCarNumber());
        phoneNum.setText(currentCarriage.getPhoneNumber());
        consignee.setText(currentCarriage.getConsignee());
        decId.setText(currentCarriage.getDeclarationId());
        rdbPolitrans.setSelected(currentCarriage.getBroker().equals(Broker.POLITRANS));
        rdbExim.setSelected(currentCarriage.getBroker().equals(Broker.EXIM));
        txtAdditionalInformation.setText(currentCarriage.getAdditionalInformation());
        currentDate.setValue(currentCarriage.getDate().toLocalDate());
        String time = currentCarriage.getDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        txtTime.setText(time);
    }

    public void setCurrentCarriage(Carriage currentCarriage) {
        this.currentCarriage = currentCarriage;
        fillFields();
    }

    public void setMainController(MainController mainController) {
        MainController mainController1 = mainController;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setCarriages(CarriagesList carriagesList) {
        this.carriagesList = carriagesList;
    }

}
