package com.ajaxict.edhub;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {


    private Stage stage;
    private Scene scene;

    @FXML private AnchorPane root;
    @FXML private TextField ctrFirstName;
    @FXML private TextField ctrLastName;
    @FXML private TextField ctrEmail;
    @FXML private ComboBox ctrGender;
    @FXML private TextField ctrPassword;
    @FXML private Label lblTitle;


    public void switchToWelcome(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("welcomeView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("EdHub - Register");
        stage.setScene(scene);
        stage.show();

    }


    public void saveUserToDb (ActionEvent event) {

        // Get the registration data from the view
        String firstName = ctrFirstName.getText();
        String lastName = ctrLastName.getText();
        String email = (String) ctrEmail.getText();
        String gender = (String) ctrGender.getValue();
        String password = ctrPassword.getText();

        // Insert the registration data into the database
        try {
            Connection conn = DBConnect.DBConnect();

            // Prepare the SQL statement
            String sql = "INSERT INTO users (first_name, last_name, email, gender, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set the parameters of the SQL statement
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, gender);
            pstmt.setString(5, password);

            // Execute the SQL statement
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                // If the data was successfully inserted into the database
                System.out.println("Registration successful!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EdHub");
                alert.setHeaderText(null);
                alert.setContentText("Registration was successful!");
                alert.showAndWait();
            } else {
                // If the data was not inserted into the database
                System.out.println("Registration failed!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EdHub");
                alert.setHeaderText(null);
                alert.setContentText("Registration failed! Something went wrong. Try again");
                alert.showAndWait();
            }

            // Close the PreparedStatement and Connection objects
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }

    }


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


    public void quitApp (ActionEvent event) {
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        this.stage.close();
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            ctrGender.getItems().addAll("Male","Female");
    }
}