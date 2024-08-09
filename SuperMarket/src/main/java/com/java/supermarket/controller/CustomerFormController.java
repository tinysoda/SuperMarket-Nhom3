package com.java.supermarket.controller;
import com.java.supermarket.DBUtils;
import com.java.supermarket.object.Customer;
import com.java.supermarket.object.CustomerState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class CustomerFormController {
    @FXML private Button saveCustomerForm;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField pointsField;
    private TabContentController tabContentController;
    @FXML private ListView<String> customerListView;
    @FXML private TextField searchCutsomerField;
    @FXML private Button updateCustomerInfor;
    @FXML private Button clearBtn;

    public void setTabContentController(TabContentController controller) {
        this.tabContentController = controller;
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
        if (CustomerState.getName() != null) {
            nameField.setText(CustomerState.getName());
            phoneField.setText(CustomerState.getPhone());
            pointsField.setText(String.valueOf(CustomerState.getPoints()));
            nameField.setEditable(false);
            phoneField.setEditable(false);
            pointsField.setEditable(false);
        }

        searchCutsomerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                ObservableList<String> customerSuggestions = searchCustomerByPhone(newValue);
                if (customerSuggestions.isEmpty()) {
                    customerSuggestions.add("Chưa có khách hàng đó");
                }
                customerListView.setItems(customerSuggestions);
                customerListView.setVisible(true);
            } else {
                customerListView.setVisible(false);
            }
        });

        customerListView.setOnMouseClicked(event -> {
            String selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null && !selectedCustomer.equals("Chưa có khách hàng đó")) {
                populateCustomerFields(selectedCustomer);
                String[] parts = selectedCustomer.split(" - ");
                if (parts.length == 3) {
                    // Tạo đối tượng Customer và cập nhật TabContentController
                    Customer selectedCustomerObj = new Customer(parts[0], parts[1], Integer.parseInt(parts[2].replace(" points", "")));
                    tabContentController.saveCustomer(selectedCustomerObj);  // Cập nhật đối tượng Customer trong TabContentController
                    tabContentController.setCustomerName(selectedCustomerObj.getName());  // Cập nhật tên khách hàng trên giao diện
                }
            }
            customerListView.setVisible(false);
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

        updateCustomerInfor.setOnAction(event -> handleUpdateCustomerInfo());
        clearBtn.setOnAction(event -> handleClearFields());
    }

    private void handleClearFields() {
        nameField.clear();
        phoneField.clear();
        pointsField.clear();

        if (tabContentController != null) {
            tabContentController.setCustomerName("");
        }
        nameField.setEditable(true);
        phoneField.setEditable(true);
        pointsField.setEditable(false);
    }

    private ObservableList<String> searchCustomerByPhone(String phone) {
        ObservableList<String> suggestions = FXCollections.observableArrayList();
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT * FROM customer WHERE phone LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String customerPhone = resultSet.getString("phone");
                int points = resultSet.getInt("points");
                suggestions.add(name + " - " + customerPhone + " - " + points + " points");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    private void populateCustomerFields(String customerInfo) {
        String[] parts = customerInfo.split(" - ");
        if (parts.length == 3) {
            String name = parts[0];
            String phone = parts[1];
            int points = Integer.parseInt(parts[2].replace(" points", ""));

            CustomerState.setName(name);
            CustomerState.setPhone(phone);
            CustomerState.setPoints(points);

            nameField.setText(name);
            phoneField.setText(phone);
            pointsField.setText(String.valueOf(points));

            nameField.setEditable(false);
            phoneField.setEditable(false);
            pointsField.setEditable(false);
        }
    }


    @FXML
    private void handleUpdateCustomerInfo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Chỉnh sửa thông tin khách hàng");
        alert.setHeaderText(null);
        alert.setContentText("Chọn trường bạn muốn chỉnh sửa: Name, Phone, Points");

        ButtonType buttonName = new ButtonType("Name");
        ButtonType buttonPhone = new ButtonType("Phone");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonName, buttonPhone, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonName) {
                nameField.setEditable(true);
            } else if (result.get() == buttonPhone) {
                phoneField.setEditable(true);
            }
        }
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
            points = 0;
        } else {
            points = Integer.parseInt(pointsField.getText());
        }

        if (isPhoneDuplicate(phone)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thêm được khách hàng vì số điện thoại bị trùng");
            alert.showAndWait();
            return;
        }

        Customer customer = new Customer(name, phone, points);
        tabContentController.saveCustomer(customer);
        tabContentController.setCustomerName(name);
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private boolean isPhoneDuplicate(String phone) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT * FROM customer WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}