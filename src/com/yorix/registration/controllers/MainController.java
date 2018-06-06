package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.LorriesList;
import com.yorix.registration.Lorry;
import com.yorix.registration.io.InOut;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private TableView<Lorry> tblLorries;
    @FXML
    private TableColumn<Lorry, String> tblClmCarID, tblClmDriversPhone;
    @FXML
    private TableColumn<Lorry, Broker> tblClmBroker;

    private ResourceBundle bundle;
    private Stage mainStage;
    private Stage addNewLorryStage;
    private Stage currentLorryStage;
    private Parent addNewLorryWindow;
    private Parent currentLorryWindow;
    private FXMLLoader addNewLorryLoader;
    private FXMLLoader currentLorryLoader;
    private CurrentLorryController currentLorryController;

    private LorriesList lorries;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        lorries = new LorriesList();

        tblClmCarID.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        tblClmDriversPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblLorries.setItems(lorries.getLorries());

        initListeners();
        initLoader();
    }

    private void initListeners() {
        lorries.getLorries().addListener((ListChangeListener<Lorry>) c -> {
//                updateCountLabel(); todo create method
        });

        tblLorries.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) openNote(tblLorries.getSelectionModel().getSelectedItem());
        });
    }

    private void initLoader() {
        addNewLorryLoader = new FXMLLoader(getClass().getResource("../fxml/addNewLorry.fxml"));
        currentLorryLoader = new FXMLLoader(getClass().getResource("../fxml/currentLorry.fxml"));
        addNewLorryLoader.setResources(bundle);
        currentLorryLoader.setResources(bundle);

        try {
            addNewLorryWindow = addNewLorryLoader.load();
            currentLorryWindow = currentLorryLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddNewLorryController addNewLorryController = addNewLorryLoader.getController();
        addNewLorryController.setLorries(lorries);
        addNewLorryController.setMainController(this);
    }

    public void createNote() {
        if (addNewLorryStage == null) {
            addNewLorryStage = new Stage();
            addNewLorryStage.setTitle(bundle.getString("title.addNewNotice"));
            addNewLorryStage.setResizable(false);
            addNewLorryStage.setScene(new Scene(addNewLorryWindow));
            addNewLorryStage.initModality(Modality.WINDOW_MODAL);
            addNewLorryStage.initOwner(mainStage);

            AddNewLorryController addNewLorryController = addNewLorryLoader.getController();
            addNewLorryController.setCurrentStage(addNewLorryStage);
        }
        addNewLorryStage.show();
    }

    public void openNote(ActionEvent actionEvent) {
        openNote(tblLorries.getSelectionModel().getSelectedItem());
    }

    public void openNote(Lorry selectedLorry) {
        if (selectedLorry == null) return;
        if (currentLorryStage == null) {
            currentLorryStage = new Stage();
            currentLorryStage.setTitle(bundle.getString("title.aboutLorry"));
            currentLorryStage.setResizable(false);
            currentLorryStage.setScene(new Scene(currentLorryWindow));
            currentLorryStage.initModality(Modality.WINDOW_MODAL);
            currentLorryStage.initOwner(mainStage);

            currentLorryController = currentLorryLoader.getController();
            currentLorryController.setCurrentStage(currentLorryStage);
            currentLorryController.setLorries(lorries);
        }
        currentLorryController.setCurrentLorry(selectedLorry);
        currentLorryStage.show();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
