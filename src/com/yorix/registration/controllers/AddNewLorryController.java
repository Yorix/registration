package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Lorry;
import com.yorix.registration.io.Reader;
import com.yorix.registration.io.Writer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AddNewLorryController implements Initializable {
    @FXML
    private TextField carId, phoneNum, consignee;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;
    private Broker broker;
    private Stage currentStage;

    private ObservableList<Lorry> lorries;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
    }

    public void addNewLorry(ActionEvent actionEvent) {
        if (rdbPolitrans.isSelected()) broker = Broker.POLITRANS;
        else if (rdbExim.isSelected()) broker = Broker.EXIM;
        else return; //TODO добавить требование выбора фирмы

        if (carId.getText().isEmpty()) return; //TODO добавить требование ввода номера машины
        if (phoneNum.getText().length() < 10) return; //TODO добавить сообщение о неверном номере телефона

        lorries.add(new Lorry(carId.getText(), phoneNum.getText(), consignee.getText(), broker));
        Writer.write(lorries);
        System.out.println(lorries); //TODO удалить

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

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setLorries(ObservableList<Lorry> lorries) {
        this.lorries = lorries;
    }
}
