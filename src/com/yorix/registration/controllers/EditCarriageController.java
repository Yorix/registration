package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Car;
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

public class EditCarriageController implements Initializable {
    @FXML
    private Label lblCarID;
    @FXML
    private RadioButton rdbPolitrans, rdbExim;
    @FXML
    private TableView<Carriage> tblCarriages;
    @FXML
    private TableColumn<Carriage, String> tblClmDate, tblClmConsignee, tblClmBroker;
    @FXML
    private TextField txtCarId, txtPhoneNum, txtConsignee;

    private Stage currentStage;
    private ResourceBundle bundle;

    private Carriage currentCarriage;
    private CarriagesList carriagesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
    }

    public void addNewCarriage(ActionEvent actionEvent) {
        LocalDateTime date = LocalDateTime.now();
        Car car = new Car("0000", "1234567890"); //todo реализовать добавление машины
        String consignee = txtConsignee.getText();
        if (!rdbPolitrans.isSelected() && !rdbExim.isSelected()) return; //todo предупреждение о выборе брокера
        Broker broker = rdbPolitrans.isSelected() ? Broker.POLITRANS : Broker.EXIM;

        carriagesList.add(new Carriage(date, car, consignee, broker));

        InOut.write(carriagesList);
        clearFields();
        currentStage.hide();
    }

    private void clearFields() {
        txtConsignee.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setCurrentCarriage(Carriage currentCarriage) {
        this.currentCarriage = currentCarriage;
//        fillFields(); todo раскомментировать
    }

    public void setCarriagesList(CarriagesList carriagesList) {
        this.carriagesList = carriagesList;
    }

//    private void fillFields() { todo исправить
//        txtCarId.setText(currentCarriage.getCarNumber());
//        txtPhoneNum.setText(currentCarriage.getPhoneNumber());
//
//        tblClmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
//        tblClmConsignee.setCellValueFactory(new PropertyValueFactory<>("consignee"));
//        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
//        tblCarriages.setItems(currentCarriage.getCarriages().getCarriages());
//    }
}
