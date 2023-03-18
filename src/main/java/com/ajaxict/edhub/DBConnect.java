package com.ajaxict.edhub;

// Import Java.sql
import java.sql.*;

public class DBConnect {
    // Create a connection variable and set it to null
    Connection conn = null;

    static final String DB_URL ="jdbc:mysql://localhost:3306/ed_hub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_DRV = "com.mysql.jdbc.Driver";
    static final String DB_USER = "root";
    static final String DB_PASSWD = "";


    public static Connection DBConnect() {

        // Provide a try and catch exception
        try {
            // Connect to mysql library
            // Class.forName("com.mysql.cj.jdbc.Driver");
            // jdbc:mysql://hostname:port/databasename, server username, server password
            Connection conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            return conn;

        }catch(Exception ex) {
            // If connection fail
            System.out.println("Error: " + ex);
            return null;
        }

    }

}
