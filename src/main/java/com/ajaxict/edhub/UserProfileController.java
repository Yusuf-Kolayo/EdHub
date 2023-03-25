package com.ajaxict.edhub;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserProfileController {

    @FXML private Label lblFirstName;
    @FXML private Label lblLastName;
    @FXML private Label lblEmail;
    @FXML private Label lblGender;
    @FXML private ImageView imgViewer;
    private User selectedUser;
    private Stage stage;
    private String displayPic;

    private FileInputStream dpFileInputStream;


    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        lblFirstName.setText(selectedUser.getFirstName());
        lblLastName.setText(selectedUser.getLastName());
        lblEmail.setText(selectedUser.getEmail());
        lblGender.setText(selectedUser.getGender());
        this.displayPic = selectedUser.getDisplayPic();
        showDp();

    }







    public void changeDisplayPic(ActionEvent event) throws IOException {

        String oldDp = selectedUser.getDisplayPic();

        // Show file chooser dialog to select image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Display Picture");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            // create the target directory for the uploaded user pictures if it does not already exist
            String userHomeDir = System.getProperty("user.home");
            String projectName = "edhub";
            File projectDir = new File(userHomeDir, projectName);
            if (!projectDir.exists()) {
                projectDir.mkdir();
            }
            File userDpDir = new File(projectDir, "user_dp");
            if (!userDpDir.exists()) {
                userDpDir.mkdir();
            }

            long timestamp = System.currentTimeMillis();      // fetch & attach timestamp
            String timestampStr = String.valueOf(timestamp);

            // Copy selected file to user_dp folder
            String destFilePath = userHomeDir + "/edhub/user_dp/user_" + selectedUser.getId() + "_" + timestampStr + ".jpg";
            Path destPath = Paths.get(destFilePath);
            Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            // Update user's displayPic field in database
            if (selectedUser.setDisplayPic(destFilePath)) {

                if (oldDp!=null) {
                    deleteOldDp(oldDp);  // delete old pic
                }
                showDp();   // update the display pic in the UI
            }
        }
    }


    public boolean showDp() {
        this.displayPic = selectedUser.getDisplayPic();
        if (this.displayPic!=null) {
            try {
                dpFileInputStream = new FileInputStream(selectedUser.getDisplayPic());
                Image userDp = new Image(dpFileInputStream);
                imgViewer.setImage(userDp);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            Image userDP = new Image(getClass().getResourceAsStream("avatar_dummy.png"));
            imgViewer.setImage(userDP);
            imgViewer.setFitWidth(202); // set the desired width of the ImageView
            imgViewer.setFitHeight(192); // set the desired height of the ImageView
            imgViewer.setPreserveRatio(true); // maintain the aspect ratio of the image
        }
        return true;
    }


    public boolean deleteOldDp(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                dpFileInputStream.close(); // close the input stream to release any locks on the file
                boolean result = file.delete();
                if (!result) {
                    System.err.println("Failed to delete file: " + filePath);
                }
                return result;
            } catch (SecurityException e) {
                System.err.println("Security exception when deleting file: " + filePath);
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("File does not exist: " + filePath);
        }
        return false;
    }



}
