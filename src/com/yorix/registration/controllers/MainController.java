package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Car;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btnShowBroker1, btnShowBroker2, btnShowAll;
    @FXML
    private TableView<Carriage> tblCarriages;
    @FXML
    private TableColumn<Carriage, String> tblClmDate, tblClmCarNumber, tblClmPhoneNumber, tblClmConsignee, tblClmDeclarationId;
    @FXML
    private TableColumn<Carriage, Broker> tblClmBroker;

    private ResourceBundle bundle;
    private Stage mainStage;
    private Stage editDialogStage;
    private Parent editDialogWindow;
    private FXMLLoader editDialogLoader;
    private EditDialogController editDialogController;

    private CarriagesList carriagesList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        carriagesList = InOut.read();

        tblClmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tblClmCarNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        tblClmPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tblClmConsignee.setCellValueFactory(new PropertyValueFactory<>("consignee"));
        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblClmDeclarationId.setCellValueFactory(new PropertyValueFactory<>("declarationId"));
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
        editDialogLoader = new FXMLLoader(getClass().getResource("../fxml/editDialog.fxml"));
        editDialogLoader.setResources(bundle);

        try {
            editDialogWindow = editDialogLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editDialogController = editDialogLoader.getController();
        editDialogController.setCarriages(carriagesList);
        editDialogController.setMainController(this);
    }

    public void addCarriage(ActionEvent actionEvent) {
        editDialogController.setCurrentCarriage(null);

        showEditDialog(bundle.getString("title.addNewNote"));
    }

    public void editCarriage(Carriage carriage) {
        editDialogController.setCurrentCarriage(carriage);
        showEditDialog(bundle.getString("title.editNote"));
    }

    public void showEditDialog(String title) {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(editDialogWindow));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);

            EditDialogController editDialogController = editDialogLoader.getController();
            editDialogController.setCurrentStage(editDialogStage);
        }
        editDialogStage.setTitle(title);
        editDialogStage.show();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showCarriages(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnShowBroker1) {
            tblCarriages.setItems(carriagesList.getCarriages(Broker.POLITRANS));

        } else if (actionEvent.getSource() == btnShowBroker2) {
            tblCarriages.setItems(carriagesList.getCarriages(Broker.EXIM));

        } else if (actionEvent.getSource() == btnShowAll) {
            tblCarriages.setItems(carriagesList.getCarriages());
        }
    }
}
