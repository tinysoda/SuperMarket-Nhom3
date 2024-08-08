package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import java.text.NumberFormat;
import java.util.Locale;


public class AdminDashboardController implements Initializable {

    @FXML
    private AnchorPane employeeCard;

    @FXML
    private AnchorPane inComeInDayCard;

    @FXML
    private AnchorPane totalInComeCard;

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
    private TableView<BillDetail> adminBillProTable;
    @FXML
    private TableColumn<BillDetail, Integer>  col_bill_pro_id;

    @FXML
    private TableColumn<BillDetail, String>  col_bill_pro_name;
    @FXML
    private TableColumn<BillDetail, Double>  col_bill_pro_price;
    @FXML
    private TableColumn<BillDetail, Integer>  col_bill_pro_quantity;
    @FXML
    private TableColumn<BillDetail, Double>  col_bill_pro_total;

    @FXML
    private TableColumn<BillDetail, Void> col_bill_detail_print;


    @FXML
    private TableView<Bill> adminBillTable;
    @FXML
    private TableColumn<Bill, Integer>  col_bill_bill_id;
    @FXML
    private TableColumn<Bill, Integer>  col_billing_staff_id;
    @FXML
    private TableColumn<Bill, String>  col_billing_staff_username;

    @FXML
    private TableColumn<Bill, String>  col_bill_customer_phone;
    @FXML
    private TableColumn<Bill, String>  col_bill_customer_name;
    @FXML
    private TableColumn<Bill, Double>  col_bill_total_amount;
    @FXML
    private TableColumn<Bill, Timestamp>  col_bill_created_at;

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
    private AreaChart<String, Double> adminIncomeLineChart;

    @FXML
    private PieChart adminIncomePieChart;

    @FXML
    private TextField adminLnameTF;

    @FXML
    private Button adminLogoutBtn;

    @FXML
    private TextField adminPhoneTF;

    @FXML
    private Button adminProAddBtn;

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
    private TableView<Employee> admin_StaffTable; // Ensure this is typed correctly

    @FXML
    private TableColumn<?, ?> col_bill_id;

    @FXML
    private TableColumn<?, ?> col_created_at;

    @FXML
    private TableColumn<Product, String> col_pro_cat;

    @FXML
    private TableColumn<Product, String> col_pro_desc;

    @FXML
    private TableColumn<Product, Integer> col_pro_id;

    @FXML
    private TableColumn<Product, String> col_pro_name;

    @FXML
    private TableColumn<Product, Double> col_pro_price;

    @FXML
    private TableColumn<Product, Integer> col_pro_quantity;

    @FXML
    private TableColumn<Product, String> col_pro_status;

    @FXML
    private TableColumn<Product, String> col_pro_stt;

    @FXML
    private TableColumn<?, ?> col_pro_total;

    @FXML
    private TableColumn<Employee, String> col_staff_fname;
@FXML
    private TableColumn<Bill, String> col_bill_staff_name;

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
    private ComboBox<Category> adminProCatTF;

    @FXML
    private HBox adminCategoryManForm;
    @FXML
    private Button adminCategoryAddBtn;
    @FXML
    private Button adminCategoryBtn;
    @FXML
    private Button adminCategoryClearBtn;
    @FXML
    private TextField adminCategoryDescTF;
    @FXML
    private AnchorPane adminCategoryForm;
    @FXML
    private TextField adminCategoryLookUpTF;
    @FXML
    private TextField adminCategoryNameTF;
    @FXML
    private TableView<Category> adminCategoryTable;
    @FXML
    private Button adminCategoryUpdateBtn;
    @FXML
    private TableColumn<Category, String> col_category_desc;
    @FXML
    private TableColumn<Category, Integer> col_category_id;
    @FXML
    private TableColumn<Category, String> col_category_name;

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

