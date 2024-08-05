package com.java.supermarket;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {
    private static final String dbName="supermarket";
    private static final String dbUrl="jdbc:mysql://localhost:3306/"+dbName;
    private static final String dbUser="root";
    private static final String dbPass="";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
