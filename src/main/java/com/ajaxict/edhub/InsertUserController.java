package com.ajaxict.edhub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InsertUserController implements Initializable {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox genderField;
    @FXML private TextField passwordField;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderField.getItems().addAll("Male","Female");
    }

    @FXML
    private void handleSaveAction() {
        // Get the user's details from the input fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String gender = (String) genderField.getValue();
        String password = passwordField.getText();
        String usrType = "regular_user";

        // Check if all the input fields are filled in
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            // Display a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required!");
            alert.showAndWait();
//            AlertBox.display("Error", "All fields are required!");
            return;
        }

        // Insert the user's details into the database
        try {
            Connection conn = DBConnect.DBConnect();

            // Prepare the SQL statement
            String sql = "INSERT INTO users (usr_type, first_name, last_name, email, gender, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usrType);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, gender);
            pstmt.setString(6, password);

            // Execute the SQL statement
            pstmt.executeUpdate();

            // Close the PreparedStatement and Connection objects
            pstmt.close();
            conn.close();

            // refresh the dashboard users table
            ManageUserController.getInstance().refreshTableData();

            // Display a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User saved successfully!");
            alert.showAndWait();

            clearFields();

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }
    }



    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        genderField.getSelectionModel().clearSelection();
        passwordField.clear();
    }

    // invoke by a back button - commented due to invalidation of it's use
//    @FXML
//    private void BackToDashboard() {
//        // refresh the dashboard users table
//        ManageUserController.getInstance().refreshTableData();
//
//        // Close the insertUserWindow
//        Stage insertUserWindow = (Stage) firstNameField.getScene().getWindow();
//        insertUserWindow.close();
//    }
}
