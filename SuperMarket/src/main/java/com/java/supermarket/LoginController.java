package com.java.supermarket;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private HBox login_form;

    @FXML
    private Button login_button;

    @FXML
    private Button login_close;

    @FXML
    private TextField login_password;

    @FXML
    private TextField login_username;

    public void Close(){
        System.exit(0);
    }

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public void Login(){
        String loginQuery = "select * from user where name=? and password=?";
        con=DBUtils.getConnection();
            Alert alert;
        String username = login_username.getText();
        String password = login_password.getText();
        if(username.isEmpty()||password.isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng điền đầy đủ thông tin");
            alert.showAndWait();
        }
        try{
            ps=con.prepareStatement(loginQuery);
            ps.setString(1,username);
            ps.setString(2,password);
            rs=ps.executeQuery();
            if (rs.next()){
                login_button.getScene().getWindow().hide();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText("Đăng nhập thành công");
                alert.showAndWait();

                if (username.equals("admin")){
                    Parent root=FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
                    Stage stage=new Stage();
                    Scene scene=new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Sai thông tin đăng nhập");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize(URL url, ResourceBundle rb){

    }
}
