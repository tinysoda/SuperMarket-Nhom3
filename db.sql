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
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int DEFAULT NULL,
  `total_amount` decimal(10,0) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_phone` (`customer_phone`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`customer_phone`) REFERENCES `customer` (`phone`),
  CONSTRAINT `bill_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.bill: ~16 rows (approximately)
INSERT INTO `bill` (`id`, `customer_phone`, `user_id`, `total_amount`, `created_at`) VALUES
	(1, '06666', 3, 20000, '2024-07-26 07:50:28'),
	(2, '06666', 3, 20000, '2024-07-26 08:01:46'),
	(3, '06666', 3, 15999, '2024-07-26 08:06:30'),
	(4, '0123', 3, 20000, '2024-07-26 08:34:12'),
	(5, '06666', 3, 20000, '2024-07-26 10:30:51'),
	(6, '09999', 3, 40000, '2024-07-26 11:08:30'),
	(7, '0777', 3, 30000, '2024-07-26 11:26:42'),
	(8, '06666', 3, 50000, '2024-07-26 13:09:38'),
	(9, '06666', 3, 30000, '2024-07-26 23:18:24'),
	(10, '06666', 3, 10000, '2024-07-26 17:40:35'),
	(11, '06666', 3, 20000, '2024-07-26 17:44:31'),
	(12, '06666', 3, 20000, '2024-07-26 18:37:07'),
	(13, '06666', 3, 80000, '2024-07-27 14:37:32'),
	(14, '06666', 3, 10000, '2024-07-27 14:41:38'),
	(15, '0987654321', 3, 50000, '2024-07-29 12:24:40'),
	(16, '09823312', 3, 100000, '2024-07-29 12:36:25');

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.billdetail: ~19 rows (approximately)
INSERT INTO `billdetail` (`id`, `product_id`, `bill_id`, `product_name`, `quantity`, `price`) VALUES
	(1, 2, 3, 'Lavie', 2, 20000),
	(2, 2, 4, 'Lavie', 2, 20000),
	(3, 2, 5, 'Lavie', 2, 20000),
	(4, 2, 6, 'Lavie', 1, 10000),
	(5, 3, 6, 'Mỳ hảo hảo', 3, 30000),
	(6, 2, 7, 'Lavie', 2, 20000),
	(7, 3, 7, 'Mỳ hảo hảo', 1, 10000),
	(8, 1, 8, 'Sting', 5, 50000),
	(9, 1, 9, 'Sting', 3, 30000),
	(10, 2, 10, 'Lavie', 1, 10000),
	(11, 2, 11, 'Lavie', 2, 20000),
	(12, 2, 12, 'Lavie', 2, 20000),
	(13, 2, 13, 'Lavie', 3, 30000),
	(14, 3, 13, 'Mỳ hảo hảo', 5, 50000),
	(15, 3, 14, 'Mỳ hảo hảo', 1, 10000),
	(16, 4, 15, 'Clear Man', 2, 50000),
	(17, 1, 16, 'Sting', 3, 30000),
	(18, 2, 16, 'Lavie', 2, 20000),
	(19, 4, 16, 'Clear Man', 2, 50000);

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

-- Dumping data for table supermarket.customer: ~6 rows (approximately)
INSERT INTO `customer` (`name`, `phone`, `points`) VALUES
	('Giang', '0123', 2000),
	('Dũng', '06666', 23000),
	('Đô', '0777', 5000),
	('Diep', '09823312', 10000),
	('David', '0987654321', 5000),
	('Diệu Hương', '09999', 4000);

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
	(1, 'Sting', 1, 'Nước giải khát', 10000, 7, ''),
	(2, 'Lavie', 1, 'Nước lọc Lavie', 10000, 8, ''),
	(3, 'Mỳ hảo hảo', 2, 'Mỳ ăn liền', 10000, 10, ''),
	(4, 'Clear Man', 3, 'Sữa tắm Clear Man', 25000, 6, '');

-- Dumping structure for table supermarket.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('manager','employee') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'employee',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.user: ~4 rows (approximately)
INSERT INTO `user` (`id`, `first_name`, `last_name`, `phone`, `role`, `username`, `password`) VALUES
	(1, 'Quốc Dũng', 'Đỗ ', '0923132', 'manager', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9'),
	(2, 'Rê Mon', 'Đỗ', '076744564', 'employee', 'employee1', 'employee'),
	(3, 'Messi', 'Lionel', '01323132', 'employee', 'c', 'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35'),
	(4, 'Cris', 'Backam', '0992332', 'manager', 'd', '1');

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
