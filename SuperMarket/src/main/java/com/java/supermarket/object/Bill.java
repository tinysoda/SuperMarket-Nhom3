package com.java.supermarket.object;

import java.sql.Timestamp;
import java.util.List;

public class Bill {
    private int id;
    private String customerPhone;
    private int userId;
    private double totalAmount;
    private Timestamp createdAt;
    private List<BillDetail> billDetails;

    // Constructor, getters and setters
    public Bill(String customerPhone, int userId, double totalAmount, List<BillDetail> billDetails) {
        this.customerPhone = customerPhone;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.billDetails = billDetails;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public List<BillDetail> getBillDetails() { return billDetails; }
    public void setBillDetails(List<BillDetail> billDetails) { this.billDetails = billDetails; }
}


