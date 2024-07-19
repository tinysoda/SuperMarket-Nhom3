package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.Product;
import com.java.supermarket.object.ProductStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import com.java.supermarket.object.Employee;

public class AdminDashboardController implements Initializable {

    @FXML
    private Button Close;

    @FXML
    private Button Minimize;

    @FXML
    private Button adminAddUserBtn;

    @FXML
    private AnchorPane adminBillManForm;

    @FXML
    private TextField adminBillLookUpTF;

    @FXML
    private Button adminBillManBtn;

    @FXML
    private TableView<?> adminBillProTable;

    @FXML
    private TableView<?> adminBillTable;

    @FXML
    private Button adminClearUserBtn;

    @FXML
    private Label adminDailyIncomeLabel;

    @FXML
    private Button adminDeleteUserBtn;

    @FXML
    private TextField adminFnameTF;

    @FXML
    private BorderPane adminForm;

    @FXML
    private AreaChart<?, ?> adminIncomeChart;

    @FXML
    private TextField adminLnameTF;

    @FXML
    private Button adminLogoutBtn;

    @FXML
    private TextField adminPhoneTF;

    @FXML
    private Button adminProAddBtn;

    @FXML
    private TextField adminProCatTF;

    @FXML
    private Button adminProClearBtn;

    @FXML
    private Button adminProDelBtn;

    @FXML
    private TextField adminProDescTF;

    @FXML
    private AnchorPane adminProForm;

    @FXML
    private TextField adminProLookUpTF;

    @FXML
    private Button adminProManBtn;

    @FXML
    private HBox adminProManForm;

    @FXML
    private TextField adminProNameTF;

    @FXML
    private TextField adminProPriceTF;

    @FXML
    private TextField adminProQuanityTF;

    @FXML
    private TableView<Product> adminProTable;

    @FXML
    private Button adminProUpdateBtn;

    @FXML
    private ComboBox<String> adminRoleCB;

    @FXML
    private TextField adminStaffLookUpTF;

    @FXML
    private Button adminStaffManBtn;

    @FXML
    private HBox adminStaffManForm;

    @FXML
    private Label adminStaffNoLabel;

    @FXML
    private Button adminStatBtn;

    @FXML
    private AnchorPane adminStatForm;

    @FXML
    private Label adminTitleLabel;

    @FXML
    private Label adminTotalIncomLabel;

    @FXML
    private Button adminUpdateUserBtn;

    @FXML
    private TextField adminUsernameTF;

    @FXML
    private TableView<Employee> admin_StaffTable;

    @FXML
    private AnchorPane admin_card2;

    @FXML
    private AnchorPane card1;

    @FXML
    private AnchorPane card3;

    @FXML
    private TableColumn<?, ?> col_bill_id;

    @FXML
    private TableColumn<?, ?> col_created_at;

    @FXML
    private TableColumn<Product, String> col_pro_cat;

    @FXML
    private TableColumn<Product, String> col_pro_desc;

    @FXML
    private TableColumn<?, ?> col_pro_id;

    @FXML
    private TableColumn<Product, String> col_pro_name;

    @FXML
    private TableColumn<Product, String> col_pro_price;

    @FXML
    private TableColumn<Product, String> col_pro_quantity;

    @FXML
    private TableColumn<Product, String> col_pro_status;

    @FXML
    private TableColumn<Product, String> col_pro_stt;

    @FXML
    private TableColumn<?, ?> col_pro_total;

    @FXML
    private TableColumn<Employee, String> col_staff_fname;

    @FXML
    private TableColumn<Employee, Integer> col_staff_id;

    @FXML
    private TableColumn<Employee, String> col_staff_lname;

    @FXML
    private TableColumn<Employee, String> col_staff_phone;

    @FXML
    private TableColumn<Employee, String> col_staff_role;

    @FXML
    private TableColumn<Employee, String> col_staff_username;

    @FXML
    private TableColumn<?, ?> col_total_amount;

    private Double x;
    private Double y;

    private void hideAllForm() {
        adminStatForm.setVisible(false);
        adminStatBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminStaffManForm.setVisible(false);
        adminStaffManBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminProManForm.setVisible(false);
        adminProManBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminBillManForm.setVisible(false);
        adminBillManBtn.setStyle("-fx-background-color: TRANSPARENT");
    }

