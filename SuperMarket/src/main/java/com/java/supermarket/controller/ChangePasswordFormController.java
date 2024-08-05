package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import com.java.supermarket.object.Employee;
import com.java.supermarket.object.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordFormController {

    @FXML
    private Button cancelBtn;

    @FXML
    private PasswordField newPassTF;

    @FXML
    private PasswordField oldPassTF;

    @FXML
    private PasswordField reNewPassTF;

    @FXML
    private Button savePassBtn;
    @FXML
    public void cancelChangePass( ) {
        cancelBtn.getScene().getWindow().hide();
    }
    @FXML
    public void changePassword() {
        int userId = UserSession.getInstance().getUserId();
        String username = UserSession.getInstance().getUsername();
        String storedEncryptedPassword = UserSession.getInstance().getPassword();
        String oldPassword = oldPassTF.getText();
        String newPassword = newPassTF.getText();
        String reNewPassword = reNewPassTF.getText();
        Alert alert;

        try {
            String encryptedOldPassword = encryptPassword(oldPassword);

            if (newPassword.equals(oldPassword)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu mới phải khác mật khẩu cũ");
                alert.showAndWait();
            } else if (!newPassword.equals(reNewPassword)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu mới không khớp");
                alert.showAndWait();
            } else if (!encryptedOldPassword.equals(storedEncryptedPassword)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu cũ không đúng");
                alert.showAndWait();
            }else if(newPassword.length()<6){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu phải dài hơn 6 kí tự");
                alert.showAndWait();}
            else {
                String encryptedNewPassword = encryptPassword(newPassword);
                updatePasswordWithEncryption(userId, encryptedNewPassword);

                // Update the UserSession with the new encrypted password
                UserSession.getInstance().setPassword(encryptedNewPassword);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu đã được thay đổi thành công");
                alert.showAndWait();

                // Clear the password fields
                oldPassTF.clear();
                newPassTF.clear();
                reNewPassTF.clear();
                cancelBtn.getScene().getWindow().hide();
            }
        } catch (NoSuchAlgorithmException | SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi cập nhật mật khẩu: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updatePasswordWithEncryption(int userId, String encryptedPassword) throws SQLException {
        String updateQuery = "UPDATE user SET password = ? WHERE id = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(updateQuery)) {

            ps.setString(1, encryptedPassword);
            ps.setInt(2, userId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating password failed, no rows affected.");
            }
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

}
