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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;


import java.text.NumberFormat;
import java.util.Locale;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class TabContentController implements Initializable {
    @FXML
    private Button QRBtn;
    @FXML
    private ImageView qrImage;
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
    @FXML
    private Button changePassBtn;

    private ObservableList<Product> productList;
    private Customer customer;
    private boolean discountApplied = false;
    private double discountAmount = 0.0;

    private boolean isQRVisible = false;

    @FXML
    private void toggleQRVisibility() {
        isQRVisible = !isQRVisible;
        qrImage.setVisible(isQRVisible);

        if (isQRVisible) {
            QRBtn.setText("Ẩn QR");
        } else {
            QRBtn.setText("Mã QR");
        }
    }


    @FXML
    private void handleUsePointDiscount(ActionEvent event) {
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Bạn chưa thêm thông tin khách hàng");
            alert.showAndWait();
            return;
        }
        String initialText = "Dùng điểm giảm giá";
        String toggleText = "Không dùng điểm giảm giá";
        try {
            double totalAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(totalAmountLabel.getText()).doubleValue();
            int customerPoints = customer.getPoints();
            if (!discountApplied) {
                discountAmount = customerPoints;
                if (discountAmount > totalAmount) {
                    discountAmount = totalAmount;
                }
                double newTotalAmount = totalAmount - discountAmount;
                totalAmountLabel.setText(formatCurrency(newTotalAmount));
                discountApplied = true;
                usePointDiscount.setText(toggleText);
                updateChangeAmount();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Đã áp dụng điểm giảm giá thành công!");
                alert.showAndWait();
            } else {
                double originalTotalAmount = totalAmount + discountAmount;
                totalAmountLabel.setText(formatCurrency(originalTotalAmount));
                discountApplied = false;
                discountAmount = 0.0;
                usePointDiscount.setText(initialText);
                updateChangeAmount();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Không áp dụng mã giảm giá.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi giảm giá");
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
            String billQuery = "INSERT INTO bill (customer_phone,customer_name, user_id,user_name, total_amount, created_at) VALUES (?, ?, ?, ?,?,?)";
            PreparedStatement billStatement = connection.prepareStatement(billQuery, Statement.RETURN_GENERATED_KEYS);
            billStatement.setString(1, bill.getCustomerPhone());
            billStatement.setString(2, bill.getCustomerName());
            billStatement.setInt(3, bill.getUserId());
            billStatement.setString(4, bill.getUserFName());
            billStatement.setDouble(5, bill.getTotalAmount());
            bill.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            billStatement.setTimestamp(6, bill.getCreatedAt());
            billStatement.executeUpdate();
            ResultSet generatedKeys = billStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int billId = generatedKeys.getInt(1);
                String billDetailQuery = "INSERT INTO billdetail (product_id, bill_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement billDetailStatement = connection.prepareStatement(billDetailQuery);
                for (BillDetail detail : bill.getBillDetails()) {
                    Product product = getProductById(detail.getProductId());
                    billDetailStatement.setInt(1, detail.getProductId());
                    billDetailStatement.setInt(2, billId);
                    billDetailStatement.setString(3, detail.getProductName());
                    billDetailStatement.setInt(4, detail.getQuantity());
                    billDetailStatement.setDouble(5, product.getPrice());
                    billDetailStatement.addBatch();
                }
                billDetailStatement.executeBatch();
                generateInvoice(billId, bill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Product getProductById(int productId) {
        Product product = null;
        try {
            Connection connection = DBUtils.getConnection();
            String query = "SELECT * FROM product WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }


    private void generateInvoice(int billId, Bill bill) {
        String filePath = "bills/bill_" + billId + ".pdf";
        String fontPath = "src/main/resources/fonts/ARIAL.TTF";
        String logoPath = "src/main/resources/com/java/supermarket/images/bigclogo.png";

        File dir = new File("bills");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PdfWriter writer = new PdfWriter(filePath); PdfDocument pdf = new PdfDocument(writer); Document document = new Document(pdf)) {
            PdfFont font = PdfFontFactory.createFont(fontPath, "Identity-H", true);
            document.setFont(font);

            ImageData imageData = ImageDataFactory.create(logoPath);
            Image logo = new Image(imageData);
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            logo.scaleToFit(100, 50);
            document.add(logo);

            Paragraph title = new Paragraph("Hóa Đơn Bán Hàng")
                    .setFont(font)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedCreatedAt = bill.getCreatedAt().toLocalDateTime().format(formatter);

            document.add(new Paragraph("Thời gian in hóa đơn: " + formattedCreatedAt).setFont(font));
            document.add(new Paragraph("Mã Hóa Đơn: " + billId).setFont(font));
            document.add(new Paragraph("Tên người thanh toán: " + employeeName.getText()).setFont(font));
            document.add(new Paragraph("Tên khách hàng: " + customer.getName()).setFont(font));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("STT").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Giá tiền").setFont(font)));

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            int stt = 1;
            for (BillDetail detail : bill.getBillDetails()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(stt++)).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(detail.getProductName()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getQuantity())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(currencyFormat.format(detail.getPrice())).setFont(font)));
            }
            document.add(table);

            document.add(new Paragraph("Tiền khách đưa: " + amountGivenField.getText()).setFont(font));
            document.add(new Paragraph("Tiền trả lại khách: " + changeAmountLabel.getText()).setFont(font));

            document.add(new Paragraph("\nTổng tiền: " + formatCurrency(bill.getTotalAmount())));
            document.add(new Paragraph("Tổng tiền (viết bằng chữ): " + convertNumberToWords((int) bill.getTotalAmount())));
            if (discountApplied) {
                document.add(new Paragraph("Đã áp dụng điểm giảm giá: " + formatCurrency(discountAmount)));
            }

            Paragraph thank = new Paragraph("Cảm ơn quý khách")
                    .setFont(font)
                    .setFontSize(18)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(thank);
        } catch (Exception e) {
            e.printStackTrace();
        }

        openPDF(filePath);
        showInvoiceAlert();
    }

    private static final String[] units = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
    private static final String[] teens = {"mười", "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm", "mười sáu", "mười bảy", "mười tám", "mười chín"};
    private static final String[] tens = {"", "", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"};
    private static final String[] thousands = {"", "nghìn", "triệu", "tỷ"};

    private String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20 && number % 100 > 9) {
            current = teens[number % 10];
            number /= 100;
        } else {
            current = units[number % 10];
            number /= 10;

            current = tens[number % 10] + " " + current;
            number /= 10;
        }
        if (number == 0) return current.trim();
        return units[number] + " trăm " + current.trim();
    }

    public String convertNumberToWords(int number) {
        if (number == 0) {
            return "không";
        }

        String prefix = "";
        String current = "";
        int place = 0;

        do {
            int n = number % 1000;
            if (n != 0) {
                String s = convertLessThanOneThousand(n);
                current = s + " " + thousands[place] + " " + current;
            }
            place++;
            number /= 1000;
        } while (number > 0);

        return (prefix + current).trim() + " đồng";
    }


    private void openPDF(String filePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("The PDF file does not exist.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported.");
        }
    }


    private void showInvoiceAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hóa Đơn");
        alert.setHeaderText("Hóa Đơn Bán Hàng");
        alert.setContentText("Tạo hóa đơn thành công");
        alert.showAndWait();
    }

    private String employeeUsername;
    private String employeeFirstname;

    public void setCustomerName(String name) {
        customerNameFiled.setText(name);
    }

    public void setEmployeeUsername() {
        String username = this.getEmployeeUsername();
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
                employeeFirstname = firstName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        try {
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
            double totalAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(totalAmountLabel.getText()).doubleValue();
            double amountGiven = parseAmountGivenField();
            double changeAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(changeAmountLabel.getText()).doubleValue();

            if (amountGiven < totalAmount) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Tiền khách trả < tổng tiền hóa đơn");
                alert.showAndWait();
                return;
            }

            if (changeAmount < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Tiền trả lại khách không thể âm");
                alert.showAndWait();
                return;
            }

            System.out.println("Employee Username: " + employeeUsername);
            System.out.println("Employee ID: " + employeeId);


            Bill bill = new Bill(customer.getPhone(), employeeId, totalAmount, billDetails);
            bill.setUserFName(employeeUsername);
            bill.setCustomerName(customer.getName());
            saveBillToDatabase(bill);

            double pricePerBill = totalAmount;
            int pointsEarned = (int) (pricePerBill / 100);
            updateCustomerPoints(customer.getPhone(), pointsEarned);

            if (discountApplied) {
                int newCustomerPoints = customer.getPoints() - (int) discountAmount;
                updateCustomerPointsInDatabase(customer.getPhone(), newCustomerPoints);
                customer.setPoints(newCustomerPoints);
                discountApplied = false;
                discountAmount = 0.0;
            }

            customer = null;
            productList.clear();
            productTableView.refresh();
            totalAmountLabel.setText(formatCurrency(0));
            amountGivenField.setText("0");
            changeAmountLabel.setText(formatCurrency(0));
            customerNameFiled.setText(" ");

        } catch (ParseException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi xử lý hóa đơn");
            alert.showAndWait();
        }
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

    private Map<Integer, Integer> spinnerValues = new HashMap<>();

    private void saveSpinnerValues() {
        for (Product product : productList) {
            spinnerValues.put(product.getId(), product.getQuantity());
        }
    }

    private void restoreSpinnerValues() {
        for (Product product : productList) {
            if (spinnerValues.containsKey(product.getId())) {
                product.setQuantity(spinnerValues.get(product.getId()));
            }
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        productList = FXCollections.observableArrayList();
        productTableView.setItems(productList);
        colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        colQuantity.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return new SimpleObjectProperty<>(createSpinnerForProduct(product));
        });
        qrImage.setVisible(false);
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());
        colTitle.setResizable(false);
        colPrice.setResizable(false);
        colQuantity.setResizable(false);
        colDelete.setResizable(false);
        colTotal.setResizable(false);
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
                        updateChangeAmount();
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
                if (suggestions.isEmpty()) {
                    suggestions.add("Không có loại sản phẩm này trong kho");
                }
                suggestionListView.setItems(suggestions);
                suggestionListView.setVisible(true);
            } else {
                suggestionListView.setVisible(false);
            }
        });

        suggestionListView.setOnMouseClicked(event -> addSelectedProduct(event));
        usePointDiscount.setOnAction(this::handleUsePointDiscount);

        colPrice.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
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

        colTotal.setCellFactory(tc -> new TextFieldTableCell<>(new StringConverter<Double>() {
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


        totalAmountLabel.setText(formatCurrency(0));
        changeAmountLabel.setText(formatCurrency(0));


        amountGivenField.textProperty().addListener((observable, oldValue, newValue) -> {
            formatAmountGivenField(newValue);
            if (!discountApplied) {
                updateTotalAmount();
            }
        });

        amountGivenField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            // Check if the entered character is not a digit and not 'đ' and not backspace
            if (!character.matches("[0-9]") && !character.equals("đ") && !character.equals("\b")) {
                // Show error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Only numeric characters are allowed.");
                alert.showAndWait();
                // Consume the event to prevent the character from being entered
                event.consume();
            }
        });

        updateTotalAmount();

    }

    private void formatAmountGivenField(String newValue) {
        if (!newValue.isEmpty()) {
            int caretPosition = amountGivenField.getCaretPosition();
            String cleanString = newValue.replaceAll("[^\\d]", "");

            int maxLength = 12;
            if (cleanString.length() > maxLength) {
                cleanString = cleanString.substring(0, maxLength);
            }

            if (!cleanString.isEmpty()) {
                try {
                    long amountGiven = Long.parseLong(cleanString);
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedAmount = currencyFormatter.format(amountGiven).replace("₫", "đ");

                    int newLength = formattedAmount.length();
                    int lengthDifference = newLength - newValue.length();

                    amountGivenField.setText(formattedAmount);

                    amountGivenField.positionCaret(caretPosition + (formattedAmount.length() - newValue.length()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double parseAmountGivenField() throws ParseException {
        String amountGivenText = amountGivenField.getText().replace("đ", "").replaceAll("[^\\d]", "");
        if (amountGivenText.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(amountGivenText);
    }


    private void updateChangeAmount() {
        try {
            // Parse the total amount and amount given
            double totalAmount = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).parse(totalAmountLabel.getText()).doubleValue();
            String cleanString = amountGivenField.getText().replaceAll("[^\\d]", "");
            if (!cleanString.isEmpty()) {
                double amountGiven = Double.parseDouble(cleanString);
                // Calculate the change amount
                double changeAmount = amountGiven - totalAmount;
                changeAmountLabel.setText(formatCurrency(changeAmount));
            } else {
                changeAmountLabel.setText(formatCurrency(0));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            changeAmountLabel.setText(formatCurrency(0));
        }
    }

    private ObservableList<String> getSuggestions(String searchText) {
        ObservableList<String> suggestions = FXCollections.observableArrayList();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        for (Product product : adminProductList()) {
            if (product.getName().toLowerCase().contains(searchText)) {
                suggestions.add(product.getName() + " - " + currencyFormat.format(product.getPrice()));
            }
        }
        return suggestions;
    }


    private void addSelectedProduct(MouseEvent event) {
        saveSpinnerValues();
        String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            String productName = selectedSuggestion.split(" - ")[0];
            boolean productExists = false;
            for (Product existingProduct : productList) {
                if (existingProduct.getName().equalsIgnoreCase(productName)) {
                    productExists = true;
                    break;
                }
            }
            if (productExists) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Bạn đã thêm sản phẩm này");
                alert.showAndWait();
            } else {
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
                            product.setQuantity(1);
                            productList.add(product);
                            productTableView.setItems(productList);
                            suggestionListView.setVisible(false);
                            updateTotalAmount();
                            System.out.println("Product added: " + product.getName());
                        }
                        break;
                    }
                }
            }
        }
        restoreSpinnerValues();
        updateTotalAmount();
    }

    private Spinner<Integer> createSpinnerForProduct(Product product) {
        int currentStock = getProductStock(product.getId());
        Spinner<Integer> spinner = new Spinner<>(0, currentStock, product.getQuantity());
        spinner.setEditable(true);

        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue > currentStock) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng chọn vượt quá số lượng hàng hiện có");
                alert.showAndWait();
                spinner.getValueFactory().setValue(oldValue);
            } else {
                product.setQuantity(newValue.intValue());
                updateTotalAmount();
            }
        });

        spinner.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("[0-9]") && !character.equals("\b") && !character.equals("\r")) {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Chỉ được nhập ký tự số");
                alert.showAndWait();
                // Consume the event
                event.consume();
            }
        });

        spinner.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                spinner.getEditor().clear();
                event.consume();
            }
        });

        spinner.getEditor().setOnAction(event -> {
            try {
                int value = Integer.parseInt(spinner.getEditor().getText());
                spinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                spinner.getEditor().setText("0");
                spinner.getValueFactory().setValue(0);
            }
        });

        return spinner;
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
        try {
            double totalAmount = productList.stream()
                    .mapToDouble(Product::getTotal)
                    .sum();

            if (discountApplied) {
                totalAmount -= discountAmount;
            }
            updateChangeAmount();

            totalAmountLabel.setText(formatCurrency(totalAmount));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void calculateChange() {
        if (!amountGivenField.getText().isEmpty()) {
            formatAmountGivenField(amountGivenField.getText());
            updateChangeAmount();
        } else {
            changeAmountLabel.setText(formatCurrency(0));
        }
    }

    private ObservableList<Product> adminProductList() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT product.*, category.name as category_name FROM product LEFT JOIN category ON product.category_id = category.id";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"), null);
                Product product = new Product(id, name, description, category, price, quantity, ProductStatus.AVAILABLE);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @FXML
    void showCustomerForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/supermarket/CustomerForm.fxml"));
            Parent root = loader.load();
            CustomerFormController controller = loader.getController();
            controller.setTabContentController(this);
            controller.setCustomer(this.customer);
            controller.getPhoneField().setOnKeyReleased(e -> {
                String phone = controller.getPhoneField().getText();
                if (!phone.isEmpty()) {
                    if (customer != null) {
                        Platform.runLater(() -> {
                            controller.getNameField().setText(customer.getName());
                            setCustomerName(customer.getName());
                        });
                    }
                }
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thông tin khách hàng");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showChangePassForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/java/supermarket/changePasswordForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            final double[] xOffset = {0};
            final double[] yOffset = {0};

            root.setOnMousePressed(event -> {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset[0]);
                stage.setY(event.getScreenY() - yOffset[0]);
                stage.setOpacity(0.8);
            });

            root.setOnMouseReleased(event -> stage.setOpacity(1));

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load change password form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void saveCustomer(Customer customer) {
        try {
            this.customer = customer;
            Connection connection = DBUtils.getConnection();
            String query = "INSERT INTO customer (name, phone, points) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name=VALUES(name), points=VALUES(points)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getPhone());
            preparedStatement.setInt(3, customer.getPoints());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }


    double x;
    double y;

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Đăng xuất?");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                staffLogoutBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/com/java/supermarket/login.fxml"));
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

    public void setEmployeeName(String firstName, String lastName) {
        employeeName.setText(firstName + " " + lastName);
    }

    private int employeeId;
    private String password;
    private String firstName;

    public void setEmployeeData(int id, String firstName, String username, String password) {
        this.employeeId = id;
        this.firstName = firstName;
        this.employeeUsername = username;
        this.password = password;
    }

    public void setEmployeeData(int id, String username, String password) {
        this.employeeId = id;
        this.employeeUsername = username;
        this.password = password;
    }

    public void setEmployeeFirstname(String employeeFirstname) {
        this.employeeFirstname = employeeFirstname;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public String getEmployeeFirstname() {
        return employeeFirstname;
    }

}
