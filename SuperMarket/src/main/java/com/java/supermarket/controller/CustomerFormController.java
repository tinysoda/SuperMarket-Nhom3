package com.java.supermarket.controller;
import com.java.supermarket.DBUtils;
import com.java.supermarket.object.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerFormController {
    @FXML private Button saveCustomerForm;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField pointsField;
    private EmployeeDashboardController employeeDashboardController;

    public void setEmployeeDashboardController(EmployeeDashboardController controller) {
        this.employeeDashboardController = controller;
    }
    public void setCustomer(Customer customer) {
        if (customer != null) {
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhone());
            pointsField.setText(String.valueOf(customer.getPoints()));
        }
    }

    @FXML
    private void initialize() {
        pointsField.setEditable(false);
        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    clearCustomerInfo();
                    return;
                }

                Customer customer = getCustomerByPhone(newValue);
                if (customer != null) {
                    nameField.setText(customer.getName());
                    pointsField.setText(String.valueOf(customer.getPoints()));
                } else {
                    clearCustomerInfo();
                }
            }
        });

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(oldValue);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Sai cú pháp: chỉ được phép nhập số");
                alert.showAndWait();
            }
        });
    }

    private void clearCustomerInfo() {
        nameField.clear();
        pointsField.clear();
        if (employeeDashboardController != null) {
            employeeDashboardController.clearCustomerNameField();
        }
    }

    public Customer getCustomerByPhone(String phone) {
        Customer customer = null;
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT * FROM customer WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int points = resultSet.getInt("points");
                customer = new Customer(name, phone, points);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } return customer;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public TextField getNameField() {
        return nameField;
    }
    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        int points;

        if (pointsField.getText().isEmpty()) {
            points = 0; // Set default points to 0 if the points field is empty
        } else {
            points = Integer.parseInt(pointsField.getText());
        }

        Customer customer = new Customer(name, phone, points);
        employeeDashboardController.saveCustomer(customer);
        employeeDashboardController.setCustomerName(name); // Update the customer name
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


}