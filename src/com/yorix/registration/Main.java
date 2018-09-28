package com.yorix.registration;

import com.yorix.registration.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    private String lang = "ru";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles/Locale", new Locale(lang)));
        Parent root = loader.load();
        primaryStage.setTitle(loader.getResources().getString("title.main"));
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(200);
        MainController mainController = loader.getController();
        mainController.setMainStage(primaryStage);
        primaryStage.show();
    }


    public void reRun(String lang, Stage primaryStage) {
        this.lang = lang;
        try {
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}