package com.yorix.registration.controllers;

import javafx.scene.control.Alert;

public class PopUp {
    public static void showAlert(String message) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle(message);
        dialog.setHeaderText(message);
        dialog.getDialogPane().getScene().getStylesheets().add("styles/main.css");
        dialog.show();
    }
}