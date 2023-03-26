package com.ajaxict.edhub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {


    @FXML private Pane mainPane;
    private static MainFrameController instance;

    public static MainFrameController getInstance() {
        if (instance == null) {
            instance = new MainFrameController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardView.fxml"));
            Parent firstContent = null;
            firstContent = loader.load();
            DashboardController dashboardController = loader.getController();
            mainPane.getChildren().add(firstContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // get the instance of the class
        instance = this;
    }


    public void SwitchToDashboard() throws IOException {

        // clear mainPane
        mainPane.getChildren().clear();

        // load new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardView.fxml"));
        Parent newContent = loader.load();
        DashboardController dashboardController = loader.getController();

        mainPane.getChildren().add(newContent);
    }



    public void SwitchToManageUser() throws IOException {

        // clear mainPane
        mainPane.getChildren().clear();

        // load new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manageUserView.fxml"));
        Parent newContent = loader.load();
        ManageUserController manageUserController = loader.getController();

        mainPane.getChildren().add(newContent);
    }

}
