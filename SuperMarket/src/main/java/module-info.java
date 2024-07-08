module com.java.supermarket {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.java.supermarket to javafx.fxml;
    exports com.java.supermarket;
}
