package com.ajaxict.edhub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {


    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane root;



    public void switchToWelcome(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("welcomeView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("EdHub - Register");
        stage.setScene(scene);
        stage.show();

    }


    public void quitApp (ActionEvent event) {
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        this.stage.close();
        Platform.exit();
    }

}