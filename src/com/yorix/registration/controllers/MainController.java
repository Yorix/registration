package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.LorriesList;
import com.yorix.registration.Lorry;
import com.yorix.registration.io.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    private Stage editDialogStage;
    private Stage mainStage;
    private Parent editWindow;
    private ResourceBundle bundle;
    private FXMLLoader loader;
    private AddNewLorryController addNewLorryController;

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
        lorries.getLorries().addListener(new ListChangeListener<Lorry>() {
            @Override
            public void onChanged(Change<? extends Lorry> c) {
//                updateCountLabel(); todo create method
            }
        });

        tblLorries.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) editNote();
            }
        });
    }

    public void editNote() {
        Lorry selectedPerson = (Lorry) tblLorries.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) return;
//        editDialogController.setPerson(selectedPerson); todo написать метод
//        showDialog();
    }

    private void initLoader() {
        loader = new FXMLLoader(getClass().getResource("../fxml/addNewLorry.fxml"));
        loader.setResources(bundle);

        try {
            editWindow = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addNewLorryController = loader.getController();
//        addNewLorryController.setLorries(lorries.getLorries());
    }

    public void createNote() {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle(bundle.getString("addNewNotice"));
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(editWindow));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);

            AddNewLorryController addNewLorryController = loader.getController();
            addNewLorryController.setCurrentStage(editDialogStage);
        }

        editDialogStage.show();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
