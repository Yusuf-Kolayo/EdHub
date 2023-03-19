package com.ajaxict.edhub;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox genderField;
    private User selectedUser;
    private Stage stage;


    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        firstNameField.setText(selectedUser.getFirstName());
        lastNameField.setText(selectedUser.getLastName());
        emailField.setText(selectedUser.getEmail());
        genderField.setValue(selectedUser.getGender());
    }

    @FXML
    private void handleSaveAction() {
        if (selectedUser != null) {
            // Update the user's details in the database
            try {
                Connection conn = DBConnect.DBConnect();

                // Prepare the SQL statement
                String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, gender = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, lastNameField.getText());
                pstmt.setString(3, emailField.getText());
                pstmt.setString(4, (String) genderField.getSelectionModel().getSelectedItem());
                pstmt.setInt(5, selectedUser.getId());

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
                alert.setContentText("User information updated successfully!");
                alert.showAndWait();


            } catch (SQLException ex) {
                // If there is an exception
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }



//    @FXML
//    public void BackToDashboard () {
//        // refresh the dashboard users table
//        ManageUserController.getInstance().refreshTableData();
//
//        // Close the insertUserWindow
//        Stage insertUserWindow = (Stage) firstNameField.getScene().getWindow();
//        insertUserWindow.close();
//    }

}
