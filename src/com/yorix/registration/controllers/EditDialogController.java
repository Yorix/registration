package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditDialogController implements Initializable {
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
    }

    public void execute(ActionEvent actionEvent) {
        Broker broker;
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else return; //todo добавить требование выбора фирмы


        if (phoneNum.getText().length() != 14) return; //todo добавить сообщение о неверном номере телефона
        if (carId.getText().isEmpty()) return; //todo добавить требование ввода номера машины

        if (currentCarriage != null) {
            currentCarriage.setCarNumber(carId.getText());
            currentCarriage.setPhoneNumber(phoneNum.getText());
            currentCarriage.setConsignee(consignee.getText());
            currentCarriage.setBroker(broker);
            currentCarriage.setDeclarationId(decId.getText());

        } else {
            carriagesList.add(
                    new Carriage(
                            LocalDateTime.now(),
                            carId.getText(),
                            phoneNum.getText(),
                            consignee.getText(),
                            broker,
                            decId.getText()
                    )
            );
        }

        InOut.write(carriagesList);
        clearFields();
        currentStage.hide();
    }

    private void clearFields() {
        carId.clear();
        phoneNum.setText("+380");
        consignee.clear();
        decId.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
        currentCarriage = null;
    }

    public void setCurrentCarriage(Carriage currentCarriage) {
        this.currentCarriage = currentCarriage;
        if (currentCarriage != null)
            fillFields();
        else clearFields();
    }

    private void fillFields() {
        carId.setText(currentCarriage.getCarNumber());
        phoneNum.setText(currentCarriage.getPhoneNumber());
        consignee.setText(currentCarriage.getConsignee());
        decId.setText(currentCarriage.getDeclarationId());
        rdbPolitrans.setSelected(currentCarriage.getBroker().equals(Broker.POLITRANS));
        rdbExim.setSelected(currentCarriage.getBroker().equals(Broker.EXIM));
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