        adminCategoryManForm.setVisible(false);
        adminCategoryBtn.setStyle("-fx-background-color: TRANSPARENT");

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
            adminProManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
            adminShowProduct();
            adminProLookUp();
        } else if (event.getSource() == adminBillManBtn) {
            adminTitleLabel.setText("Quản lý - Hoá đơn");
            adminBillManForm.setVisible(true);
            adminBillManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
            adminShowBill();
            adminBillLookUp();
        }else if (event.getSource() == adminCategoryBtn) {
            adminTitleLabel.setText("Quản lý - Danh mục");
            adminCategoryManForm.setVisible(true);
            adminCategoryBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
            adminShowCategory();
            adminCategoryLookUp();
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

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                Stage stage = (Stage) adminLogoutBtn.getScene().getWindow();
                stage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/com/java/supermarket/login.fxml"));
                Scene scene = new Scene(root);
                Stage loginStage = new Stage();
                loginStage.setScene(scene);
                loginStage.initStyle(StageStyle.TRANSPARENT);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    loginStage.setX(event.getScreenX() - x);
                    loginStage.setY(event.getScreenY() - y);
                    loginStage.setOpacity(0.8);
                });
                root.setOnMouseReleased((MouseEvent event) -> {
                    loginStage.setOpacity(1);
                });

                loginStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Employee

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
            e.printStackTrace();  // In ra lỗi để dễ dàng kiểm tra
        }
    }

    public void adminEmployeeSelect() {
        Employee employee = admin_StaffTable.getSelectionModel().getSelectedItem();
        if (employee != null) {
            adminFnameTF.setText(employee.getFirst_name());
            adminLnameTF.setText(employee.getLast_name());
            adminPhoneTF.setText(employee.getPhone());
            adminUsernameTF.setText(employee.getUsername());

        }
    }

    public int countEmployees() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM user";
        con = DBUtils.getConnection();
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count-1;
    }

    //bill start
    public ObservableList<Bill> adminBillList() {
        ObservableList<Bill> billList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bill";
        con = DBUtils.getConnection();
        try {
            Bill bill;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setUserId(rs.getInt("user_id"));
                bill.setUserFName(rs.getString("user_name"));
                bill.setCustomerPhone(rs.getString("customer_phone"));
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setCreatedAt(rs.getTimestamp("created_at"));
                billList.add(bill);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return billList;
    }

    public void adminShowBill() {
        try {
            ObservableList<Bill> billList = adminBillList();
            col_bill_bill_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_billing_staff_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
            col_billing_staff_username.setCellValueFactory(new PropertyValueFactory<>("userFName"));
            col_bill_customer_phone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            col_bill_customer_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            col_bill_total_amount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            col_bill_created_at.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            adminBillTable.setItems(billList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ObservableList<BillDetail> adminProductsOfBill(int billId) {
        ObservableList<BillDetail> billDetailList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM billdetail WHERE bill_id = ?";
        con = DBUtils.getConnection();
        try {
            BillDetail billDetail = null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            rs = ps.executeQuery();
            while (rs.next()) {
                billDetail = new BillDetail(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                billDetailList.add(billDetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return billDetailList;
    }
    public void adminShowProBill(int billId) {
        try {
            ObservableList<BillDetail> billDetailList = adminProductsOfBill(billId);
            col_bill_pro_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
            col_bill_pro_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
            col_bill_pro_price.setCellValueFactory(new PropertyValueFactory<>("price"));
            col_bill_pro_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            col_bill_pro_total.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BillDetail, Double>, ObservableValue<Double>>() {
                @Override
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<BillDetail, Double> param) {
                    BillDetail billDetail = param.getValue();
                    if (billDetail != null) {
                        Double total = billDetail.getPrice() * billDetail.getQuantity();
                        return new SimpleDoubleProperty(total).asObject();
                    }
                    return new SimpleDoubleProperty(0.0).asObject();
                }
            });

            col_bill_detail_print.setCellFactory(col -> new TableCell<BillDetail, Void>() {
                private final Button printButton = new Button("BILL");

                {
                    // Action when the button is clicked
                    printButton.setOnAction(event -> {
                        // Get the selected Bill from the adminBillTable
                        Bill selectedBill = adminBillTable.getSelectionModel().getSelectedItem();
                        if (selectedBill != null) {
                            // Display the bill ID in the console
                            System.out.println("Selected Bill ID for printing: " + selectedBill.getId());

                            // Open the bill PDF using the selected bill ID
                            openBillPDF(selectedBill.getId());
                        } else {
                            System.out.println("No bill selected, cannot print.");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(printButton);
                    }
                }
            });

            adminBillProTable.setItems(billDetailList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBillPDF(int billId) {
        try {
            // Construct the file path for the bill PDF
            String filePath = "bills/bill_" + billId + ".pdf";

            // Open the PDF file with the system's default PDF viewer
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open the bill PDF.");
            alert.showAndWait();
        }
    }

    public void adminBillSelect() {
        Bill bill = adminBillTable.getSelectionModel().getSelectedItem();
        if (bill != null) {
            adminShowProBill(bill.getId());
        }
    }
    public void adminBillLookUp() {
        adminBillLookUpTF.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue != null ? newValue.toLowerCase() : "";
            ObservableList<Bill> billList = FXCollections.observableArrayList();

            try (Connection con = DBUtils.getConnection()) {
                String query;
                if (searchKey.isEmpty()) {
                    query = "SELECT * FROM bill";
                } else {
                    query = "SELECT * FROM bill WHERE id LIKE ? OR customer_phone LIKE ? OR user_id LIKE ? OR total_amount LIKE ? OR (CAST(created_at AS CHAR) LIKE ? )";
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    if (!searchKey.isEmpty()) {
                        ps.setString(1, "%" + searchKey + "%");
                        ps.setString(2, "%" + searchKey + "%");
                        ps.setString(3, "%" + searchKey + "%");
                        ps.setString(4, "%" + searchKey + "%");
                        ps.setString(5, "%" + searchKey + "%");

                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Bill bill = new Bill(null, 0, 0, null);
                            bill.setId(rs.getInt("id"));
                            bill.setUserId(rs.getInt("user_id"));
                            bill.setCustomerPhone(rs.getString("customer_phone"));
                            bill.setTotalAmount(rs.getDouble("total_amount"));
                            bill.setCreatedAt(rs.getTimestamp("created_at"));
                            billList.add(bill);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            adminBillTable.setItems(billList);
        });
    }
    //staff
    public void adminStaffLookUp() {
        adminStaffLookUpTF.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue != null ? newValue.toLowerCase() : "";
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();

            try (Connection con = DBUtils.getConnection()) {
                String query;
                if (searchKey.isEmpty()) {
                    query = "SELECT * FROM user";
                } else {
                    query = "SELECT * FROM user WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    if (!searchKey.isEmpty()) {
                        ps.setString(1, "%" + searchKey + "%");
                        ps.setString(2, "%" + searchKey + "%");
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Employee employee = new Employee(
                                    rs.getInt("id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("phone"),
                                    rs.getString("role"),
                                    rs.getString("username"),
                                    rs.getString("password")
                            );
                            employeeList.add(employee);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            admin_StaffTable.setItems(employeeList);
        });
    }



    public void adminEmployeeAdd() {
        String insertQuery = "INSERT INTO user (first_name, last_name, phone, username, password) VALUES (?, ?, ?, ?, ?)";
        String checkUsernameQuery = "SELECT COUNT(*) FROM user WHERE username = ?";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkUsernameQuery);
             PreparedStatement insertPs = con.prepareStatement(insertQuery)) {

            if (adminFnameTF.getText().isEmpty() || adminLnameTF.getText().isEmpty() ||
                    adminPhoneTF.getText().isEmpty() || adminUsernameTF.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin nhân viên");
                alert.showAndWait();
            } else {
                // Check if username already exists
                checkPs.setString(1, adminUsernameTF.getText());
                ResultSet rs = checkPs.executeQuery();
                rs.next();
                int count = rs.getInt(1);

                if (count > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Tên đăng nhập đã tồn tại. Vui lòng chọn tên đăng nhập khác.");
                    alert.showAndWait();

                }else if(adminUsernameTF.getText().length()<3){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Tên đăng nhập phải dài hơn 3 kí tự");
                    alert.showAndWait();
                }else if (!adminPhoneTF.getText().matches("\\+?\\d+") ||
                        adminPhoneTF.getText().length() < 10 ||
                        adminPhoneTF.getText().length() > 13) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("SDT không đúng định dạng");
                    alert.showAndWait();
                }
                else {
                    insertPs.setString(1, adminFnameTF.getText());
                    insertPs.setString(2, adminLnameTF.getText());
                    insertPs.setString(3, adminPhoneTF.getText());

                    insertPs.setString(4, adminUsernameTF.getText());

                    // Encrypt the password
                    String encryptedPassword = encryptPassword("bigcstaff");
                    insertPs.setString(5, encryptedPassword);

                    insertPs.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Thêm nhân viên " + adminFnameTF.getText() + " thành công");
                    alert.showAndWait();

                    adminShowEmployee();
                    adminEmployeeClear();
                }
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi thêm nhân viên: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void adminEmployeeUpdate() {
        String updateQuery = "UPDATE user SET first_name = ?, last_name = ?, phone = ? WHERE id = ?";
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

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin nhân viên");
                alert.showAndWait();
            }
//            else if(adminUsernameTF.getText().length()<3){
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText(null);
//                alert.setContentText("Tên đăng nhập phải dài hơn 3 kí tự");
//                alert.showAndWait();}
             else if (!adminUsernameTF.getText().equals(selectedEmployee.getUsername())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không thể thay đổi Username");
                alert.showAndWait();}
            else if (!adminPhoneTF.getText().matches("\\+?\\d+") ||
                    adminPhoneTF.getText().length() < 10 ||
                    adminPhoneTF.getText().length() > 13) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("SDT không đúng định dạng");
                alert.showAndWait();
            } else {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, phone);

                ps.setInt(4, selectedEmployee.getId());
                ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật nhân viên " + firstName + " thành công");
                alert.showAndWait();

                selectedEmployee.setFirst_name(firstName);
                selectedEmployee.setLast_name(lastName);
                selectedEmployee.setPhone(phone);

                adminShowEmployee();
                admin_StaffTable.getSelectionModel().select(selectedIndex); // Chọn lại mục đã chọn trước đó
                adminEmployeeClear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void adminEmployeeDelete() {
//        String deleteQuery = "DELETE FROM user WHERE id = ?";
//
//        try (Connection con = DBUtils.getConnection();
//             PreparedStatement ps = con.prepareStatement(deleteQuery)) {
//
//            Employee selectedEmployee = admin_StaffTable.getSelectionModel().getSelectedItem();
//            int selectedIndex = admin_StaffTable.getSelectionModel().getSelectedIndex();
//
//            if (selectedIndex == -1 || selectedEmployee == null) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi");
//                alert.setHeaderText(null);
//                alert.setContentText("Chọn nhân viên cần xóa");
//                alert.showAndWait();
//                return;
//            }
//
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Xóa Nhân Viên");
//            alert.setHeaderText(null);
//            alert.setContentText("Bạn có chắc chắn muốn xóa nhân viên " + selectedEmployee.getFirst_name() + " ?");
//            Optional<ButtonType> option = alert.showAndWait();
//
//            if (option.get().equals(ButtonType.OK)) {
//                ps.setInt(1, selectedEmployee.getId());
//                ps.executeUpdate();
//
//                alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Thông báo");
//                alert.setHeaderText(null);
//                alert.setContentText("Xóa nhân viên thành công");
//                alert.showAndWait();
//
//                adminShowEmployee();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void adminEmployeeClear() {
        adminFnameTF.clear();
        adminLnameTF.clear();
        adminPhoneTF.clear();
        adminUsernameTF.clear();
    }

    //Category start
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    private void loadCategories() {
        try (Connection con = DBUtils.getConnection()) {
            String query = "SELECT * FROM category";
            try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                categoryList.clear();
                while (rs.next()) {
                    Category category = new Category(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
                    categoryList.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adminProCatTF.setItems(categoryList);
    }


    private ObservableList<Category> adminCategoryList() {
        ObservableList<Category> categoriesList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        con = DBUtils.getConnection();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Category category = new Category(id, name, description);
                categoriesList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return categoriesList;
    }

    private ObservableList<Category> adminCategoryList2;

    public void adminShowCategory() {
        adminCategoryList2 = adminCategoryList();
        col_category_id.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        col_category_name.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        col_category_desc.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));

        adminCategoryTable.setItems(adminCategoryList2);
    }

    public void adminCategoryLookUp() {
        adminCategoryLookUpTF.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchKey = newValue != null ? newValue.toLowerCase() : "";
            ObservableList<Category> categoryList = FXCollections.observableArrayList();

            try (Connection con = DBUtils.getConnection()) {
                String query;
                if (searchKey.isEmpty()) {
                    query = "SELECT * FROM category";
                } else {
                    query = "SELECT * FROM category WHERE LOWER(name) LIKE ? OR LOWER(description) LIKE ?";
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    if (!searchKey.isEmpty()) {
                        ps.setString(1, "%" + searchKey + "%");
                        ps.setString(2, "%" + searchKey + "%");
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Category category = new Category(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("description")
                            );
                            categoryList.add(category);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Update the table items without clearing the selection
            adminCategoryTable.setItems(categoryList);
        });
    }


    public void adminCateAdd() {
        String insertQuery = "INSERT INTO category (name, description) VALUES (?, ?)";
        con = DBUtils.getConnection();
        try {
            Alert alert;
            if (adminCategoryNameTF.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin danh mục");
                alert.showAndWait();
            } else {
                ps = con.prepareStatement(insertQuery);
                ps.setString(1, adminCategoryNameTF.getText());
                ps.setString(2, adminCategoryDescTF.getText());
                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm danh mục " + adminCategoryNameTF.getText() + " thành công");
                alert.showAndWait();
                loadCategories();
                adminShowCategory();
                adminCateClear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi thêm danh mục");
            alert.showAndWait();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void adminCateClear() {
        adminCategoryNameTF.clear();
        adminCategoryDescTF.clear();
    }

    public void adminCateUpdate() {
        String updateQuery = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        con = DBUtils.getConnection();

        Category selectedCategory = (Category) adminCategoryTable.getSelectionModel().getSelectedItem();
        int selectedIndex = adminCategoryTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Chọn danh mục cần cập nhật");
            alert.showAndWait();
            return;
        }

        try {
            Alert alert;
            if (adminCategoryNameTF.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin danh mục");
                alert.showAndWait();
            } else {
                ps = con.prepareStatement(updateQuery);
                ps.setString(1, adminCategoryNameTF.getText());
                ps.setString(2, adminCategoryDescTF.getText());
                ps.setInt(3, selectedCategory.getId());
                ps.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật danh mục " + adminCategoryNameTF.getText() + " thành công");
                alert.showAndWait();

                // Update the selected category in the list with the new values
                selectedCategory.setName(adminCategoryNameTF.getText());
                selectedCategory.setDescription(adminCategoryDescTF.getText());

                // Refresh the table with the updated list
                adminShowCategory();
                adminCategoryTable.getSelectionModel().select(selectedIndex); // Reselect the previously selected item
                adminCateClear();
                adminCategoryLookUp();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adminCategorySelect() {
        Category category = (Category) adminCategoryTable.getSelectionModel().getSelectedItem();
        int index = adminCategoryTable.getSelectionModel().getSelectedIndex();

        if (index == -1) {
            return;
        }
        adminCategoryNameTF.setText(category.getName());
        adminCategoryDescTF.setText(category.getDescription());
    }

    public void adminCategoryDelete() {
        String deleteQuery = "DELETE FROM category WHERE id = ?";
        con = DBUtils.getConnection();

        Category selectedCategory = (Category) adminCategoryTable.getSelectionModel().getSelectedItem();
        int selectedIndex = adminCategoryTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Chọn danh mục cần xoá");
            alert.showAndWait();
            return;
        }

        try {
            Alert alert;
            ps = con.prepareStatement(deleteQuery);
            ps.setInt(1, selectedCategory.getId());
            ps.executeUpdate();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Xoá danh mục " + selectedCategory.getName() + " thành công");
            alert.showAndWait();

            adminShowCategory();
            adminCateClear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Product start
    private ObservableList<Product> adminProductList() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT product.*, category.name as category_name FROM product LEFT JOIN category ON product.category_id = category.id";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"), null); // Assuming no description needed here
                Product product = new Product(id, name, description, category, price, quantity, ProductStatus.AVAILABLE);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }


    public ObservableList<Category> getCategoryList() {
        String sql = "SELECT * FROM category";
        con = DBUtils.getConnection();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Category category = new Category(id, name, description);
                categoryList.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return categoryList;
    }

    private ObservableList<Product> adminProductList2;

    public void adminShowProduct() {
        ObservableList<Product> productList = adminProductList();
        col_pro_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_pro_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_pro_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_pro_cat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getName()));

        col_pro_price.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPrice())
        );

        col_pro_quantity.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getQuantity())
        );

        col_pro_status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        adminProTable.setItems(productList);
    }



    public void adminProSelect() {
        Product product = adminProTable.getSelectionModel().getSelectedItem();
        if (product == null) return;
        adminProNameTF.setText(product.getName());
        adminProDescTF.setText(product.getDescription());
        adminProCatTF.setValue(product.getCategory());
        adminProPriceTF.setText(String.valueOf(product.getPrice()));
        adminProQuanityTF.setText(String.valueOf(product.getQuantity()));
    }



    public void adminProAdd() {
        String insertQuery = "INSERT INTO product (name, description, category_id, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(insertQuery)) {
            Alert alert;
            if (adminProNameTF.getText().isEmpty() || adminProPriceTF.getText().isEmpty() || adminProQuanityTF.getText().isEmpty() || adminProCatTF.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Điền đầy đủ thông tin sản phẩm");
                alert.showAndWait();
            } else {
                ps.setString(1, adminProNameTF.getText());
                ps.setString(2, adminProDescTF.getText());
                ps.setInt(3, adminProCatTF.getValue().getId());
                ps.setDouble(4, Double.parseDouble(adminProPriceTF.getText()));
                ps.setInt(5, Integer.parseInt(adminProQuanityTF.getText()));
                ps.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Thêm sản phẩm " + adminProNameTF.getText() + " thành công");
                alert.showAndWait();
                adminShowProduct(); // Cập nhật bảng sau khi thêm sản phẩm
                adminProClear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adminProClear() {
        adminProNameTF.clear();
        adminProDescTF.clear();
        adminProCatTF.getSelectionModel().clearSelection();
        adminProPriceTF.clear();
        adminProQuanityTF.clear();
    }


    public void adminProUpdate() {
        String updateQuery = "UPDATE product SET name = ?, description = ?, category_id = ?, price = ?, quantity = ? WHERE id = ?";
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
                ps.setInt(3, adminProCatTF.getValue().getId());
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
                selectedProduct.setCategory(adminProCatTF.getValue()); // Update ComboBox to use Category object
                selectedProduct.setPrice(Double.parseDouble(adminProPriceTF.getText()));
                selectedProduct.setQuantity(Integer.parseInt(adminProQuanityTF.getText()));
                selectedProduct.setStatus((selectedProduct.getQuantity() > 0) ? ProductStatus.AVAILABLE : ProductStatus.DELETED);
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
                    query = "SELECT product.*, category.name as category_name, category.description as category_description FROM product LEFT JOIN category ON product.category_id = category.id";
                } else {
                    query = "SELECT product.*, category.name as category_name, category.description as category_description FROM product LEFT JOIN category ON product.category_id = category.id WHERE LOWER(product.name) LIKE ? OR LOWER(product.description) LIKE ? OR LOWER(category.name) LIKE ?";
                }

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    if (!searchKey.isEmpty()) {
                        ps.setString(1, "%" + searchKey + "%");
                        ps.setString(2, "%" + searchKey + "%");
                        ps.setString(3, "%" + searchKey + "%");
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int categoryId = rs.getInt("category_id");
                            String categoryName = rs.getString("category_name");
                            String categoryDescription = rs.getString("category_description");
                            Category category = new Category(categoryId, categoryName, categoryDescription);

                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            String description = rs.getString("description");
                            double price = rs.getDouble("price");
                            int quantity = rs.getInt("quantity");
                            ProductStatus status = rs.getBoolean("is_deleted") ? ProductStatus.DELETED : ProductStatus.AVAILABLE;

                            Product product = new Product(id, name, description, category, price, quantity, status);
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
        try {
            hideAllForm();
            adminStatForm.setVisible(true);
            adminStatBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
            adminProNameTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
            adminProDescTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
            adminProCatTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
            adminProPriceTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
            adminProQuanityTF.setOnKeyPressed(this::handleTextFieldEnterPressed);
            admin_StaffTable.setOnMouseClicked(event -> adminEmployeeSelect());
            adminBillTable.setOnMouseClicked(event -> adminBillSelect());
            adminStaffNoLabel.setText(String.valueOf(countEmployees()));
            adminShowEmployee();
            adminStaffLookUp();
            loadCategories();
            adminShowCategory();
            adminCategoryLookUp();
            adminShowProduct(); // Khởi tạo bảng sản phẩm
            adminProCatTF.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getName() : "";
                }

                @Override
                public Category fromString(String string) {
                    return null; // No need to implement
                }
            });

            updateIncomeLabels(); // Add this line to update the income labels and chart
            updateIncomeLineChart();
            updateIncomePieChart();

            col_bill_total_amount.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double object) {
                    return formatCurrency(object);
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            col_pro_price.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double object) {
                    return formatCurrency(object);
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            col_bill_pro_price.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double object) {
                    return formatCurrency(object);
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            col_bill_pro_total.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
                @Override
                public String toString(Double object) {
                    return formatCurrency(object);
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(string).doubleValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while initializing the dashboard: " + e.getMessage());
            alert.showAndWait();
        }
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


    public void updateIncomeLabels() {
        updateDailyIncome();
        updateTotalIncome();
        updateIncomeLineChart();
    }

    public void updateDailyIncome() {
        String sql = "SELECT SUM(total_amount) AS daily_income FROM bill WHERE DATE(created_at) = CURDATE()";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double dailyIncome = rs.getDouble("daily_income");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                adminDailyIncomeLabel.setText(currencyFormat.format(dailyIncome));
            } else {
                adminDailyIncomeLabel.setText("0 ₫");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalIncome() {
        String sql = "SELECT SUM(total_amount) AS total_income FROM bill";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double totalIncome = rs.getDouble("total_income");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                adminTotalIncomLabel.setText(currencyFormat.format(totalIncome));
            } else {
                adminTotalIncomLabel.setText("0 ₫");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIncomeLineChart() {
        String sql = "SELECT DATE(created_at) AS date, SUM(total_amount) AS daily_income FROM bill GROUP BY DATE(created_at)";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            XYChart.Series<String, Double> series = new XYChart.Series<>();
            while (rs.next()) {
                String date = rs.getString("date");
                double dailyIncome = rs.getDouble("daily_income");
                series.getData().add(new XYChart.Data<>(date, dailyIncome));
            }

            // Clear existing data and add new series
            adminIncomeLineChart.getData().clear();
            adminIncomeLineChart.getData().add(series);

            // Optionally set a title for the chart
            adminIncomeLineChart.setTitle("Income Over Time");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateIncomePieChart() {
        String totalIncomeSql = "SELECT SUM(total_amount) AS total_income FROM bill";
        String categoryIncomeSql = "SELECT c.name AS category_name, SUM(b.total_amount) AS category_income " +
                "FROM bill b " +
                "JOIN billdetail bd ON b.id = bd.bill_id " +
                "JOIN product p ON bd.product_id = p.id " +
                "JOIN category c ON p.category_id = c.id " +
                "GROUP BY c.name";

        double totalIncome = 0;
        try (Connection con = DBUtils.getConnection();
             PreparedStatement totalIncomePs = con.prepareStatement(totalIncomeSql);
             PreparedStatement categoryIncomePs = con.prepareStatement(categoryIncomeSql);
             ResultSet totalIncomeRs = totalIncomePs.executeQuery();
             ResultSet categoryIncomeRs = categoryIncomePs.executeQuery()) {

            // Tính tổng doanh thu
            if (totalIncomeRs.next()) {
                totalIncome = totalIncomeRs.getDouble("total_income");
            }

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Thêm dữ liệu vào PieChart và tính toán phần trăm
            while (categoryIncomeRs.next()) {
                String category = categoryIncomeRs.getString("category_name");
                double categoryIncome = categoryIncomeRs.getDouble("category_income");
                double percentage = (categoryIncome / totalIncome) * 100;
                pieChartData.add(new PieChart.Data(category + " (" + String.format("%.2f%%", percentage) + ")", categoryIncome));
            }

            adminIncomePieChart.setData(pieChartData);

            // Cài đặt tiêu đề và các thuộc tính khác
            adminIncomePieChart.setTitle("Income by Category");
            adminIncomePieChart.setLegendVisible(true); // Hiển thị chú thích
            adminIncomePieChart.setLabelsVisible(true); // Hiển thị nhãn trên các mảnh của Pie Chart

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }

}