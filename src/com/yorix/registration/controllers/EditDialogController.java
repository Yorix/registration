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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditDialogController implements Initializable {
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
                            "\\+380\\s\\d{2}\\s\\d{3}\\s\\d{2}\\s\\d{1,2}"))
                phoneNum.setText(oldValue);

            if (newValue.startsWith(oldValue)
                    && newValue.matches(".*\\d")
                    && (oldValue.length() == 4
                    || oldValue.length() == 7
                    || oldValue.length() == 11
                    || oldValue.length() == 14))
                phoneNum.setText(newValue.replace(oldValue, oldValue + " "));
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

        if (currentCarriage != null) {
            currentCarriage.setCarNumber(carId.getText());
            currentCarriage.setPhoneNumber(phoneNum.getText().length() > 5 ? phoneNum.getText() : "");
            currentCarriage.setConsignee(consignee.getText());
            currentCarriage.setBroker(broker);
            currentCarriage.setDeclarationId(decId.getText());
            currentCarriage.setAdditionalInformation(txtAdditionalInformation.getText());

        } else {
            carriagesList.add(
                    new Carriage(
                            LocalDateTime.now(),
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
