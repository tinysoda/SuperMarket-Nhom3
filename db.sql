-- Create the database
CREATE DATABASE IF NOT EXISTS supermarket;
USE supermarket;

-- Create User table
CREATE TABLE User (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(255),
  password VARCHAR(255),
  role ENUM('manager', 'employee')
);

-- Create Product table
CREATE TABLE Product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10, 2),
  quantity INT,
  category_id INT,
  is_deleted INT DEFAULT 0,
);

-- Create Category table
CREATE TABLE Category (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT
);

-- Create Customer table
CREATE TABLE Customer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(255),
  address VARCHAR(255),
  points INT DEFAULT 0
);

-- Create Bill table
CREATE TABLE Bill (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT,
  user_id INT,
  total_amount DECIMAL(10, 2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES Customer(id),
  FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Create BillDetail table
CREATE TABLE BillDetail (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT,
  bill_id INT,
  quantity INT,
  price DECIMAL(10, 2),
  FOREIGN KEY (product_id) REFERENCES Product(id),
  FOREIGN KEY (bill_id) REFERENCES Bill(id)
);

-- Add foreign key constraints
ALTER TABLE Product ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES Category(id);

-- This should be added to ensure that the script can be run in one go without errors.
SET FOREIGN_KEY_CHECKS = 0;
