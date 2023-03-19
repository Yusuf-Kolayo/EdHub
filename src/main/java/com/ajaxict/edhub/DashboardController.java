package com.ajaxict.edhub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    private Scene manageUserScene;
    private Scene postsBoardScene;
    @FXML private Pane mainPane;
    private static DashboardController instance;

    public static DashboardController getInstance() {
        if (instance == null) {
            instance = new DashboardController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manageUserView.fxml"));
            Parent firstContent = null;
            firstContent = loader.load();
            ManageUserController manageUserController = loader.getController();
            mainPane.getChildren().add(firstContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // get the instance of the class
        instance = this;
    }



}
