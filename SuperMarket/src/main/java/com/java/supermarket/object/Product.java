package com.java.supermarket.object;

import javafx.beans.property.*;
import javafx.scene.control.Button;

public class Product {
    private int id;
    private String name;
    private String description;
    private Category category;
    private DoubleProperty price;
    private IntegerProperty quantity;
    private DoubleProperty total;
    private ProductStatus status;
    private Button deleteButton;
    public Product() {
        this.price = new SimpleDoubleProperty();
        this.quantity = new SimpleIntegerProperty();
        this.total = new SimpleDoubleProperty();
    }
    public StringProperty statusProperty() {
        String statusStr = (getQuantity() > 0) ? "Còn hàng" : "Đã xoá";
        return new SimpleStringProperty(statusStr);
    }
    public Product(int id, String name, String description, Category category, double price, int quantity, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.total = new SimpleDoubleProperty(price * quantity);
        this.status = status;
        updateTotal();
    }
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        updateTotal();
        updateStatus();
    }
    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getTotal() { return total.get(); }
    public void setTotal(double total) { this.total.set(total); }
    public DoubleProperty totalProperty() { return total; }
    public Button getDeleteButton() { return deleteButton; }
    public void setDeleteButton(Button deleteButton) { this.deleteButton = deleteButton; }
    private void updateTotal() {
        this.total.set(this.price.get() * this.quantity.get());
    }
    private void updateStatus() {
        this.status = (this.quantity.get() > 0) ? ProductStatus.AVAILABLE : ProductStatus.DELETED;
    }
}

