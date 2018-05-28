package com.yorix.registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));
        loader.setResources(ResourceBundle.getBundle("com.yorix.registration.bundles.Locale", new Locale("ru")));
        Parent root = loader.load();
        primaryStage.setTitle(loader.getResources().getString("title"));
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        Lorry lorry = new Lorry("", "", Broker.EXIM);

        lorry.addCurrentDate();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
