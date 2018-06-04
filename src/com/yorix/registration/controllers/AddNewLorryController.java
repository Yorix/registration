package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.LorriesList;
import com.yorix.registration.Lorry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddNewLorryController implements Initializable {
    @FXML
    private TextField carId, phoneNum, consignee;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;

    private MainController mainController;

    private Broker broker;
    private Stage currentStage;
    private LorriesList lorries;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
    }

    public void addNewLorry(ActionEvent actionEvent) {
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else return; //todo добавить требование выбора фирмы


        if (phoneNum.getText().length() < 10) return; //todo добавить сообщение о неверном номере телефона
        if (carId.getText().isEmpty()) return; //todo добавить требование ввода номера машины
        for (Lorry lorry : lorries.getLorries()) {
            if (lorry.getIdNumber().equals(carId.getText())) {
                mainController.openNote(lorry);
                clearFields();
                currentStage.hide();
                return;
            }
        }

        lorries.add(new Lorry(carId.getText(), phoneNum.getText(), consignee.getText(), broker));

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

    public void setLorries(LorriesList lorries) {
        this.lorries = lorries;
    }
}
