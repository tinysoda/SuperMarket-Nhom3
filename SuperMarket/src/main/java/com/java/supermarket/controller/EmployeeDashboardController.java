package com.java.supermarket.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class EmployeeDashboardController implements Initializable {
    @FXML
    private ComboBox<?> Purchase_Product;

    @FXML
    private Button close;

    @FXML
    private BorderPane employeeForm;

    @FXML
    private Button logout;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_PayBtn;

    @FXML
    private TextField purchase_ProductID;

    @FXML
    private Spinner<?> purchase_Quantity;

    @FXML
    private Button purchase_ReceptionBtn;

    @FXML
    private Label purchase_Total;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private TextField purchase_category;

    @FXML
    private TableColumn<?, ?> purchase_col_Product;

    @FXML
    private TableColumn<?, ?> purchase_col_Quantity;

    @FXML
    private TableColumn<?, ?> purchase_col_category;

    @FXML
    private TableColumn<?, ?> purchase_col_price;

    @FXML
    private TableColumn<?, ?> purchase_col_productID;

    @FXML
    private Label purchase_employee;

    @FXML
    private TableView<?> purchase_table_view;

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void minimize(ActionEvent event) {

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) employeeForm.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
