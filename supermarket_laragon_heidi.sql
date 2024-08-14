-- --------------------------------------------------------
-- Máy chủ:                      127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Phiên bản:           12.1.0.6537
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.bill: ~16 rows (approximately)
INSERT INTO `bill` (`id`, `user_id`, `user_name`, `customer_phone`, `customer_name`, `total_amount`, `created_at`) VALUES
	(4, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 07:24:27'),
	(5, 3, 'c', '0123', 'Giang', 20000, '2024-08-08 07:31:24'),
	(6, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 07:49:16'),
	(7, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 07:50:29'),
	(8, 3, 'c', '0123', 'Giang', 25000, '2024-08-08 07:54:19'),
	(9, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 08:00:09'),
	(10, 3, 'c', '0923123', 'Huong', 10000, '2024-08-08 16:13:46'),
	(11, 3, 'c', '0943534', 'Dung', 25000, '2024-08-08 16:14:11'),
	(12, 3, 'c', '0123', 'Giang', 10000, '2024-08-08 18:45:19'),
	(13, 3, 'c', '0123', 'Giang', 80000, '2024-08-08 19:00:57'),
	(14, 3, 'c', '09999', 'ABC', 25000, '2024-08-09 13:02:21'),
	(15, 3, 'c', '0123', 'Giang', 25000, '2024-08-09 13:11:04'),
	(16, 3, 'c', '0923123', 'Huong', 9900, '2024-08-09 19:09:23'),
	(17, 3, 'c', '0943534', 'Dung', 49750, '2024-08-09 19:18:04'),
	(18, 7, 'employee2', '094234332', 'Hai', 80000, '2024-08-12 06:29:35'),
	(19, 7, 'employee2', '09231234', 'Huong', 65000, '2024-08-12 06:44:08');

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
	(1, 3, 4, 'Mỳ hảo hảo', 1, 10000),
	(2, 2, 5, 'Lavie', 2, 10000),
	(3, 2, 6, 'Lavie', 1, 10000),
	(4, 2, 7, 'Lavie', 1, 10000),
	(5, 4, 8, 'Clear Man', 1, 25000),
	(6, 2, 9, 'Lavie', 1, 10000),
	(7, 2, 10, 'Lavie', 1, 10000),
	(8, 4, 11, 'Clear Man', 1, 25000),
	(9, 2, 12, 'Lavie', 1, 10000),
	(10, 5, 13, 'Bim Bim 123', 8, 10000),
	(11, 4, 14, 'Clear Man', 1, 25000),
	(12, 4, 15, 'Clear Man', 1, 25000),
	(13, 2, 16, 'Lavie', 1, 10000),
	(14, 4, 17, 'Clear Man', 2, 25000),
	(15, 2, 18, 'Lavie', 5, 10000),
	(16, 1, 18, 'Sting', 3, 10000),
	(17, 2, 19, 'Lavie', 3, 10000),
	(18, 4, 19, 'Clear Man', 1, 25000),
	(19, 1, 19, 'Sting', 1, 10000);

-- Dumping structure for table supermarket.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.category: ~14 rows (approximately)
INSERT INTO `category` (`id`, `name`, `description`) VALUES
	(1, 'Nước', 'nước uống'),
	(2, 'Mỳ', 'Mỳ ăn liền'),
	(3, 'Sữa tắm', 'Các loại sữa tắm'),
	(4, 'Đồ ăn vặt', 'Đồ ăn vặt trẻ em'),
	(5, 'Bánh kẹo', 'Các loại bánh kẹo'),
	(6, 'Gia vị', 'Các loại gia vị nấu ăn'),
	(7, 'Đồ hộp', 'Thực phẩm đóng hộp'),
	(8, 'Nước ép', 'Nước ép trái cây'),
	(9, 'Rau củ quả', 'Rau củ quả tươi'),
	(10, 'Thịt cá', 'Thịt cá tươi sống'),
	(11, 'Đồ đông lạnh', 'Thực phẩm đông lạnh'),
	(12, 'Đồ gia dụng', 'Đồ gia dụng trong nhà bếp'),
	(13, 'Đồ dùng học tập', 'Đồ dùng học tập cho học sinh'),
	(14, 'Sản phẩm chăm sóc cá nhân', 'Các sản phẩm chăm sóc cá nhân');

-- Dumping structure for table supermarket.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `points` int DEFAULT '0',
  PRIMARY KEY (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.customer: ~6 rows (approximately)
INSERT INTO `customer` (`name`, `phone`, `points`) VALUES
	('abcd', '0123', 0),
	('Huong', '0923123', 0),
	('Huong', '09231234', 750),
	('Hai', '094234332', 800),
	('Dung', '0943534', 0),
	('ABC', '09999', 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.product: ~54 rows (approximately)
INSERT INTO `product` (`id`, `name`, `category_id`, `description`, `price`, `quantity`, `status`) VALUES
	(1, 'Sting', 1, 'Nước giải khát', 10000, 6, 'AVAILABLE'),
	(2, 'Lavie', 1, 'Nước lọc Lavie', 10000, 1, 'AVAILABLE'),
	(3, 'Mỳ hảo hảo', 2, 'Mỳ ăn liền', 10000, 20, 'AVAILABLE'),
	(4, 'Clear Man', 3, 'Sữa tắm Clear Man', 25000, 19, 'AVAILABLE'),
	(5, 'Bim Bim 123', 4, 'Đồ ăn vặt cho trẻ em', 10000, 42, 'AVAILABLE'),
	(6, 'Kẹo cao su Doublemint', 5, 'Kẹo cao su hương bạc hà', 5000, 100, 'AVAILABLE'),
	(7, 'Socola KitKat', 5, 'Socola KitKat', 25000, 100, 'AVAILABLE'),
	(8, 'Bánh snack Oishi', 5, 'Bánh snack vị tôm', 10000, 100, 'AVAILABLE'),
	(9, 'Kẹo dẻo Haribo', 5, 'Kẹo dẻo hình gấu Haribo', 30000, 100, 'AVAILABLE'),
	(10, 'Muối i-ốt', 6, 'Muối i-ốt tinh khiết', 8000, 100, 'AVAILABLE'),
	(11, 'Tiêu đen', 6, 'Tiêu đen xay', 15000, 100, 'AVAILABLE'),
	(12, 'Nước mắm Nam Ngư', 6, 'Nước mắm Nam Ngư chai', 25000, 100, 'AVAILABLE'),
	(13, 'Bột ngọt Ajinomoto', 6, 'Bột ngọt Ajinomoto', 20000, 100, 'AVAILABLE'),
	(14, 'Dầu ăn Neptune', 6, 'Dầu ăn Neptune chai 1L', 35000, 100, 'AVAILABLE'),
	(15, 'Cá hộp 3 miền', 7, 'Cá hộp 3 miền sốt cà', 20000, 100, 'AVAILABLE'),
	(16, 'Thịt hộp Spam', 7, 'Thịt hộp Spam', 45000, 100, 'AVAILABLE'),
	(17, 'Đậu hộp Heinz', 7, 'Đậu hộp Heinz', 30000, 100, 'AVAILABLE'),
	(18, 'Sữa đặc ông Thọ', 7, 'Sữa đặc có đường ông Thọ', 20000, 100, 'AVAILABLE'),
	(19, 'Rau củ hộp Green Giant', 7, 'Rau củ hộp Green Giant', 40000, 100, 'AVAILABLE'),
	(20, 'Nước ép cam Twister', 8, 'Nước ép cam Twister', 25000, 100, 'AVAILABLE'),
	(21, 'Nước ép táo Tropicana', 8, 'Nước ép táo Tropicana', 30000, 100, 'AVAILABLE'),
	(22, 'Nước ép cà rốt Vfresh', 8, 'Nước ép cà rốt Vfresh', 20000, 100, 'AVAILABLE'),
	(23, 'Nước ép nho Welch', 8, 'Nước ép nho Welch', 35000, 100, 'AVAILABLE'),
	(24, 'Nước ép dứa Del Monte', 8, 'Nước ép dứa Del Monte', 32000, 100, 'AVAILABLE'),
	(25, 'Rau cải xanh', 9, 'Rau cải xanh tươi', 15000, 100, 'AVAILABLE'),
	(26, 'Cà chua bi', 9, 'Cà chua bi tươi', 20000, 100, 'AVAILABLE'),
	(27, 'Khoai tây', 9, 'Khoai tây tươi', 10000, 100, 'AVAILABLE'),
	(28, 'Bí đỏ', 9, 'Bí đỏ tươi', 12000, 100, 'AVAILABLE'),
	(29, 'Cà rốt', 9, 'Cà rốt tươi', 15000, 100, 'AVAILABLE'),
	(30, 'Thịt bò tươi', 10, 'Thịt bò tươi loại 1', 120000, 100, 'AVAILABLE'),
	(31, 'Thịt heo', 10, 'Thịt heo tươi loại 1', 80000, 100, 'AVAILABLE'),
	(32, 'Cá hồi', 10, 'Cá hồi tươi nguyên con', 150000, 100, 'AVAILABLE'),
	(33, 'Gà ta nguyên con', 10, 'Gà ta tươi nguyên con', 90000, 100, 'AVAILABLE'),
	(34, 'Tôm sú', 10, 'Tôm sú tươi sống', 180000, 100, 'AVAILABLE'),
	(35, 'Bánh bao đông lạnh', 11, 'Bánh bao đông lạnh', 25000, 100, 'AVAILABLE'),
	(36, 'Thịt viên đông lạnh', 11, 'Thịt viên đông lạnh', 30000, 100, 'AVAILABLE'),
	(37, 'Cá viên đông lạnh', 11, 'Cá viên đông lạnh', 20000, 100, 'AVAILABLE'),
	(38, 'Rau củ đông lạnh', 11, 'Rau củ đông lạnh', 15000, 100, 'AVAILABLE'),
	(39, 'Kem Wall', 11, 'Kem Wall các vị', 45000, 100, 'AVAILABLE'),
	(40, 'Nồi cơm điện', 12, 'Nồi cơm điện 1.8L', 450000, 100, 'AVAILABLE'),
	(41, 'Bình đun siêu tốc', 12, 'Bình đun siêu tốc 1.5L', 300000, 100, 'AVAILABLE'),
	(42, 'Chảo chống dính', 12, 'Chảo chống dính 26cm', 200000, 100, 'AVAILABLE'),
	(43, 'Lò vi sóng', 12, 'Lò vi sóng 23L', 1200000, 100, 'AVAILABLE'),
	(44, 'Máy xay sinh tố', 12, 'Máy xay sinh tố Philips', 500000, 100, 'AVAILABLE'),
	(45, 'Bút bi Thiên Long', 13, 'Bút bi Thiên Long xanh', 5000, 100, 'AVAILABLE'),
	(46, 'Vở ô ly', 13, 'Vở ô ly 200 trang', 15000, 100, 'AVAILABLE'),
	(47, 'Thước kẻ 20cm', 13, 'Thước kẻ nhựa 20cm', 5000, 100, 'AVAILABLE'),
	(48, 'Cặp sách học sinh', 13, 'Cặp sách học sinh chống gù', 200000, 100, 'AVAILABLE'),
	(49, 'Bút chì', 13, 'Bút chì gỗ 2B', 3000, 100, 'AVAILABLE'),
	(50, 'Kem đánh răng Colgate', 14, 'Kem đánh răng Colgate', 25000, 100, 'AVAILABLE'),
	(51, 'Dầu gội Sunsilk', 14, 'Dầu gội Sunsilk đen', 45000, 100, 'AVAILABLE'),
	(52, 'Xà phòng Lifebuoy', 14, 'Xà phòng Lifebuoy đỏ', 20000, 100, 'AVAILABLE'),
	(53, 'Sữa rửa mặt Acnes', 14, 'Sữa rửa mặt Acnes 100g', 60000, 100, 'AVAILABLE'),
	(54, 'Khăn giấy ướt', 14, 'Khăn giấy ướt Huggies', 15000, 100, 'AVAILABLE');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarket.user: ~7 rows (approximately)
INSERT INTO `user` (`id`, `first_name`, `last_name`, `phone`, `role`, `username`, `password`, `image`) VALUES
	(1, 'Quốc Dũng', 'Đỗ ', '0923132', 'manager', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', NULL),
	(2, 'Rê Mon', 'Đỗ', '076744564', 'employee', 'employee1', 'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', NULL),
	(3, 'Messi', 'Lionel', '01323132', 'employee', 'c', '5f16c3e2f49a617db5d9b49a3b433af11b054f8ee5700e3cb5b004c4cce20dca', NULL),
	(4, 'Cris', 'Backam', '0992332', 'employee', 'd2', 'd4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35', NULL),
	(5, 'giang', 'nguyen', '0123456789', 'employee', 'john01', '5f16c3e2f49a617db5d9b49a3b433af11b054f8ee5700e3cb5b004c4cce20dca', NULL),
	(6, 'Hello', 'Kitty', '0912312311', 'employee', 'hello', 'bbf4fcf939ca472423c7a60f759a233f336c7ac40c39c843cc72fe76ca5ed9e0', NULL),
	(7, 'Duy', 'Anh', '0934342345', 'employee', 'employee2', 'bbf4fcf939ca472423c7a60f759a233f336c7ac40c39c843cc72fe76ca5ed9e0', NULL);

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