    public void switchForm(ActionEvent event) {
        hideAllForm();
        if (event.getSource() == adminStatBtn) {
            adminTitleLabel.setText("Quản lý - Thống kê");
            adminStatForm.setVisible(true);
            adminStatBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        } else if (event.getSource() == adminStaffManBtn) {
            adminTitleLabel.setText("Quản lý - Nhân viên");
            adminStaffManForm.setVisible(true);
            adminStaffManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        } else if (event.getSource() == adminProManBtn) {
            adminTitleLabel.setText("Quản lý - Sản phẩm");
            adminProManForm.setVisible(true);
            adminShowProduct();
            adminProLookUp();
            adminProManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        } else if (event.getSource() == adminBillManBtn) {
            adminTitleLabel.setText("Quản lý - Hoá đơn");
            adminBillManForm.setVisible(true);
            adminBillManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) adminForm.getScene().getWindow();
        stage.setIconified(true);
    }

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Đăng xuất?");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                adminLogoutBtn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.initStyle(StageStyle.TRANSPARENT);
                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });
                stage.setScene(scene);
                stage.show();

            } else return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;

    public ObservableList<Employee> adminEmployeeList() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String sql = "select * from user";
        con = DBUtils.getConnection();
        try {
            Employee employee;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("password"));
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }


    public void adminShowEmployee() {
        try {
            ObservableList<Employee> employeeList = adminEmployeeList();

            col_staff_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_staff_fname.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            col_staff_lname.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            col_staff_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            col_staff_role.setCellValueFactory(new PropertyValueFactory<>("role"));
            col_staff_username.setCellValueFactory(new PropertyValueFactory<>("username"));

            admin_StaffTable.setItems(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adminEmployeeSelect() {
        Employee employee = admin_StaffTable.getSelectionModel().getSelectedItem();
        if (employee != null) {
            adminFnameTF.setText(employee.getFirst_name());
            adminLnameTF.setText(employee.getLast_name());
            adminPhoneTF.setText(employee.getPhone());
            adminRoleCB.setValue(employee.getRole());
            adminUsernameTF.setText(employee.getUsername());
        }
    }

    public void adminStaffLookUp() {
        try {
            ObservableList<Employee> employeeList = adminEmployeeList();
            FilteredList<Employee> filter = new FilteredList<>(employeeList, e -> true);

            adminStaffLookUpTF.textProperty().addListener((observable, oldValue, newValue) -> {
                filter.setPredicate(predicateEmployee -> {
                    if (newValue.isEmpty() || newValue == null) {
                        return true;
                    }
                    String searchKey = newValue.toLowerCase();
                    return predicateEmployee.getFirst_name().toLowerCase().contains(searchKey) ||
                            predicateEmployee.getLast_name().toLowerCase().contains(searchKey) ||
                            predicateEmployee.getPhone().toLowerCase().contains(searchKey) ||
                            predicateEmployee.getRole().toLowerCase().contains(searchKey) ||
                            predicateEmployee.getUsername().toLowerCase().contains(searchKey);
                });
            });

            SortedList<Employee> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(admin_StaffTable.comparatorProperty());
            admin_StaffTable.setItems(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Product> adminProductList() {
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        String sql = "select * from product";
        con = DBUtils.getConnection();
        try {
            Product product;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                String category = rs.getString("category");
                Float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                String productStat = rs.getString("status");
                ProductStatus productStatus = ProductStatus.valueOf(productStat);
                product = new Product(id, name, desc, category, price, quantity, productStatus);
                productsList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return productsList;
    }

    public void adminEmployeeAdd() {
        String insertQuery = "INSERT INTO user (first_name, last_name, phone, role, username, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(insertQuery)) {

            if (adminFnameTF.getText().isEmpty() || adminLnameTF.getText().isEmpty() ||
                    adminPhoneTF.getText().isEmpty() || adminUsernameTF.getText().isEmpty() ||
                    adminRoleCB.getSelectionModel().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin nhân viên");
                alert.showAndWait();
            } else {
                ps.setString(1, adminFnameTF.getText());
                ps.setString(2, adminLnameTF.getText());
                ps.setString(3, adminPhoneTF.getText());
                ps.setString(4, adminRoleCB.getValue());
                ps.setString(5, adminUsernameTF.getText());
                ps.setString(6, "default_password"); // Bạn có thể thay đổi hoặc mã hóa mật khẩu sau
                ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm nhân viên " + adminFnameTF.getText() + " thành công");
                alert.showAndWait();

                adminShowEmployee();
                adminEmployeeClear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adminEmployeeUpdate() {
        String updateQuery = "UPDATE user SET first_name = ?, last_name = ?, phone = ?, role = ?, username = ? WHERE id = ?";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(updateQuery)) {

            Employee selectedEmployee = admin_StaffTable.getSelectionModel().getSelectedItem();
            int selectedIndex = admin_StaffTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex == -1 || selectedEmployee == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Chọn nhân viên cần cập nhật");
                alert.showAndWait();
                return;
            }

            String firstName = adminFnameTF.getText();
            String lastName = adminLnameTF.getText();
            String phone = adminPhoneTF.getText();
            String role = adminRoleCB.getValue();
            String username = adminUsernameTF.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || role == null || username.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin nhân viên");
                alert.showAndWait();
            } else {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, phone);
                ps.setString(4, role);
                ps.setString(5, username);
                ps.setInt(6, selectedEmployee.getId());
                ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật nhân viên " + firstName + " thành công");
                alert.showAndWait();

                selectedEmployee.setFirst_name(firstName);
                selectedEmployee.setLast_name(lastName);
                selectedEmployee.setPhone(phone);
                selectedEmployee.setRole(role);
                selectedEmployee.setUsername(username);

                adminShowEmployee();
                admin_StaffTable.getSelectionModel().select(selectedIndex);
                adminEmployeeClear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void adminEmployeeDelete() {
        String deleteQuery = "DELETE FROM user WHERE id = ?";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(deleteQuery)) {

            Employee selectedEmployee = admin_StaffTable.getSelectionModel().getSelectedItem();
            int selectedIndex = admin_StaffTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex == -1 || selectedEmployee == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Chọn nhân viên cần xóa");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa Nhân Viên");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn xóa nhân viên " + selectedEmployee.getFirst_name() + " ?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                ps.setInt(1, selectedEmployee.getId());
                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Xóa nhân viên thành công");
                alert.showAndWait();

                adminShowEmployee();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adminEmployeeClear() {
        adminFnameTF.clear();
        adminLnameTF.clear();
        adminPhoneTF.clear();
        adminUsernameTF.clear();
        adminRoleCB.getSelectionModel().clearSelection();
    }




    //Product start
    private ObservableList<Product> adminProductList2;

    public void adminShowProduct() {
        adminProductList2 = adminProductList();
        col_pro_stt.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        col_pro_name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        col_pro_cat.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        col_pro_desc.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        col_pro_price.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        col_pro_quantity.setCellValueFactory(new PropertyValueFactory<Product, String>("quantity"));
        col_pro_status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());


        adminProTable.setItems(adminProductList2);
    }

    public void adminProSelect() {
        Product product = adminProTable.getSelectionModel().getSelectedItem();
        int index = adminProTable.getSelectionModel().getSelectedIndex();

        if (index == -1) {
            return;
        }
        adminProNameTF.setText(product.getName());
        adminProDescTF.setText(product.getDescription());
        adminProCatTF.setText(product.getCategory());
        adminProPriceTF.setText(product.getPrice().toString());
        adminProQuanityTF.setText(String.valueOf(product.getQuantity()));

    }

    public void adminProAdd() {
        String insertQuery = "insert into product (name,description,category,price,quantity) values(?,?,?,?,?)";
        con = DBUtils.getConnection();
        try {
            Alert alert;
            if (adminProNameTF.getText().isEmpty() || adminProPriceTF.getText().isEmpty() || adminProQuanityTF.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin sản phẩm");
                alert.showAndWait();
            } else {
                ps = con.prepareStatement(insertQuery);
                ps.setString(1, adminProNameTF.getText());
                ps.setString(2, adminProDescTF.getText());
                ps.setString(3, adminProCatTF.getText());
                ps.setFloat(4, Float.parseFloat(adminProPriceTF.getText()));
                ps.setInt(5, Integer.parseInt(adminProQuanityTF.getText()));
                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm sản phẩm "+adminProNameTF.getText()+" thành công");
                alert.showAndWait();

                adminShowProduct();
                adminProClear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void adminProClear(){
        adminProNameTF.clear();
        adminProDescTF.clear();
        adminProCatTF.clear();
        adminProPriceTF.clear();
        adminProQuanityTF.clear();
    }

    public void adminProUpdate() {
        String updateQuery = "UPDATE product SET name = ?, description = ?, category = ?, price = ?, quantity = ? WHERE id = ?";
        con = DBUtils.getConnection();

        Product selectedProduct = adminProTable.getSelectionModel().getSelectedItem();
        int selectedIndex = adminProTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Chọn sản phẩm cần cập nhật");
            alert.showAndWait();
            return;
        }

        try {
            Alert alert;
            if (adminProNameTF.getText().isEmpty() || adminProPriceTF.getText().isEmpty() || adminProQuanityTF.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin sản phẩm");
                alert.showAndWait();
            } else {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, adminProNameTF.getText());
                ps.setString(2, adminProDescTF.getText());
                ps.setString(3, adminProCatTF.getText());
                ps.setFloat(4, Float.parseFloat(adminProPriceTF.getText()));
                ps.setInt(5, Integer.parseInt(adminProQuanityTF.getText()));
                ps.setInt(6, selectedProduct.getId());
                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật sản phẩm " + adminProNameTF.getText() + " thành công");
                alert.showAndWait();

                // Update the selected product in the list with the new values
                selectedProduct.setName(adminProNameTF.getText());
                selectedProduct.setDescription(adminProDescTF.getText());
                selectedProduct.setCategory(adminProCatTF.getText());
                selectedProduct.setPrice(Float.parseFloat(adminProPriceTF.getText()));
                selectedProduct.setQuantity(Integer.parseInt(adminProQuanityTF.getText()));

                // Refresh the table with the updated list
                adminShowProduct();
                adminProTable.getSelectionModel().select(selectedIndex); // Reselect the previously selected item
                adminProClear();
                adminProLookUp();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void adminProLookUp() {
        adminProLookUpTF.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue != null ? newValue.toLowerCase() : "";
            ObservableList<Product> productList = FXCollections.observableArrayList();

            try (Connection con = DBUtils.getConnection()) {
                String query;
                if (searchKey.isEmpty()) {
                    query = "SELECT * FROM product";
                } else {
                    query = "SELECT * FROM product WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ? OR LOWER(category) LIKE ?";
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    if (!searchKey.isEmpty()) {
                        ps.setString(1, "%" + searchKey + "%");
                        ps.setString(2, "%" + searchKey + "%");
                        ps.setString(3, "%" + searchKey + "%");
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Product product = new Product();
                            product.setId(rs.getInt("id"));
                            product.setName(rs.getString("name"));
                            product.setDescription(rs.getString("description"));
                            product.setCategory(rs.getString("category"));
                            product.setPrice(rs.getFloat("price"));
                            product.setQuantity(rs.getInt("quantity"));
                            productList.add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Update the table items without clearing the selection
            adminProTable.setItems(productList);
        });
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminRoleCB.setItems(FXCollections.observableArrayList("Manager", "Employee"));
        hideAllForm();
        adminStatForm.setVisible(true);
        adminStatBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        adminProNameTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
        adminProDescTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
        adminProCatTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
        adminProPriceTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
        adminProQuanityTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
        admin_StaffTable.setOnMouseClicked(event -> adminEmployeeSelect());
        adminShowEmployee();
        adminStaffLookUp();
    }

    //Focus change on Enter
    private void handleTextFieldEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            TextField sourceTextField = (TextField) event.getSource();
            switch (sourceTextField.getId()) {
                case "adminProNameTF":
                    adminProDescTF.requestFocus();
                    break;
                case "adminProDescTF":
                    adminProCatTF.requestFocus();
                    break;
                case "adminProCatTF":
                    adminProPriceTF.requestFocus();
                    break;
                case "adminProPriceTF":
                    adminProQuanityTF.requestFocus();
                    break;
                case "adminProQuanityTF":
                    // Move focus back to the first field or any other desired behavior
                    adminProNameTF.requestFocus();
                    break;
                default:
                    break;
            }
        }
    }

    // Your other methods like adminProLookUp, adminProUpdate, etc. go here
    // ...

}