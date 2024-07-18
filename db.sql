-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for supermarket
CREATE DATABASE IF NOT EXISTS `supermarket` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `supermarket`;

-- Dumping structure for table supermarket.bill
CREATE TABLE IF NOT EXISTS `bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `user_id` int NOT NULL,
  `total_amount` float NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_phone` (`customer_phone`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`customer_phone`) REFERENCES `customer` (`phone`),
  CONSTRAINT `bill_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table supermarket.bill: ~0 rows (approximately)

-- Dumping structure for table supermarket.billdetail
CREATE TABLE IF NOT EXISTS `billdetail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bill_id` int NOT NULL,
  `product_id` int NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `price` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `billdetail_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `billdetail_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table supermarket.billdetail: ~0 rows (approximately)

-- Dumping structure for table supermarket.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `name` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `points` int DEFAULT '0',
  PRIMARY KEY (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table supermarket.customer: ~0 rows (approximately)

-- Dumping structure for table supermarket.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `price` float NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `status` enum('AVAILABLE','DELETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table supermarket.product: ~2 rows (approximately)
INSERT INTO `product` (`id`, `name`, `description`, `category`, `price`, `quantity`, `status`) VALUES
  (1, 'Aquavina', 'Nước lọc Aquavina 500ml', 'Đồ uống', 10000, 1, 'AVAILABLE'),
  (2, 'Niva', 'Bông tai Niva', 'Vệ sinh', 12000, 0, 'DELETED');

-- Trigger to update product status based on quantity
DELIMITER //
CREATE TRIGGER update_product_status BEFORE INSERT ON product
FOR EACH ROW
BEGIN
    IF NEW.quantity = 0 THEN
        SET NEW.status = 'DELETED';
    ELSE
        SET NEW.status = 'AVAILABLE';
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER update_product_status_before_update BEFORE UPDATE ON product
FOR EACH ROW
BEGIN
    IF NEW.quantity = 0 THEN
        SET NEW.status = 'DELETED';
    ELSE
        SET NEW.status = 'AVAILABLE';
    END IF;
END;
//
DELIMITER ;

-- Dumping structure for table supermarket.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1',
  `role` enum('manager','employee') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'employee',
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table supermarket.user: ~2 rows (approximately)
INSERT INTO `user` (`id`, `username`, `password`, `role`, `first_name`, `last_name`, `phone`) VALUES
  (1, 'admin', 'admin123', 'manager', 'manager', 'admin', '123456781'),
  (2, 'employee1', 'employee', 'employee', 'john', 'nguyen', '123456782');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
