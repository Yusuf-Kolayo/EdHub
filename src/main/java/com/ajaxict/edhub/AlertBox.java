package com.ajaxict.edhub;

import javafx.scene.control.Alert;

public class AlertBox {

    public static void display(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
