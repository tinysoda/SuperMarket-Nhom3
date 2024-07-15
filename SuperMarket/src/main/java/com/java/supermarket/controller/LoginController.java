package com.java.supermarket.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.java.supermarket.DBUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Double x;
    private Double y;


    public void Login() {
        String loginQuery = "SELECT * FROM user WHERE username=? AND password=?";
        Alert alert;
        String username = login_username.getText();
        String password = login_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng điền đầy đủ thông tin");
            alert.showAndWait();
            return;
        }

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(loginQuery)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    login_button.getScene().getWindow().hide();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Đăng nhập thành công");
                    alert.showAndWait();

                    Parent root;
                    if (username.equals("admin")) {
                        root = FXMLLoader.load(getClass().getResource("/com/java/supermarket/adminDashboard.fxml"));
                    } else {
                        root = FXMLLoader.load(getClass().getResource("employeeDashboard.fxml"));
                    }

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    scene.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    scene.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                        stage.setOpacity(0.8);
                    });

                    scene.setOnMouseReleased((MouseEvent event) -> {
                        stage.setOpacity(1);
                    });
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Sai thông tin đăng nhập");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi kết nối cơ sở dữ liệu: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IO Error");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi tải giao diện: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void initialize(URL url, ResourceBundle rb){

    }
}
