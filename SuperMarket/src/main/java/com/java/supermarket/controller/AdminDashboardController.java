package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.Product;
import com.java.supermarket.object.ProductStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ComboBox<?> adminRoleCB;

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
    private TableView<?> admin_StaffTable;

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
    private TableColumn<?, ?> col_staff_fname;

    @FXML
    private TableColumn<?, ?> col_staff_id;

    @FXML
    private TableColumn<?, ?> col_staff_lname;

    @FXML
    private TableColumn<?, ?> col_staff_phone;

    @FXML
    private TableColumn<?, ?> col_staff_role;

    @FXML
    private TableColumn<?, ?> col_staff_username;

    @FXML
    private TableColumn<?, ?> col_total_amount;

    private Double x;
    private Double y;
    private void hideAllForm(){
        adminStatForm.setVisible(false);
        adminStatBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminStaffManForm.setVisible(false);
        adminStaffManBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminProManForm.setVisible(false);
        adminProManBtn.setStyle("-fx-background-color: TRANSPARENT");

        adminBillManForm.setVisible(false);
        adminBillManBtn.setStyle("-fx-background-color: TRANSPARENT");
    }
    public void switchForm(ActionEvent event){
        hideAllForm();
        if(event.getSource() == adminStatBtn){
            adminTitleLabel.setText("Quản lý - Thống kê");
            adminStatForm.setVisible(true);
            adminStatBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        }
        else if(event.getSource() == adminStaffManBtn){
            adminTitleLabel.setText("Quản lý - Nhân viên");
            adminStaffManForm.setVisible(true);
            adminStaffManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        }
        else if(event.getSource() == adminProManBtn){
            adminTitleLabel.setText("Quản lý - Sản phẩm");
            adminProManForm.setVisible(true);
            showProduct();
            adminProManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        }
        else if(event.getSource() == adminBillManBtn){
            adminTitleLabel.setText("Quản lý - Hoá đơn");
            adminBillManForm.setVisible(true);
            adminBillManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
        }
    }

    public void close(){
        System.exit(0);
    }
    public void minimize(){
        Stage stage=(Stage)adminForm.getScene().getWindow();
        stage.setIconified(true);
    }

    public void logout(){
        try {
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Đăng xuất?");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

            Optional<ButtonType> option=alert.showAndWait();

            if (option.get().equals(ButtonType.OK)){
                adminLogoutBtn.getScene().getWindow().hide();

                Parent root= FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage=new Stage();
                Scene scene=new Scene(root);

                stage.initStyle(StageStyle.TRANSPARENT);
                root.setOnMousePressed((MouseEvent event)->{
                    x=event.getSceneX();
                    y=event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event)->{
                    stage.setX(event.getScreenX()-x);
                    stage.setY(event.getScreenY()-y);

                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased((MouseEvent event)->{
                    stage.setOpacity(1);
                });
                stage.setScene(scene);
                stage.show();

            }else return;

        }catch (Exception e){e.printStackTrace();}
    }

    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;

    ObservableList<Product> adminProductList(){
     ObservableList<Product> productsList= FXCollections.observableArrayList();
     String sql="select * from product";
     con = DBUtils.getConnection();
     try{
         Product product;
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         while(rs.next()){
                     int id=rs.getInt("id");
                     String name=rs.getString("name");
                     String desc=rs.getString("description");
                     String category=rs.getString("category");
                     Float price=rs.getFloat("price");
                     int quantity=rs.getInt("quantity");
                     String productStat=rs.getString("product_status");
                     ProductStatus productStatus=ProductStatus.valueOf(productStat);
                     product=new Product(id,name,desc,category,price,quantity,productStatus);
             productsList.add(product);
         }
     }catch (Exception e){
         e.printStackTrace();
         return null;
     }
     return productsList;
    }

    ObservableList<Product> adminProductList2;

    public void showProduct(){
        adminProductList2=adminProductList();
        col_pro_stt.setCellValueFactory(new PropertyValueFactory<Product,String>("id"));
        col_pro_name.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        col_pro_cat.setCellValueFactory(new PropertyValueFactory<Product,String>("category"));
        col_pro_desc.setCellValueFactory(new PropertyValueFactory<Product,String>("description"));
        col_pro_price.setCellValueFactory(new PropertyValueFactory<Product,String>("price"));
        col_pro_quantity.setCellValueFactory(new PropertyValueFactory<Product,String>("quantity"));
        col_pro_status.setCellValueFactory(new PropertyValueFactory<Product,String>("status"));

        adminProTable.setItems(adminProductList2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideAllForm();
        adminProManForm.setVisible(true);
        adminProManBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #8AE308, #4CAF50);");
    }
}
