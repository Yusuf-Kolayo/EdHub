package com.ajaxict.edhub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class InsertUserController implements Initializable {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox genderField;
    @FXML private TextField passwordField;
    @FXML private TextField numRandUserField;


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
            AlertBox.display("Error", "All fields are required!", Alert.AlertType.WARNING);
            return;
        }

        User.saveNewUser(firstName,lastName,email,gender,password,usrType);

        // refresh the dashboard users table
        ManageUserController.getInstance().refreshTableData();
        clearFields();
        AlertBox.display("Success", "User saved successfully!", Alert.AlertType.INFORMATION);

    }

    @FXML
    private void fetchRandomUsersAndInsert() {
        // Get the user's details from the input fields
        String numUsers = numRandUserField.getText();

        // Check if all the input fields are filled in
        if (numUsers.isEmpty()) {
            // Display a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Enter number of users to generate!");
            alert.showAndWait();
            numRandUserField.requestFocus();
        } else {
            int num = Integer.parseInt(numUsers);

                if (!isInternetConnected()) {
                    System.out.println("No internet connection.");
                    return;
                }
                try {
                    // Set API endpoint and query parameters
                    String endpoint = "https://randomuser.me/api/?results=" + num + "&nat=us,ca&inc=name,email,gender,picture";
                    URL url = new URL(endpoint);

                    // Open a connection to the API endpoint
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Get response from the API
                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        throw new RuntimeException("Failed to retrieve data from API: " + responseCode);
                    }
                    Scanner scanner = new Scanner(url.openStream());
                    String response = scanner.useDelimiter("\\Z").next();
                    scanner.close();

                    // Parse the response data
                    JSONObject json = new JSONObject(response);
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject user = results.getJSONObject(i);
                        String firstName = user.getJSONObject("name").getString("first");
                        String lastName = user.getJSONObject("name").getString("last");
                        String email = user.getString("email");
                        String gender = user.getString("gender");
                        String pictureUrl = user.getJSONObject("picture").getString("large");
                        System.out.println(firstName + " " + lastName + " - " + email);

                        String password = firstName;
                        String usrType = "regular_user";

                        User.saveNewUser(firstName,lastName,email,gender,password,usrType);


                        // check if user dp folder present or create
                        Utility.checkUserDpFolder();
                        String userDpDirectory = new Utility().userDpDirectory;

                        long timestamp = System.currentTimeMillis();      // fetch & attach timestamp
                        String timestampStr = String.valueOf(timestamp);
                        int user_id = User.getNextAutoIncrement();
                        String userDpAbsoluteFilePath = userDpDirectory+"/user_" +user_id+"_"+ timestampStr + ".jpg";

                        // Download user picture
                        URL picture = new URL(pictureUrl);
                        InputStream in = new BufferedInputStream(picture.openStream());
                        FileOutputStream out = new FileOutputStream(userDpAbsoluteFilePath);
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = in.read(buffer, 0, buffer.length)) >= 0) {
                            out.write(buffer, 0, bytesRead);
                        }
                        out.close();
                        in.close();

                        User newUser = new User(email);
                        newUser.saveDisplayPic(userDpAbsoluteFilePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            // refresh the dashboard users table
            ManageUserController.getInstance().refreshTableData();
            clearFields();
            AlertBox.display("Success", "Users fetched and saved successfully!", Alert.AlertType.INFORMATION);

        }

    }





    private static boolean isInternetConnected() {
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            conn.disconnect();
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        genderField.getSelectionModel().clearSelection();
        passwordField.clear();
        numRandUserField.clear();
    }

// invoke by a back button - commented due to invalidation of the use
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
