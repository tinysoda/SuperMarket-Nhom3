package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import java.io.FileOutputStream;
import java.io.IOException;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class EmployeeDashboardController implements Initializable {
    @FXML
    private Label customerNameFiled;

    @FXML
    private Label employeeName;

    @FXML
    private BorderPane employeeForm;

    @FXML
    private TextField productSearchField;

    @FXML
    private ListView<String> suggestionListView;

    @FXML
    private Button customerInfoButton;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, String> colTitle;

    @FXML
    private TableColumn<Product, Double> colPrice;

    @FXML
    private TableColumn<Product, Spinner<Integer>> colQuantity;

    @FXML
    private TableColumn<Product, Double> colTotal;

    @FXML
    private TableColumn<Product, Button> colDelete;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TextField amountGivenField;

    @FXML
    private Label changeAmountLabel;

    @FXML
    private Button saveOrderButton;

    private ObservableList<Product> productList;

    private Customer customer; // Biến lưu trữ thông tin khách hàng hiện tại

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        // Stage stage = (Stage) employeeForm.getScene().getWindow();
        // stage.setIconified(true);
    }

    private void saveBillToDatabase(Bill bill) {
        try {
            Connection connection = DBUtils.getConnection();
            String billQuery = "INSERT INTO bill (customer_phone, user_id, total_amount) VALUES (?, ?, ?)";
            PreparedStatement billStatement = connection.prepareStatement(billQuery, Statement.RETURN_GENERATED_KEYS);
            billStatement.setString(1, bill.getCustomerPhone());
            billStatement.setInt(2, bill.getUserId());
            billStatement.setDouble(3, bill.getTotalAmount());
            billStatement.executeUpdate();

            ResultSet generatedKeys = billStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int billId = generatedKeys.getInt(1);

                String billDetailQuery = "INSERT INTO billdetail (bill_id, product_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement billDetailStatement = connection.prepareStatement(billDetailQuery);
                for (BillDetail detail : bill.getBillDetails()) {
                    billDetailStatement.setInt(1, billId);
                    billDetailStatement.setInt(2, detail.getProductId());
                    billDetailStatement.setString(3, detail.getProductName()); // Cập nhật productName
                    billDetailStatement.setInt(4, detail.getQuantity());
                    billDetailStatement.setDouble(5, detail.getPrice());
                    billDetailStatement.addBatch();
                }
                billDetailStatement.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String employeeUsername; // Tên đăng nhập của nhân viên

    // Phương thức để thiết lập tên khách hàng
    public void setCustomerName(String name) {
        customerNameFiled.setText(name);
    }

    // Phương thức để thiết lập tên nhân viên
    public void setEmployeeUsername(String username) {
        this.employeeUsername = username;
        setEmployeeNameFromUsername(username);
    }

    private void setEmployeeNameFromUsername(String username) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT first_name, last_name FROM user WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                employeeName.setText(firstName + " " + lastName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showCustomerForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/supermarket/CustomerForm.fxml"));
            Parent root = loader.load();

            CustomerFormController controller = loader.getController();
            controller.setEmployeeDashboardController(this);
            controller.setCustomer(this.customer); // Truyền thông tin khách hàng hiện tại cho form

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thông tin khách hàng");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveCustomer(Customer customer) {
        this.customer = customer; // Lưu thông tin khách hàng vào biến
        try {
            Connection connection = DBUtils.getConnection();
            String query = "INSERT INTO customer (name, phone, points) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhone());
            preparedStatement.setInt(3, customer.getPoints());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void calculateChange(KeyEvent event) {
        double totalAmount = Double.parseDouble(totalAmountLabel.getText());
        double amountGiven = Double.parseDouble(amountGivenField.getText());
        double changeAmount = amountGiven - totalAmount;
        changeAmountLabel.setText(String.valueOf(changeAmount));
    }

    public void generateInvoiceWithJasper() {
        try {
            // Tải tệp .jrxml
            InputStream inputStream = getClass().getResourceAsStream("/path/to/hoadon.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Tạo datasource từ danh sách sản phẩm
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productList);

            // Tạo các tham số cần thiết cho báo cáo
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("customerName", customerNameFiled.getText());
            parameters.put("employeeName", employeeName.getText());
            parameters.put("totalAmount", Double.parseDouble(totalAmountLabel.getText()));
            parameters.put("amountGiven", Double.parseDouble(amountGivenField.getText()));
            parameters.put("changeAmount", Double.parseDouble(changeAmountLabel.getText()));

            // Điền dữ liệu vào báo cáo
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Xuất báo cáo ra tệp PDF
            String filePath = "hoadon.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

            showInvoiceAlert(filePath);
            System.out.println("PDF generated successfully. File saved at: " + filePath);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }








    private void showInvoiceAlert(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hóa Đơn");
        alert.setHeaderText("Hóa Đơn Bán Hàng");
        alert.setContentText("Hóa đơn đã được tạo tại: " + filePath);
        alert.showAndWait();
    }


    @FXML
    void saveOrder(ActionEvent event) {
        if (amountGivenField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Điền thiếu thông tin thanh toán");
            alert.showAndWait();
            return;
        }

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa thêm thông tin khách hàng");
            alert.showAndWait();
            return;
        }

        if (productList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Chưa thêm sản phẩm vào hóa đơn");
            alert.showAndWait();
            return;
        }

        List<BillDetail> billDetails = new ArrayList<>();
        for (Product product : productList) {
            int currentStock = getProductStock(product.getId());
            if (product.getQuantity() > currentStock) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Sản phẩm " + product.getName() + " đã hết hàng");
                alert.showAndWait();
                return;
            } else {
                updateProductStock(product.getId(), currentStock - product.getQuantity());
                billDetails.add(new BillDetail(product.getId(), product.getName(), product.getQuantity(), product.getPrice())); // Cập nhật productName
            }
        }

        double totalAmount = Double.parseDouble(totalAmountLabel.getText());
        Bill bill = new Bill(customer.getPhone(), getEmployeeId(employeeUsername), totalAmount, billDetails);
        saveBillToDatabase(bill);

        productList.clear();
        productTableView.setItems(productList);
        customer = null;
        totalAmountLabel.setText("");
        amountGivenField.clear();
        changeAmountLabel.setText("");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Tạo hóa đơn thành công");
        alert.showAndWait();

        System.out.println("Generating invoice...");
        generateInvoiceWithJasper();
    }




    private int getEmployeeId(String username) {
        int employeeId = 0;
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT id FROM user WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employeeId = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeId;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productList = FXCollections.observableArrayList();
        productTableView.setItems(productList);

        colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

        colQuantity.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            int currentStock = getProductStock(product.getId()); // Lấy số lượng hàng hiện tại từ cơ sở dữ liệu
            Spinner<Integer> spinner = new Spinner<>(0, currentStock, product.getQuantity()); // Đặt giới hạn tối đa là currentStock

            spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue > currentStock) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số lượng chọn vượt quá số lượng hàng hiện có");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Đặt lại giá trị của Spinner về giá trị thực tế từ cơ sở dữ liệu
                            Platform.runLater(() -> {
                                spinner.getValueFactory().setValue(product.getQuantity());
                            });
                        }
                    });
                } else {
                    product.setQuantity(newValue.intValue()); // Cập nhật số lượng của sản phẩm
                    updateTotalAmount(); // Cập nhật tổng số tiền
                }
            });

            return new SimpleObjectProperty<>(spinner);
        });

        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        colDelete.setCellFactory(param -> new TableCell<Product, Button>() {
            private final Button deleteButton = new Button("Xóa");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    deleteButton.setOnAction(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        productList.remove(product);
                        updateTotalAmount();
                    });
                    setGraphic(deleteButton);
                    setText(null);
                }
            }
        });

        productSearchField.setOnKeyReleased(event -> {
            String searchText = productSearchField.getText().toLowerCase();
            if (!searchText.isEmpty()) {
                ObservableList<String> suggestions = getSuggestions(searchText);
                suggestionListView.setItems(suggestions);
                suggestionListView.setVisible(true);
            } else {
                suggestionListView.setVisible(false);
            }
        });

        suggestionListView.setOnMouseClicked(event -> addSelectedProduct(event));
    }



    private ObservableList<String> getSuggestions(String searchText) {
        ObservableList<String> suggestions = FXCollections.observableArrayList();
        for (Product product : adminProductList()) {
            if (product.getName().toLowerCase().contains(searchText)) {
                suggestions.add(product.getName() + " (" + product.getPrice() + ")");
            }
        }
        return suggestions;
    }

    private void addSelectedProduct(MouseEvent event) {
        String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            String productName = selectedSuggestion.split(" \\(")[0];
            for (Product product : adminProductList()) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    productList.add(product);
                    productTableView.setItems(productList);
                    suggestionListView.setVisible(false);
                    System.out.println("Product added: " + product.getName()); // Thêm dòng này
                    break;
                }
            }
        }
        updateTotalAmount();
    }



    private int getProductStock(int productId) {
        int stock = 0;
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT quantity FROM product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                stock = resultSet.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }


    private void updateProductStock(int productId, int newStock) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "UPDATE product SET quantity = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newStock);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateTotalAmount() {
        double totalAmount = productList.stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum();
        totalAmountLabel.setText(String.valueOf(totalAmount));
    }


    private ObservableList<Product> adminProductList() {
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT * FROM product";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                ProductStatus status = ProductStatus.valueOf(resultSet.getString("status").toUpperCase());

                Product product = new Product(id, name, description, category, price, quantity, status);
                productsList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

}
