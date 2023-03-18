package com.ajaxict.edhub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WelcomeController {

    private Parent root;
    private Stage stage;
    private Scene scene;


    @FXML private TextField ctrEmail;
    @FXML private TextField ctrPassword;




    public void logInUser(ActionEvent event) throws IOException {

        // Get the login data from the view
        String email = ctrEmail.getText();
        String password = ctrPassword.getText();

        // Check if the email and password match a user in the database
        try {
            Connection conn = DBConnect.DBConnect();

            // Prepare the SQL statement
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set the parameters of the SQL statement
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // Execute the SQL statement
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the login data matches a user in the database
                System.out.println("Login successful!");
                Parent root = FXMLLoader.load(getClass().getResource("dashboardView.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setTitle("EdHub - Dashboard");
                stage.setScene(scene);
                stage.show();
            } else {
                // If the login data does not match a user in the database
                System.out.println("Login failed!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EdHub");
                alert.setContentText("Login failed! Incorrect email or password.");
                alert.showAndWait();
            }

            // Close the ResultSet, PreparedStatement, and Connection objects
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }
    }




    public void switchToRegister(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("registerView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("EdHub - Register");
        stage.setScene(scene);
        stage.show();

    }



    public void quitApp (ActionEvent event) {
//           stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//           this.stage.close();
        Platform.exit();
    }

}