package com.java.supermarket.object;

public class BillDetail {
    private int id;
    private int productId;
    private String productName; // Thêm thuộc tính productName
    private int billId;
    private int quantity;
    private double price;

    // Constructor, getters and setters
    public BillDetail(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName; // Cập nhật productName
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
