package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.io.InOut;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblCount;
    @FXML
    private DatePicker dtpFrom, dtpTo;
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
    private Stage createReportStage;
    private Parent editDialogWindow;
    private FXMLLoader editDialogLoader;
    private EditDialogController editDialogController;

    private CarriagesList carriagesList;
    private LocalDate from;
    private LocalDate to;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        carriagesList = InOut.read();

        to = LocalDate.now();
        from = LocalDate.of(to.getYear(), to.getMonthValue(), 1);
        dtpFrom.setValue(from);
        dtpTo.setValue(to);

        tblClmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tblClmCarNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        tblClmPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tblClmConsignee.setCellValueFactory(new PropertyValueFactory<>("consignee"));
        tblClmBroker.setCellValueFactory(new PropertyValueFactory<>("broker"));
        tblClmDeclarationId.setCellValueFactory(new PropertyValueFactory<>("declarationId"));
        tblCarriages.setItems(carriagesList.getOptionalList(from, to, null));
        tblCarriages.scrollTo(carriagesList.getSize());
        lblCount.setText(Integer.toString(carriagesList.getSize()));

        initListeners();
        initLoaders();
    }

    private void initListeners() {
        dtpFrom.getEditor().textProperty().addListener(
                (observable, oldVal, newVal) ->
                        from = LocalDate.parse(newVal, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );

        dtpTo.getEditor().textProperty().addListener(
                (observable, oldVal, newVal) ->
                        to = LocalDate.parse(newVal, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );

        carriagesList.getCarriages().addListener((ListChangeListener<Carriage>) c -> InOut.write(carriagesList));

        tblCarriages.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) editCarriage(tblCarriages.getSelectionModel().getSelectedItem());
        });
    }

    private void initLoaders() {
        editDialogLoader = new FXMLLoader(getClass().getResource("/fxml/editDialog.fxml"));
        editDialogLoader.setResources(bundle);

        try {
            editDialogWindow = editDialogLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editDialogController = editDialogLoader.getController();
        editDialogController.setCarriages(carriagesList);
    }

    public void addCarriage() {
        editDialogController.clearFields();
        showEditDialog(bundle.getString("title.addNewNote"));
    }

    public void editCarriage() {
        editCarriage(tblCarriages.getSelectionModel().getSelectedItem());
    }

    private void editCarriage(Carriage carriage) {
        editDialogController.clearFields();
        editDialogController.setCurrentCarriage(carriage);
        showEditDialog(bundle.getString("title.editNote"));
    }

    public void deleteCarriage() {
        Carriage current = tblCarriages.getSelectionModel().getSelectedItem();
        if (current == null) return;
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(bundle.getString("btn.deleteNote") + "?");
        dialog.setHeaderText(bundle.getString("btn.deleteNote") + "?");
        dialog.setContentText(current.getCarNumber());

        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK)
            carriagesList.delete(current);
    }

    private void showEditDialog(String title) {
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
        editDialogStage.showAndWait();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showCarriages(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnShowBroker1) {
            tblCarriages.setItems(carriagesList.getOptionalList(from, to, Broker.POLITRANS));
            carriagesList.setCurrentBrocker(Broker.POLITRANS);

        } else if (actionEvent.getSource() == btnShowBroker2) {
            tblCarriages.setItems(carriagesList.getOptionalList(from, to, Broker.EXIM));
            carriagesList.setCurrentBrocker(Broker.EXIM);

        } else if (actionEvent.getSource() == btnShowAll) {
            tblCarriages.setItems(carriagesList.getOptionalList(from, to, null));
            carriagesList.setCurrentBrocker(null);
        }

        tblCarriages.scrollTo(carriagesList.getSize());
        lblCount.setText(Integer.toString(carriagesList.getSize()));
    }

    public void showCreateReportWindow() {
        if (createReportStage == null) {
            CheckBox carNumber = new CheckBox(bundle.getString("lbl.carNum"));
            CheckBox phoneNumber = new CheckBox(bundle.getString("lbl.phoneNum"));
            CheckBox consignee = new CheckBox(bundle.getString("lbl.consignee"));
            CheckBox broker = new CheckBox(bundle.getString("tbl.clm.broker"));
            CheckBox declarationId = new CheckBox(bundle.getString("lbl.decId"));
            CheckBox additionalInformation = new CheckBox(bundle.getString("lbl.additionalInformation"));

            carNumber.setSelected(true);
            consignee.setSelected(true);

            HBox hBox1 = new HBox(10, carNumber, phoneNumber);
            HBox hBox2 = new HBox(10, consignee, broker);
            HBox hBox3 = new HBox(10, declarationId, additionalInformation);

            Button btnOk = new Button("OK");
            List<Carriage> listForReport = carriagesList.getOptionalList(from, to, carriagesList.getCurrentBrocker());

            VBox root = new VBox(10, hBox1, hBox2, hBox3, btnOk);
            root.setPadding(new Insets(10));

            createReportStage = new Stage();
            createReportStage.setResizable(false);
            createReportStage.setScene(new Scene(root));
            createReportStage.initModality(Modality.WINDOW_MODAL);
            createReportStage.initOwner(mainStage);
            createReportStage.setTitle(bundle.getString("btn.createReport"));

            btnOk.setOnMouseClicked(event -> {
                InOut.createReport(
                        listForReport,
                        true,
                        carNumber.isSelected(),
                        phoneNumber.isSelected(),
                        consignee.isSelected(),
                        broker.isSelected(),
                        declarationId.isSelected(),
                        additionalInformation.isSelected()
                );
                createReportStage.hide();
            });
        }

        createReportStage.show();
    }
}
