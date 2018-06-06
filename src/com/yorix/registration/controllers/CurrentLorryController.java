package com.yorix.registration.controllers;

import com.yorix.registration.*;
import com.yorix.registration.io.InOut;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CurrentLorryController implements Initializable {

    @FXML
    private RadioButton rdbPolitrans, rdbExim;
    @FXML
    private TableView<Carriage> tblCarriages;
    @FXML
    private TableColumn<Carriage, String> tblClmDate, tblClmConsignee, tblClmBroker;
    @FXML
    private TextField txtCarId, txtPhoneNum, txtConsignee;

    private Stage currentStage;
    private MainController mainController;
    private ResourceBundle bundle;

    private Lorry currentLorry;
    private LorriesList lorries;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        ToggleGroup brokers = new ToggleGroup();
        rdbPolitrans.setToggleGroup(brokers);
        rdbExim.setToggleGroup(brokers);
    }

    public void addNewCarriage(ActionEvent actionEvent) {
        FormattedDate date = new FormattedDate();
        String consignee = txtConsignee.getText();
        if (!rdbPolitrans.isSelected() && !rdbExim.isSelected()) return; //todo предупреждение о выборе брокера
        Broker broker = rdbPolitrans.isSelected() ? Broker.POLITRANS : Broker.EXIM;
        currentLorry.getCarriages().add(currentLorry, new Carriage(date, consignee, broker));

        InOut.write(lorries);
        clearFields();
        currentStage.hide();
    }

    private void clearFields() {
        txtConsignee.clear();
        rdbPolitrans.setSelected(false);
        rdbExim.setSelected(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setCurrentLorry(Lorry currentLorry) {
        this.currentLorry = currentLorry;
        fillFields();
    }

    public void setLorries(LorriesList lorries) {
        this.lorries = lorries;
    }

    private void fillFields() {
        txtCarId.setText(currentLorry.getIdNumber());
        txtPhoneNum.setText(currentLorry.getPhoneNumber());

        tblClmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tblClmConsignee.setCellValueFactory(new PropertyValueFactory<>("consignee"));
        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblCarriages.setItems(currentLorry.getCarriages().getCarriages());
    }
}
