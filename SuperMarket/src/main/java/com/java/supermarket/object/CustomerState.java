package com.java.supermarket.object;

public class CustomerState {
    private static String name;
    private static String phone;
    private static int points;

    public static void setName(String name) {
        CustomerState.name = name;
    }

    public static String getName() {
        return name;
    }

    public static void setPhone(String phone) {
        CustomerState.phone = phone;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPoints(int points) {
        CustomerState.points = points;
    }

    public static int getPoints() {
        return points;
    }
}

