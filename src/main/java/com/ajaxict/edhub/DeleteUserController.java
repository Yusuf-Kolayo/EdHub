package com.ajaxict.edhub;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUserController {

    @FXML private int userId;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox genderField;

    private User selectedUser;

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        this.userId = selectedUser.getId();
        firstNameField.setText(selectedUser.getFirstName());
        lastNameField.setText(selectedUser.getLastName());
        emailField.setText(selectedUser.getEmail());
        genderField.setValue(selectedUser.getGender());
    }

    @FXML
    private void handleDeleteUserAction() throws SQLException {
        // Get the selected user
        User selectedUser = this.selectedUser;

        if (selectedUser != null) {
            // Show confirmation dialog before deleting the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete User?");
            alert.setContentText("Are you sure you want to delete this user?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    // Create a connection to the database
                    Connection conn = DBConnect.DBConnect();

                    // Prepare the SQL statement
                    String sql = "DELETE FROM users WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedUser.getId());

                    // Execute the SQL statement
                    pstmt.executeUpdate();

                    // Close the PreparedStatement and Connection objects
                    pstmt.close();
                    conn.close();

                    // refresh the dashboard users table
                    ManageUserController.getInstance().refreshTableData();
                    ManageUserController.getInstance().clearMainPane();

                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Information Dialog");
                    successAlert.setHeaderText("User Deleted");
                    successAlert.setContentText("The selected user has been deleted.");
                    successAlert.showAndWait();

                } catch (SQLException ex) {
                    // If there is an exception
                    System.out.println("Error: " + ex.getMessage());
                }


            }
        }
    }

//    @FXML
//    private void BackToDashboard() {
//        // refresh the dashboard users table
//        ManageUserController.getInstance().refreshTableData();
//
//        // Close the deleteUserView
//        firstNameField.getScene().getWindow().hide();
//    }
}
