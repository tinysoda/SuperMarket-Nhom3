package com.java.supermarket.controller;

import com.java.supermarket.object.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerFormController {

    @FXML
    private Button saveCustomerForm;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField pointsField;

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
    private void handleSave() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        int points = Integer.parseInt(pointsField.getText());

        Customer customer = new Customer(name, phone, points);
        employeeDashboardController.saveCustomer(customer);
        employeeDashboardController.setCustomerName(name); // Cập nhật tên khách hàng

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
