module com.java.supermarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.java.supermarket to javafx.fxml;
    exports com.java.supermarket;
}
