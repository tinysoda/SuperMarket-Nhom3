package com.java.supermarket.controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
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
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontProvider;


import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;


import javafx.stage.StageStyle;


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
    private Button staffCloseBtn;
    @FXML
    private Button staffMinimizeBtn;

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
    private Button saveOrderButton;

    @FXML
    private TextField amountGivenField;

    @FXML
    private Label changeAmountLabel;

    @FXML

    private Button usePointDiscount;

    @FXML
    private Button staffLogoutBtn;


    private ObservableList<Product> productList;

    private Customer customer; // Biến lưu trữ thông tin khách hàng hiện tại


    private boolean discountApplied = false;
    private double discountAmount = 0.0;


    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
         Stage stage = (Stage) employeeForm.getScene().getWindow();
         stage.setIconified(true);
    }

    @FXML
    private void handleUsePointDiscount(ActionEvent event) {
        try {
            double totalAmount = Double.parseDouble(totalAmountLabel.getText());
            int customerPoints = customer.getPoints();

            if (!discountApplied) {
                // Giả sử 1 điểm = 1 đơn vị tiền
                discountAmount = customerPoints;

                // Nếu số điểm lớn hơn số tiền tổng, chỉ dùng số điểm bằng số tiền tổng
                if (discountAmount > totalAmount) {
                    discountAmount = totalAmount;
                }

                // Tính lại tổng số tiền sau khi trừ điểm
                double newTotalAmount = totalAmount - discountAmount;
                totalAmountLabel.setText(String.valueOf(newTotalAmount));

                // Đánh dấu là đã áp dụng điểm giảm giá
                discountApplied = true;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Đã áp dụng điểm giảm giá thành công!");
                alert.showAndWait();
            } else {
                // Bỏ áp dụng điểm giảm giá
                double originalTotalAmount = totalAmount + discountAmount;
                totalAmountLabel.setText(String.valueOf(originalTotalAmount));

                // Đánh dấu là chưa áp dụng điểm giảm giá
                discountApplied = false;
                discountAmount = 0.0;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không áp dụng mã giảm giá.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi áp dụng điểm giảm giá.");
            alert.showAndWait();
        }
    }



    private void updateCustomerPointsInDatabase(String customerPhone, int newPoints) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "UPDATE customer SET points = ? WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newPoints);
            preparedStatement.setString(2, customerPhone);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    billDetailStatement.setString(3, detail.getProductName());
                    billDetailStatement.setInt(4, detail.getQuantity());
                    billDetailStatement.setDouble(5, detail.getPrice());
                    billDetailStatement.addBatch();
                }
                billDetailStatement.executeBatch();

                generateInvoice(billId, bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void generateInvoice(int billId, Bill bill) {
        String filePath = "C:\\Users\\ASUS\\OneDrive\\Máy tính\\SuperMarket-Nhom3\\bill_" + billId + ".pdf";
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Nhúng phông chữ tiếng Việt
            PdfFont font = PdfFontFactory.createFont("C:\\Users\\ASUS\\OneDrive\\Máy tính\\SuperMarket-Nhom3\\arial-cufonfonts\\ARIAL.TTF", "Identity-H", true);
            document.setFont(font);

            // Thêm logo của công ty
            ImageData imageData = ImageDataFactory.create("C:\\Users\\ASUS\\OneDrive\\Máy tính\\SuperMarket-Nhom3\\SuperMarket-Nhom3\\SuperMarket\\src\\main\\resources\\com\\java\\supermarket\\images\\bigclogo.png");
            Image logo = new Image(imageData);
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            logo.scaleToFit(100, 50);
            document.add(logo);

            // Thêm tiêu đề "Hóa Đơn Bán Hàng"
            Paragraph title = new Paragraph("Hóa Đơn Bán Hàng")
                    .setFont(font)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Thêm thông tin vào tài liệu PDF
            document.add(new Paragraph("Mã Hóa Đơn: " + billId).setFont(font));
            document.add(new Paragraph("Tên người thanh toán: " + employeeName.getText()).setFont(font));
            document.add(new Paragraph("Tên khách hàng: " + customer.getName()).setFont(font));

            // Tạo bảng chứa thông tin sản phẩm
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2}))
                    .useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("STT").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Giá tiền").setFont(font)));

            int stt = 1;
            for (BillDetail detail : bill.getBillDetails()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(stt++)).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(detail.getProductName()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getQuantity())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getPrice())).setFont(font)));
            }

            document.add(table);
            document.add(new Paragraph("Tổng tiền: " + bill.getTotalAmount()).setFont(font));

        } catch (Exception e) {
            e.printStackTrace();
        }
        showInvoiceAlert(filePath);
    }



    private void showInvoiceAlert(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hóa Đơn");
        alert.setHeaderText("Hóa Đơn Bán Hàng");
        alert.setContentText("Hóa đơn đã được tạo tại: " + filePath);
        alert.showAndWait();
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
    void calculateChange(KeyEvent event) {
        double totalAmount = Double.parseDouble(totalAmountLabel.getText());
        double amountGiven = Double.parseDouble(amountGivenField.getText());
        double changeAmount = amountGiven - totalAmount;
        changeAmountLabel.setText(String.valueOf(changeAmount));
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
                billDetails.add(new BillDetail(product.getId(), product.getName(), product.getQuantity(), product.getTotal()));
            }
        }

        double totalAmount = Double.parseDouble(totalAmountLabel.getText());
        Bill bill = new Bill(customer.getPhone(), getEmployeeId(employeeUsername), totalAmount, billDetails);
        saveBillToDatabase(bill);

        // Calculate and update customer points
        double amountGiven = Double.parseDouble(amountGivenField.getText());
        int pointsEarned = (int) (amountGiven / 10);
        updateCustomerPoints(customer.getPhone(), pointsEarned);

        // Nếu đã áp dụng điểm giảm giá, cập nhật lại số điểm khách hàng
        if (discountApplied) {
            int newCustomerPoints = customer.getPoints() - (int) discountAmount;
            updateCustomerPointsInDatabase(customer.getPhone(), newCustomerPoints);
            customer.setPoints(newCustomerPoints);
            discountApplied = false; // Reset trạng thái
            discountAmount = 0.0;
        }

        customer = null;
        productList.clear();
        productTableView.refresh();
        totalAmountLabel.setText("0");
        amountGivenField.clear();
        changeAmountLabel.setText("0");
        customerNameFiled.setText("0");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Tạo hóa đơn thành công");
        alert.showAndWait();
    }

    private void updateCustomerPoints(String customerPhone, int pointsEarned) {
        try {
            Connection connection = DBUtils.getConnection();
            String query = "UPDATE customer SET points = points + ? WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pointsEarned);
            preparedStatement.setString(2, customerPhone);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        usePointDiscount.setOnAction(this::handleUsePointDiscount);
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
                    int currentStock = getProductStock(product.getId());
                    if (currentStock == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Sản phẩm này đã hết hàng");
                        alert.showAndWait();
                    } else {
                        productList.add(product);
                        productTableView.setItems(productList);
                        suggestionListView.setVisible(false);
                        System.out.println("Product added: " + product.getName()); // Log product addition
                    }
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
        }
        return customer;
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
            double x;
            double y;
            public void logout(){
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Đăng xuất?");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        staffLogoutBtn.getScene().getWindow().hide();

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
}


