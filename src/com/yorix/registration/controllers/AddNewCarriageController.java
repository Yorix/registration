package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Car;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
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

public class AddNewCarriageController implements Initializable {
    @FXML
    private TextField carId, phoneNum, consignee;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;

    private MainController mainController;
    private ResourceBundle bundle;

    private Broker broker;
    private Stage currentStage;
    private CarriagesList carriagesList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
    }

    public void addNewCarriage(ActionEvent actionEvent) {
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else return; //todo добавить требование выбора фирмы


        if (phoneNum.getText().length() < 10) return; //todo добавить сообщение о неверном номере телефона
        if (carId.getText().isEmpty()) return; //todo добавить требование ввода номера машины
        for (Carriage carriage : carriagesList.getCarriages()) {
            if (carriage.getCar().getCarNumber().equals(carId.getText())) {
                mainController.editCarriage(carriage);
                clearFields();
                currentStage.hide();
                return;
            }
        }

        carriagesList.add(new Carriage(LocalDateTime.now(), new Car(carId.getText(), phoneNum.getText()), consignee.getText(), broker));

        clearFields();
        currentStage.hide();
    }

    private void clearFields() {
        carId.clear();
        phoneNum.setText("+380");
        consignee.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setCarriages(CarriagesList carriagesList) {
        this.carriagesList = carriagesList;
    }
}
