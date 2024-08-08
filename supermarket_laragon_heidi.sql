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
CREATE DATABASE IF NOT EXISTS `supermarket` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `supermarket`;

-- Dumping structure for table supermarket.bill
CREATE TABLE IF NOT EXISTS `bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `total_amount` decimal(10,0) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_phone` (`customer_phone`),
  KEY `user_id` (`user_id`),
  KEY `user_name` (`user_name`),
  KEY `customer_name` (`customer_name`),
  CONSTRAINT `FK_bill_customer` FOREIGN KEY (`customer_phone`) REFERENCES `customer` (`phone`),
  CONSTRAINT `FK_bill_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.bill: ~0 rows (approximately)
INSERT INTO `bill` (`id`, `user_id`, `user_name`, `customer_phone`, `customer_name`, `total_amount`, `created_at`) VALUES
	(4, 3, NULL, '0123', 'Giang', 10000, '2024-08-08 07:24:27'),
	(5, 3, NULL, '0123', 'Giang', 20000, '2024-08-08 07:31:24'),
	(6, 3, NULL, '0123', 'Giang', 10000, '2024-08-08 07:49:16'),
	(7, 3, NULL, '0123', 'Giang', 10000, '2024-08-08 07:50:29'),
	(8, 3, NULL, '0123', 'Giang', 25000, '2024-08-08 07:54:19'),
	(9, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 08:00:09');

-- Dumping structure for table supermarket.billdetail
CREATE TABLE IF NOT EXISTS `billdetail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `bill_id` int DEFAULT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `billdetail_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `billdetail_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.billdetail: ~0 rows (approximately)
INSERT INTO `billdetail` (`id`, `product_id`, `bill_id`, `product_name`, `quantity`, `price`) VALUES
	(1, 3, 4, 'Mỳ hảo hảo', 1, 10000),
	(2, 2, 5, 'Lavie', 2, 10000),
	(3, 2, 6, 'Lavie', 1, 10000),
	(4, 2, 7, 'Lavie', 1, 10000),
	(5, 4, 8, 'Clear Man', 1, 25000),
	(6, 2, 9, 'Lavie', 1, 10000);

-- Dumping structure for table supermarket.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.category: ~4 rows (approximately)
INSERT INTO `category` (`id`, `name`, `description`) VALUES
	(1, 'Nước', 'nước uống'),
	(2, 'Mỳ', 'Mỳ ăn liền'),
	(3, 'Sữa tắm', 'Các loại sữa tắm'),
	(4, 'Đồ ăn vặt', 'Đồ ăn vặt cho trẻ em');

-- Dumping structure for table supermarket.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `points` int DEFAULT '0',
  PRIMARY KEY (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.customer: ~0 rows (approximately)
INSERT INTO `customer` (`name`, `phone`, `points`) VALUES
	('Giang', '0123', 1050);

-- Dumping structure for table supermarket.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `category_id` int DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `status` enum('DELETED','AVAILABLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'AVAILABLE',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.product: ~4 rows (approximately)
INSERT INTO `product` (`id`, `name`, `category_id`, `description`, `price`, `quantity`, `status`) VALUES
	(1, 'Sting', 1, 'Nước giải khát', 10000, 10, 'AVAILABLE'),
	(2, 'Lavie', 1, 'Nước lọc Lavie', 10000, 4, 'AVAILABLE'),
	(3, 'Mỳ hảo hảo', 2, 'Mỳ ăn liền', 10000, 7, 'AVAILABLE'),
	(4, 'Clear Man', 3, 'Sữa tắm Clear Man', 25000, 8, 'AVAILABLE');

-- Dumping structure for table supermarket.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('manager','employee') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'employee',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.user: ~5 rows (approximately)
INSERT INTO `user` (`id`, `first_name`, `last_name`, `phone`, `role`, `username`, `password`, `image`) VALUES
	(1, 'Quốc Dũng', 'Đỗ ', '0923132', 'manager', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', NULL),
	(2, 'Rê Mon', 'Đỗ', '076744564', 'employee', 'employee1', 'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', NULL),
	(3, 'Messi', 'Lionel', '01323132', 'employee', 'c', '5f16c3e2f49a617db5d9b49a3b433af11b054f8ee5700e3cb5b004c4cce20dca', NULL),
	(4, 'Cris', 'Backam', '0992332', 'employee', 'd2', 'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', NULL),
	(5, 'giang', 'nguyen', '0123456789', 'employee', 'john01', '5f16c3e2f49a617db5d9b49a3b433af11b054f8ee5700e3cb5b004c4cce20dca', NULL);

-- Dumping structure for trigger supermarket.update_product_status
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `update_product_status` BEFORE INSERT ON `product` FOR EACH ROW BEGIN
    IF NEW.quantity = 0 THEN
        SET NEW.status = 'DELETED';
    ELSE
        SET NEW.status = 'AVAILABLE';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger supermarket.update_product_status_before_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `update_product_status_before_update` BEFORE UPDATE ON `product` FOR EACH ROW BEGIN
    IF NEW.quantity = 0 THEN
        SET NEW.status = 'DELETED';
    ELSE
        SET NEW.status = 'AVAILABLE';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
