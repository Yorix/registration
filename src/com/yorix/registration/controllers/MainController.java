package com.yorix.registration.controllers;

import com.yorix.registration.Broker;
import com.yorix.registration.Carriage;
import com.yorix.registration.CarriagesList;
import com.yorix.registration.Main;
import com.yorix.registration.io.InOut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ChoiceBox<Locale> chbLang;
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

    private InOut inOut;
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

        ObservableList<Locale> locales = FXCollections.observableArrayList(new Locale("ru"), new Locale("ua"), new Locale("en"));
        chbLang.setItems(locales);
        chbLang.getSelectionModel().select(bundle.getLocale());

        inOut = new InOut(resources);
        carriagesList = inOut.read(LocalDate.now().getYear());

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
        tblCarriages.setItems(carriagesList.createOptionalList(from, to, null));
        tblCarriages.scrollTo(carriagesList.getSize());
        lblCount.setText(Integer.toString(carriagesList.getSize()));

        initListeners();
        initLoaders();
    }

    private void initListeners() {
        chbLang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(new Locale("ru"))) {
                mainStage.close();
                new Main().reRun("ru", mainStage);
            } else if (newValue.equals(new Locale("ua"))) {
                mainStage.close();
                new Main().reRun("ua", mainStage);
            } else if (newValue.equals(new Locale("en"))) {
                mainStage.close();
                new Main().reRun("en", mainStage);
            }
        });

        dtpFrom.getEditor().textProperty().addListener(
                (observable, oldVal, newVal) ->
                        from = LocalDate.parse(newVal, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );

        dtpTo.getEditor().textProperty().addListener(
                (observable, oldVal, newVal) -> {
                    to = LocalDate.parse(newVal, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    if (from.getYear() != Integer.valueOf(newVal.substring(6))) {
                        PopUp.showAlert(bundle.getString("report.wrongYear"));
                        from = LocalDate.of(to.getYear(), 1, 1);
                        dtpFrom.setValue(from);
                    }
                }
        );

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
        Carriage current = tblCarriages.getSelectionModel().getSelectedItem();
        if (current == null) return;
        editCarriage(current);
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
        dialog.getDialogPane().getScene().getStylesheets().add("styles/main.css");

        Optional<ButtonType> option = dialog.showAndWait();

        if (option.get() == ButtonType.OK) {
            int year = current.getDate().getYear();
            carriagesList.delete(current);
            inOut.write(carriagesList, year);
        }
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

        carriagesList = inOut.read(from.getYear());

        if (actionEvent.getSource() == btnShowBroker1) {
            tblCarriages.setItems(carriagesList.createOptionalList(from, to, Broker.POLITRANS));
            carriagesList.setCurrentBroker(Broker.POLITRANS);

        } else if (actionEvent.getSource() == btnShowBroker2) {
            tblCarriages.setItems(carriagesList.createOptionalList(from, to, Broker.EXIM));
            carriagesList.setCurrentBroker(Broker.EXIM);

        } else if (actionEvent.getSource() == btnShowAll) {
            tblCarriages.setItems(carriagesList.createOptionalList(from, to, null));
            carriagesList.setCurrentBroker(null);
        }

        tblCarriages.scrollTo(carriagesList.getSize());
        lblCount.setText(Integer.toString(carriagesList.getSize()));
    }

    public void showCreateReportWindow() {
        List<Carriage> listForReport = carriagesList.getOptional();

        CheckBox carNumber = new CheckBox(bundle.getString("lbl.carNum"));
        CheckBox phoneNumber = new CheckBox(bundle.getString("lbl.phoneNum"));
        CheckBox consignee = new CheckBox(bundle.getString("lbl.consignee"));
        CheckBox broker = new CheckBox(bundle.getString("tbl.clm.broker"));
        CheckBox declarationId = new CheckBox(bundle.getString("lbl.decId"));
        CheckBox additionalInformation = new CheckBox(bundle.getString("lbl.additionalInformation"));

        carNumber.setSelected(true);
        consignee.setSelected(true);
        additionalInformation.setSelected(true);

        VBox vBox1 = new VBox(10, carNumber, consignee, declarationId);
        VBox vBox2 = new VBox(10, phoneNumber, broker, additionalInformation);
        HBox hBox = new HBox(20, vBox1, vBox2);

        Button btnOk = new Button("OK");

        VBox root = new VBox(10, hBox, btnOk);
        root.setPadding(new Insets(10));

        createReportStage = new Stage();
        createReportStage.setResizable(false);
        createReportStage.setScene(new Scene(root));
        createReportStage.initModality(Modality.WINDOW_MODAL);
        createReportStage.initOwner(mainStage);
        createReportStage.setTitle(bundle.getString("btn.createReport"));

        btnOk.setOnMouseClicked(event -> {
            inOut.createReport(
                    listForReport,
                    true,
                    carNumber.isSelected(),
                    phoneNumber.isSelected(),
                    consignee.isSelected(),
                    broker.isSelected(),
                    declarationId.isSelected(),
                    additionalInformation.isSelected()
            );
            createReportStage.close();
        });

        createReportStage.getScene().getStylesheets().add("styles/main.css");
        createReportStage.show();
    }

    public void saveBase() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(this.mainStage);
        if (file != null)
            inOut.copyBase(null, file.toString());
    }

    public void loadBase() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(this.mainStage);
        if (file != null)
            inOut.copyBase(file.toString(), null);

        carriagesList = inOut.read(LocalDate.now().getYear());
        showCarriages(new ActionEvent(btnShowAll, null));
    }
}
