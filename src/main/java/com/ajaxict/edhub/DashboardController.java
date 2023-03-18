package com.ajaxict.edhub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> genderColumn;
    private ObservableList<User> users;

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


}
