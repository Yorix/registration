package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Car;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btnShow1Broker, btnShow2Broker, btnShowAll;
    @FXML
    private TableView<Carriage> tblCarriages;
    @FXML
    private TableColumn<Carriage, String> tblClmDate;
    @FXML
    private TableColumn<Carriage, String> tblClmCarNumber;
    @FXML
    private TableColumn<Carriage, String> tblClmPhoneNumber;
    @FXML
    private TableColumn<Carriage, Broker> tblClmBroker;

    private ResourceBundle bundle;
    private Stage mainStage;
    private Stage addNewLorryStage;
    private Stage editCarriageStage;
    private Parent addNewLorryWindow;
    private Parent currentLorryWindow;
    private FXMLLoader addNewCarriageLoader;
    private FXMLLoader editCarriageLoader;
    private EditCarriageController editCarriageController;

    private CarriagesList carriagesList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        carriagesList = InOut.read();

        tblClmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tblClmCarNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCar().getCarNumber()));
        tblClmPhoneNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCar().getPhoneNumber()));
        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblCarriages.setItems(carriagesList.getCarriages());

        initListeners();
        initLoaders();
    }

    private void initListeners() {
        carriagesList.getCarriages().addListener((ListChangeListener<Carriage>) c -> {
//                updateCountLabel(); todo create method
        });

        tblCarriages.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) editCarriage(tblCarriages.getSelectionModel().getSelectedItem());
        });
    }

    private void initLoaders() {
        addNewCarriageLoader = new FXMLLoader(getClass().getResource("../fxml/addNewCarriage.fxml"));
        editCarriageLoader = new FXMLLoader(getClass().getResource("../fxml/editCarriage.fxml"));
        addNewCarriageLoader.setResources(bundle);
        editCarriageLoader.setResources(bundle);

        try {
            addNewLorryWindow = addNewCarriageLoader.load();
            currentLorryWindow = editCarriageLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddNewCarriageController addNewCarriageController = addNewCarriageLoader.getController();
        addNewCarriageController.setCarriages(carriagesList);
        addNewCarriageController.setMainController(this);
    }

    public void createCarriage() {
        if (addNewLorryStage == null) {
            addNewLorryStage = new Stage();
            addNewLorryStage.setTitle(bundle.getString("title.addNewNotice"));
            addNewLorryStage.setResizable(false);
            addNewLorryStage.setScene(new Scene(addNewLorryWindow));
            addNewLorryStage.initModality(Modality.WINDOW_MODAL);
            addNewLorryStage.initOwner(mainStage);

            AddNewCarriageController addNewCarriageController = addNewCarriageLoader.getController();
            addNewCarriageController.setCurrentStage(addNewLorryStage);
        }
        addNewLorryStage.show();
    }

    public void editCarriage(ActionEvent actionEvent) {
        editCarriage(tblCarriages.getSelectionModel().getSelectedItem());
    }

    public void editCarriage(Carriage selectedCarriage) {
        if (selectedCarriage == null) return;
        if (editCarriageStage == null) {
            editCarriageStage = new Stage();
            editCarriageStage.setTitle(bundle.getString("title.aboutLorry"));
            editCarriageStage.setResizable(false);
            editCarriageStage.setScene(new Scene(currentLorryWindow));
            editCarriageStage.initModality(Modality.WINDOW_MODAL);
            editCarriageStage.initOwner(mainStage);

            editCarriageController = editCarriageLoader.getController();
            editCarriageController.setCurrentStage(editCarriageStage);
            editCarriageController.setCarriagesList(carriagesList);
        }
        editCarriageController.setCurrentCarriage(selectedCarriage);
        editCarriageStage.show();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }


    public void showCarriages(ActionEvent actionEvent) {
//        CarriagesList carriagesList = new CarriagesList();
//        if (actionEvent.getSource() == btnShow1Broker) {
//            this.carriagesList.getCarriages().stream()
//                    .map(CarriagesList::getCarriages)
//                    .map(CarriagesList::getCarriages)
//                    .forEach(System.out::println); //todo
//            for (Carriage c : this.carriagesList.getCarriages()) {
//
//            }
//        } else if (actionEvent.getSource() == btnShow2Broker) {
//
//        } else if (actionEvent.getSource() == btnShowAll) {
//
//        }
    }
}
