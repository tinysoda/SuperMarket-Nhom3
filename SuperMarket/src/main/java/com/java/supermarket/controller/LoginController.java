package com.java.supermarket.controller;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.UserSession;
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
    public int userId;

    public void Login() {
        String loginQuery = "SELECT id, username, password FROM user WHERE username=?";
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

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String loggedInUsername = rs.getString("username");
                    String storedPassword = rs.getString("password");

                    // Encrypt the entered password
                    String encryptedEnteredPassword = encryptPassword(password);

                    // Compare the encrypted entered password with the stored encrypted password
                    if (encryptedEnteredPassword.equals(storedPassword)) {
                        login_button.getScene().getWindow().hide();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Info");
                        alert.setHeaderText(null);
                        alert.setContentText("Đăng nhập thành công");
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader();
                        Parent root;
                        if (loggedInUsername.equals("admin")) {
                            loader.setLocation(getClass().getResource("/com/java/supermarket/adminDashboard.fxml"));
                        } else {
                            loader.setLocation(getClass().getResource("/com/java/supermarket/employeeDashboard.fxml"));
                        }
                        root = loader.load();

                        if (!loggedInUsername.equals("admin")) {
                            EmployeeDashboardController controller = loader.getController();
                            controller.setEmployeeData(userId, loggedInUsername, storedPassword);

                            UserSession.getInstance().setUserData(userId, loggedInUsername, storedPassword);
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
        } catch (IOException | NoSuchAlgorithmException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra: " + e.getMessage());
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


    @FXML
    private void initialize(URL url, ResourceBundle rb){

    }
}
