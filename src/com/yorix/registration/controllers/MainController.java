package com.yorix.registration.controllers;

import com.yorix.registration.Lorry;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Stage editDialogStage;
    private Stage mainStage;
    private Scene editWindow;
    private ResourceBundle bundle;
    private FXMLLoader loader;

    private List<Lorry> lorries;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;

        loader = new FXMLLoader(getClass().getResource("/com/yorix/registration/edit.fxml"));
        loader.setResources(bundle);
        try {
            editWindow = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNotice(ActionEvent actionEvent) {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle(bundle.getString("editNote"));
            editDialogStage.setResizable(false);
            editDialogStage.setScene(editWindow);
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }
        editDialogStage.showAndWait();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
