package com.java.supermarket.object;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private String password;
    private String role;


    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserData(int id, String username, String password, String role) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }
    public String getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}