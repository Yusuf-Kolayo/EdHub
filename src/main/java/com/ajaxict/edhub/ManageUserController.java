package com.ajaxict.edhub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {


    @FXML private Pane mainPane;
    @FXML private  TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> genderColumn;
    private static ObservableList<User> users;

    private static ManageUserController instance;

    public static ManageUserController getInstance() {
        if (instance == null) {
            instance = new ManageUserController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize the user table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Load the data from the database and add it to the user table
        try {
            Connection conn = DBConnect.DBConnect();

            // Prepare the SQL statement
            String sql = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = pstmt.executeQuery();

            // Create an observable list of users to hold the data
            users = FXCollections.observableArrayList();

            // Add the data to the observable list
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String displayPic = rs.getString("display_pic");

                users.add(new User(id, firstName, lastName, email, gender));
            }

            // Close the ResultSet, PreparedStatement and Connection objects
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }

        // Set the user table data
        userTableView.setItems(users);

        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Call your function here, passing in the selected row data as needed
                try {
                    handleViewUserAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        try {
            // load insertUserView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insertUserView.fxml"));
            Parent firstContent = null;
            firstContent = loader.load();
            InsertUserController insertUserController = loader.getController();
            mainPane.getChildren().add(firstContent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        

        // get the instance of the class
        instance = this;
    }


    public void refreshTableData() {
        // Load the data from the database and add it to the user table
        try {
            Connection conn = DBConnect.DBConnect();

            // Prepare the SQL statement
            String sql = "SELECT * FROM users";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Execute the SQL statement
            ResultSet rs = pstmt.executeQuery();

            // Create an observable list of users to hold the data
            users = FXCollections.observableArrayList();

            // Add the data to the observable list
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String displayPic = rs.getString("display_pic");

                users.add(new User(id, firstName, lastName, email, gender));
            }

            // Close the ResultSet, PreparedStatement and Connection objects
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }

        // Set the user table data
        userTableView.setItems(users);
    }

    public void clearMainPane() {
        mainPane.getChildren().clear();
    }



    @FXML
    private void handleViewUserAction() throws IOException {
        // Get the selected user
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            // clear mainPane
            mainPane.getChildren().clear();

            // load new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userProfileView.fxml"));
            Parent newContent = loader.load();
            UserProfileController userProfileController = loader.getController();

            // Pass the selected user's details to the editUserController
            userProfileController.setSelectedUser(selectedUser);

            mainPane.getChildren().add(newContent);


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Select a user from the list to proceed");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleEditUserAction() throws IOException {
        // Get the selected user
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            // clear mainPane
            mainPane.getChildren().clear();

            // load new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editUserView.fxml"));
            Parent newContent = loader.load();
            EditUserController editUserController = loader.getController();

            // Pass the selected user's details to the editUserController
            editUserController.setSelectedUser(selectedUser);

            mainPane.getChildren().add(newContent);


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Select a user from the list to proceed");
            alert.showAndWait();
        }
    }



    @FXML
    private void handleDeleteUserAction() throws IOException {
        // Get the selected user
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            // clear mainPane
            mainPane.getChildren().clear();

            // load new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteUserView.fxml"));
            Parent newContent = loader.load();
            DeleteUserController deleteUserController = loader.getController();

            // Pass the selected user's details to the editUserController
            deleteUserController.setSelectedUser(selectedUser);

            mainPane.getChildren().add(newContent);

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Select a user from the list to proceed");
            alert.showAndWait();
        }
    }



    @FXML
    private void handleInsertUserAction() throws IOException {

        // clear mainPane
        mainPane.getChildren().clear();

        // load new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("insertUserView.fxml"));
        Parent newContent = loader.load();
        InsertUserController insertUserController = loader.getController();

        mainPane.getChildren().add(newContent);

    }





}
