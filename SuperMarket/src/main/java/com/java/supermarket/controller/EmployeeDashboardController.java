package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.io.IOException;

public class EmployeeDashboardController implements Initializable {
    @FXML
    private BorderPane employeeForm;
    @FXML
    private AnchorPane headerEmployeeController;
    @FXML
    private Button staffCloseBtn;
    @FXML
    private Button staffMinimizeBtn;
    @FXML
    private TabPane billTabPane;

    private String employeeUsername;
    private int employeeId;

    public void setEmployeeData(int id, String username, String password) {
        this.employeeId = id;
        this.employeeUsername = username;
        this.password = password;
        initializeEmployeeDashboard();
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    private String password;
    private int tabCount = 1;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) employeeForm.getScene().getWindow();
        stage.setIconified(true);
    }

    private void setEmployeeNameFromUsernameForController(String username, TabContentController tabContentController) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT first_name, last_name FROM user WHERE BINARY username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                tabContentController.setEmployeeName(firstName, lastName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initializeEmployeeDashboard() {
        // Ensure the "+" tab is always the last tab and does not trigger when selected
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        addTab.getStyleClass().add("tab-plus");
        addTab.setOnSelectionChanged(event -> {
            if (addTab.isSelected()) {
                addNewBillTab();
            }
        });

        boolean tabExists = billTabPane.getTabs().stream().anyMatch(tab -> "+".equals(tab.getText()));
        if (!tabExists) {
            billTabPane.getTabs().add(addTab);
        }
    }

    @FXML
    private void addNewBillTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/supermarket/bodyEmployeeDashBoard.fxml"));
            AnchorPane tabContent = loader.load();
            TabContentController tabContentController = loader.getController();
            tabContent.setUserData(tabContentController);


            String username = this.getEmployeeUsername();
            int employeeId = this.getEmployeeId();
            tabContentController.setEmployeeData(employeeId, username, this.password);

            Tab newTab = new Tab("Hoá đơn " + tabCount++);


            newTab.setContent(tabContent);
            newTab.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận xóa");
                alert.setHeaderText("Bạn chắc chắn muốn xóa hóa đơn này chứ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    billTabPane.getTabs().remove(newTab);
                } else {
                    event.consume();
                }
            });

            int index = billTabPane.getTabs().size() - 1;
            billTabPane.getTabs().add(index, newTab);
            billTabPane.getSelectionModel().select(newTab);

            setEmployeeNameFromUsernameForController(username, tabContentController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TabContentController getTabContentController(Tab tab) {
        return (TabContentController) tab.getContent().getUserData();
    }
}
