package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.LorriesList;
import com.yorix.registration.Lorry;
import com.yorix.registration.io.Reader;
import javafx.collections.ListChangeListener;
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

    private Stage addDialogStage;
    private Stage mainStage;
    private Parent editWindow;
    private ResourceBundle bundle;
    private FXMLLoader loader;

    private LorriesList lorries;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        lorries = new LorriesList();


        tblClmCarID.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        tblClmDriversPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
//        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblLorries.setItems(lorries.getLorries());

        initListeners();
        initLoader();

    }

    private void initListeners() {
        lorries.getLorries().addListener((ListChangeListener<Lorry>) c -> {
//                updateCountLabel(); todo create method
        });

        tblLorries.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) editNote();
        });
    }

    private void initLoader() {
        loader = new FXMLLoader(getClass().getResource("../fxml/addNewLorry.fxml"));
        loader.setResources(bundle);

        try {
            editWindow = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddNewLorryController addNewLorryController = loader.getController();
        addNewLorryController.setLorries(lorries);
    }

    public void createNote() {
        if (addDialogStage == null) {
            addDialogStage = new Stage();
            addDialogStage.setTitle(bundle.getString("addNewNotice"));
            addDialogStage.setResizable(false);
            addDialogStage.setScene(new Scene(editWindow));
            addDialogStage.initModality(Modality.WINDOW_MODAL);
            addDialogStage.initOwner(mainStage);

            AddNewLorryController addNewLorryController = loader.getController();
            addNewLorryController.setCurrentStage(addDialogStage);
        }

        addDialogStage.show();
    }

    public void editNote() {
        Lorry selectedLorry = tblLorries.getSelectionModel().getSelectedItem();
        if (selectedLorry == null) return;
//        editDialogController.setPerson(selectedLorry); todo написать метод
//        showDialog();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
