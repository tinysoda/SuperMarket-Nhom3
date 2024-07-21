package com.java.supermarket.object;

public class Customer {
    private String name;
    private String phone;
    private int points;

    public Customer(String name, String phone, int points) {
        this.name = name;
        this.phone = phone;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
