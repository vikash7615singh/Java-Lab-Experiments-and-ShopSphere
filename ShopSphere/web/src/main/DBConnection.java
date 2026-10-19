
package com.shopsphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC";

    private static final String USERNAME = "root";

    // Replace YOUR_MYSQL_PASSWORD with your actual MySQL password
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }
}
