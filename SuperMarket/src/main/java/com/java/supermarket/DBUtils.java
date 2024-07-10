package com.java.supermarket;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {
    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket","root","");
            return con;
        }catch (Exception e){e.printStackTrace();}

        return null;
    }
}
