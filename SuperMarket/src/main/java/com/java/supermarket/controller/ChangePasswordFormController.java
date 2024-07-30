package com.java.supermarket.controller;

import com.java.supermarket.DBUtils;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordFormController {
    public void updatePasswordWithEncryption(int userId, String newPassword) {
        String updateQuery = "UPDATE user SET password = ? WHERE id = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(updateQuery)) {

            // Hash the password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật mật khẩu thành công");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không thể cập nhật mật khẩu");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi cập nhật mật khẩu: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
