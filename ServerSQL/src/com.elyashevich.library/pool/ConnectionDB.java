package com.elyashevich.library.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionDB {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("\\resource\\database");
    private static String url = resourceBundle.getString("db.url");
    private static String user = resourceBundle.getString("db.user");
    private static String pass = resourceBundle.getString("db.password");

    public static Connection createConnection() throws SQLException{
        return DriverManager.getConnection(url, user, pass);
    }
}
