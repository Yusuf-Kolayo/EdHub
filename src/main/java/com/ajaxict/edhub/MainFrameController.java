package com.ajaxict.edhub;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {


    @FXML private Pane mainPane;
    private static MainFrameController instance;
    private static ObservableList<User> users;

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
        ManageUserController controller = loader.getController();
        loadDataInBackground(controller);
    }




    public static void loadDataInBackground(ManageUserController controller) {
        Task<ObservableList<User>> task = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() throws Exception {

                Connection conn = DBConnect.DBConnect();
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                int currentRow = 0;  int allRows = 0;

//                StackTraceElement[] st = Thread.currentThread().getStackTrace();
//                System.out.println( "create connection called from " + st[2] );

                // Load the data from the database and add it to the user table
                try {


                    // Prepare the SQL statement
                    String sql = "SELECT id,email FROM users ORDER BY id asc";
                     pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                    // Execute the SQL statement
                     rs = pstmt.executeQuery();

                    // Move the cursor to the last row and get the row number
                    rs.last();   allRows = rs.getRow();

                    // Move the cursor back to the beginning
                    rs.beforeFirst();

                    // Create an observable list of users to hold the data
                    users = FXCollections.observableArrayList();

                    // Add the data to the observable list
                    while (rs.next()) {
                        currentRow++;
                        String email = rs.getString("email");
                        users.add(new User(email, conn));

                        updateProgress(currentRow, allRows); // update progress bar to full when loading is complete
                    }



                } catch (SQLException ex) {
                    // If there is an exception
                    System.out.println("Error: " + ex.getMessage());
                } finally {
                    // Close the ResultSet, PreparedStatement and Connection objects
                    rs.close();
                    pstmt.close();
                    conn.close();
                }



                return users;
            }

        };

        // bind the progress property of the task to the progress bar in the view
        controller.getProgressBar().progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(e -> {
            controller.setData(task.getValue()); // update the TableView with the loaded data
        });

        new Thread(task).start(); // start the task in a new thread
    }


}
