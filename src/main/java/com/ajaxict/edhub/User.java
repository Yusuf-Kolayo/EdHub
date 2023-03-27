package com.ajaxict.edhub;

import java.sql.*;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String displayPic;

    public User(String email, Connection conn) {

//            Connection conn = DBConnect.DBConnect();

        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                this.id = result.getInt("id");
                this.firstName = result.getString("first_name");
                this.lastName = result.getString("last_name");
                this.gender = result.getString("gender");
                this.email = email;
                this.displayPic = result.getString("display_pic");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean saveDisplayPic(String filePath) {

        try {

            // update the user's display pic filepath in the database
            Connection conn = DBConnect.DBConnect();
            String sql = "UPDATE users SET display_pic = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, filePath);
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public String getDisplayPic() {
        int userId = this.getId();
        String displayPic = null;
        try {
            Connection conn = DBConnect.DBConnect();
            String sql = "SELECT display_pic FROM users WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                displayPic = rs.getString("display_pic");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return displayPic;
    }

    public static void saveNewUser(String firstName, String lastName, String email, String gender, String password, String usrType) {


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

        } catch (SQLException ex) {
            // If there is an exception
            System.out.println("Error: " + ex.getMessage());
        }

    }

    public static int getNextAutoIncrement() {
        int nextAutoIncrementValue=0;
        Connection conn = DBConnect.DBConnect();
        // Initialize statement
        Statement statement = null;
        try {
            statement = conn.createStatement();
            // Execute query to fetch next auto-increment value for 'mytable'
            ResultSet resultSet = statement.executeQuery("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'ed_hub' AND TABLE_NAME = 'users'");

            // Get the next auto-increment value from result set
            if (resultSet.next()) {
                nextAutoIncrementValue = resultSet.getInt(1);
                System.out.println("Next auto-increment value for 'mytable' is " + nextAutoIncrementValue);
            }

            // Close database resources
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nextAutoIncrementValue;
    }







}
