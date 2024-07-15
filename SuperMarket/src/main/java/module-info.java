module com.java.supermarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires mysql.connector.java;

    opens com.java.supermarket to javafx.fxml;
    exports com.java.supermarket;
    exports com.java.supermarket.controller;
    opens com.java.supermarket.controller to javafx.fxml;
    opens com.java.supermarket.object to javafx.base;
}
