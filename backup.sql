-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: cdnsg_db_v2
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `borrow_ticket`
--

DROP TABLE IF EXISTS `borrow_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `borrow_code` varchar(255) NOT NULL,
  `borrow_time` datetime(6) DEFAULT NULL,
  `borrower_name` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `note` text,
  `return_code` varchar(255) DEFAULT NULL,
  `return_time` datetime(6) DEFAULT NULL,
  `status` enum('BORROWED','COMPLETED','INCOMPLETE','NEW') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjdfb88a8yru3sgyd1chass556` (`borrow_code`),
  UNIQUE KEY `UK76vtwiccrygrpmj8hvdnpn4yg` (`return_code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_ticket`
--

LOCK TABLES `borrow_ticket` WRITE;
/*!40000 ALTER TABLE `borrow_ticket` DISABLE KEYS */;
INSERT INTO `borrow_ticket` VALUES (1,'M-91E0A981','2026-04-20 13:35:37.993650','Sinh viên Ngân ','2026-04-20 13:35:19.687889',_binary '\0','Khoa CSSĐ-NDT','vphat772@gmail.com','','T-4A36C644',NULL,'COMPLETED'),(3,'M-EF0598EC','2026-04-20 13:39:05.798781','Sinh viên: Ngân','2026-04-20 13:38:24.028989',_binary '\0','Khoa CSSĐ-NDT','haingan050605@gmail.com','','T-626B0E45',NULL,'COMPLETED'),(5,'M-FE3DA3C3','2026-04-21 13:31:46.477513','Sinh viên: Ngân ','2026-04-21 13:31:22.699188',_binary '\0','K. CSSĐ','haingan050605@gmail.com','','T-181682B9',NULL,'COMPLETED'),(7,'M-CA8577B2',NULL,'Sinh viên Nhân ','2026-04-23 16:08:21.045519',_binary '\0','K. KT-DL',NULL,'Thêm 11 nịt',NULL,NULL,'BORROWED');
/*!40000 ALTER TABLE `borrow_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_ticket_item`
--

DROP TABLE IF EXISTS `borrow_ticket_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_ticket_item` (
  `ticket_id` bigint NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  KEY `FKb1al2mgppoklk49en2hakerc9` (`ticket_id`),
  CONSTRAINT `FKb1al2mgppoklk49en2hakerc9` FOREIGN KEY (`ticket_id`) REFERENCES `borrow_ticket` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_ticket_item`
--

LOCK TABLES `borrow_ticket_item` WRITE;
/*!40000 ALTER TABLE `borrow_ticket_item` DISABLE KEYS */;
INSERT INTO `borrow_ticket_item` VALUES (1,'Đồng phục QP size XL',1),(1,'Nịt',1),(3,'Dây điện 10m ',1),(5,'Dây điện 10m',1),(7,'Đồng phục QP L',4),(7,'Đồng phục QP XL',2),(7,'Đồng phục QP M',5);
/*!40000 ALTER TABLE `borrow_ticket_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document_category`
--

DROP TABLE IF EXISTS `document_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjrue5sxvwmuts62qsomn5mo6w` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_category`
--

LOCK TABLES `document_category` WRITE;
/*!40000 ALTER TABLE `document_category` DISABLE KEYS */;
INSERT INTO `document_category` VALUES (1,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa'),(2,'Biên bản giao nhận hàng của Kho');
/*!40000 ALTER TABLE `document_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `export_item_detail`
--

DROP TABLE IF EXISTS `export_item_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `export_item_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `export_receipt_id` bigint DEFAULT NULL,
  `material_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjjy1mx1f44f3k1w85hithknj1` (`export_receipt_id`),
  KEY `FKqxn07d6pcagweq6f19y9kssdl` (`material_id`),
  CONSTRAINT `FKjjy1mx1f44f3k1w85hithknj1` FOREIGN KEY (`export_receipt_id`) REFERENCES `export_receipt` (`id`),
  CONSTRAINT `FKqxn07d6pcagweq6f19y9kssdl` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `export_item_detail`
--

LOCK TABLES `export_item_detail` WRITE;
/*!40000 ALTER TABLE `export_item_detail` DISABLE KEYS */;
INSERT INTO `export_item_detail` VALUES (1,2,5,97),(2,2,6,101),(3,2,6,107),(4,1,7,15),(5,1,8,34),(6,5,9,34),(7,2,9,40),(8,1,10,150),(10,3,11,179),(13,1,24,170),(15,1,25,103),(16,1,25,104),(17,2,25,106),(18,2,25,122),(19,2,25,108),(24,1,26,97),(27,1,27,148),(28,1,27,181),(29,1,27,82),(30,1,28,2),(31,10,23,43),(32,2,23,20),(33,2,23,47),(34,2,23,182),(35,1,29,97),(36,1,30,101),(37,5,31,101),(39,1,32,133),(40,4,32,34),(41,4,32,33),(42,1,33,96),(43,1,34,2);
/*!40000 ALTER TABLE `export_item_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `export_receipt`
--

DROP TABLE IF EXISTS `export_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `export_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `export_date` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `receipt_code` varchar(255) NOT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsx4vua8yjnlslcb51j5y6g3d9` (`receipt_code`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `export_receipt`
--

LOCK TABLES `export_receipt` WRITE;
/*!40000 ALTER TABLE `export_receipt` DISABLE KEYS */;
INSERT INTO `export_receipt` VALUES (1,'Admin','P.QTTB&CSVC','2026-04-14','Giấy cuộn lớn','PX-20260414-001','Mai','COMPLETED'),(2,'Admin','P.QTTB&CSVC','2026-04-14','Mũi khoan','PX-20260414-002','Thanh','COMPLETED'),(3,'Admin','P.QTTB&CSVC','2026-04-14','Mực 49A','PX-20260414-003','Nghĩa','COMPLETED'),(4,'Admin','P.QTTB&CSVC','2026-04-13','Nước lau kính','PX-20260413-001','Th Trung','COMPLETED'),(5,NULL,'P.QTTB','2026-04-16','','PX-20260416-001','Mai ','COMPLETED'),(6,NULL,'P.QTTB','2026-04-18','','PX-20260418-001','Nga Nhỏ ','COMPLETED'),(7,NULL,'P.QTTB','2026-04-18','','PX-20260418-002','Thanh ','COMPLETED'),(8,NULL,'P.HSSV','2026-04-20','','PX-20260420-001','Hùng ','COMPLETED'),(9,NULL,'P.QTTB','2026-04-18','','PX-20260418-003','Thanh','COMPLETED'),(10,NULL,'P.QTTB','2026-04-20','','PX-20260420-002','Tha ','COMPLETED'),(11,NULL,'P.QTTB','2026-04-15','','PX-20260420-003','Thanh ','COMPLETED'),(23,NULL,'P.QTTB','2026-04-20','','PX-20260419-001','Thanh','COMPLETED'),(24,NULL,'ffa','2026-04-20','','PX-20260420-004','ádas','CANCELLED'),(25,NULL,'P.QTTB','2026-04-20','','PX-20260420-005','Trinh','COMPLETED'),(26,NULL,'P.QTTB','2026-04-21','','PX-20260421-001','Mai','COMPLETED'),(27,NULL,'P.QTTB','2026-04-21','','PX-20260421-002','Thanh ','COMPLETED'),(28,NULL,'P.QTTB','2026-04-22','','PX-20260422-001','Nghĩa','COMPLETED'),(29,NULL,'P.QTTB','2026-04-22','','PX-20260422-002','Mai','COMPLETED'),(30,NULL,'P.QTTB','2026-04-23','','PX-20260423-001','Nga Lớn','COMPLETED'),(31,NULL,'kho','2026-01-23','','PX-20260123-001','kho','COMPLETED'),(32,NULL,'P.QTTB','2026-04-23','','PX-20260423-002','Thanh ','COMPLETED'),(33,NULL,'P.QTTB','2026-04-23','','PX-20260423-003','Nga Nhỏi','COMPLETED'),(34,NULL,'P.QTTB','2026-04-23','','PX-20260423-004','Nghĩa ','COMPLETED');
/*!40000 ALTER TABLE `export_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fire_extinguisher`
--

DROP TABLE IF EXISTS `fire_extinguisher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fire_extinguisher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_recharge_date` date DEFAULT NULL,
  `next_recharge_date` date DEFAULT NULL,
  `note` text,
  `quantity` int DEFAULT NULL,
  `status` enum('EXPIRED','OK','WARNING') DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `location_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnw94ydfhna68a4a1olt9wgt5` (`location_id`),
  CONSTRAINT `FKnw94ydfhna68a4a1olt9wgt5` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fire_extinguisher`
--

LOCK TABLES `fire_extinguisher` WRITE;
/*!40000 ALTER TABLE `fire_extinguisher` DISABLE KEYS */;
INSERT INTO `fire_extinguisher` VALUES (1,'2025-12-06','2026-06-06','',2,'OK','CO2','Bình','3',1),(2,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',2),(3,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',3),(4,'2025-12-06','2026-06-06','',4,'OK','CO2','Bình','3',4),(5,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',5),(6,'2025-12-06','2026-06-06','',2,'OK','CO2','Bình','3',6),(7,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',7),(8,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',8),(9,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',9),(10,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',10),(11,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',11),(12,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',11),(13,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',11),(14,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',12),(15,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',13),(16,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',14),(17,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',15),(18,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','3',16),(19,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',16),(20,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',17),(21,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',18),(22,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',19),(23,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',20),(24,'2025-12-06','2026-06-06','',2,'OK','CO2','Bình','5',21),(25,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',21),(26,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',23),(27,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',24),(28,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',25),(29,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',26),(30,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',27),(31,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',28),(32,'2025-12-06','2026-06-06','',1,'OK','CO2','Bình','5',29),(33,'2025-12-06','2026-06-06','',2,'OK','CO2','Bình','5',30),(34,'2025-12-06','2026-06-06','',3,'OK','CO2','Bình','5',31),(35,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','4',32),(36,'2025-12-06','2026-06-06','',7,'OK','Bột','Bình','4',33),(37,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',17),(38,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',18),(39,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',19),(40,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',27),(41,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',25),(42,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',26),(43,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',31),(44,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','35',31),(45,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','8',34),(46,'2025-12-06','2026-06-06','',1,'OK','Bột','Bình','35',34),(47,'2025-10-30','2026-04-30','',15,'OK','CO2','Bình','3',35),(48,'2025-10-30','2026-04-30','',7,'OK','CO2','Bình','3',36),(49,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','3',37),(50,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',38),(51,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',39),(52,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',40),(53,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',41),(54,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',42),(55,'2025-10-30','2026-04-30','',4,'OK','CO2','Bình','5',43),(56,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',38),(57,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',39),(58,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',40),(59,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',41),(60,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',42),(61,'2025-10-30','2026-04-30','',4,'OK','Bột','Bình','8',43),(62,'2025-10-30','2026-04-30','',7,'OK','CO2','Bình','3',44),(63,'2025-10-30','2026-04-30','',23,'OK','Bột','Bình','4',44),(64,'2025-10-30','2026-04-30','',1,'OK','Bột','Bình','8',44),(65,'2025-10-30','2026-04-30','',1,'OK','CO2','Bình','5',46),(66,'2025-10-30','2026-04-30','',2,'OK','CO2','Bình','3',46),(67,'2025-10-30','2026-04-30','',3,'OK','Bột','Bình','8',46),(68,'2025-10-30','2026-04-30','',5,'OK','Bột','Bình','4',46),(69,'2025-10-30','2026-04-30','',2,'OK','CO2','Bình','3',45),(70,'2025-10-30','2026-04-30','',3,'OK','Bột','Bình','8',45),(71,'2025-10-30','2026-04-30','',3,'OK','Bột','Bình','4',45),(72,'2026-03-03','2026-09-03','',3,'OK','CO2','Bình','5',47),(73,'2026-03-03','2026-09-03','',5,'OK','Bột','Bình','8',47),(74,'2026-03-03','2026-09-03','',5,'OK','CO2','Bình','3',48),(75,'2026-03-03','2026-09-03','',2,'OK','CO2','Bình','5',48),(76,'2026-03-03','2026-09-03','',7,'OK','Bột','Bình','4',48),(77,'2026-03-03','2026-09-03','',6,'OK','Bột','Bình','8',48);
/*!40000 ALTER TABLE `fire_extinguisher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fire_extinguisher_history`
--

DROP TABLE IF EXISTS `fire_extinguisher_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fire_extinguisher_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `next_recharge_date` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `recharge_date` date DEFAULT NULL,
  `extinguisher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrljkkuhbs9u8dxsfeotro6ib4` (`extinguisher_id`),
  CONSTRAINT `FKrljkkuhbs9u8dxsfeotro6ib4` FOREIGN KEY (`extinguisher_id`) REFERENCES `fire_extinguisher` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fire_extinguisher_history`
--

LOCK TABLES `fire_extinguisher_history` WRITE;
/*!40000 ALTER TABLE `fire_extinguisher_history` DISABLE KEYS */;
INSERT INTO `fire_extinguisher_history` VALUES (1,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',1),(2,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',2),(3,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',3),(4,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',4),(5,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',5),(6,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',6),(7,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',7),(8,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',8),(9,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',9),(10,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',10),(11,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',11),(12,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',12),(13,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',13),(14,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',14),(15,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',15),(16,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',16),(17,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',17),(18,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',18),(19,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',19),(20,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',20),(21,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',37),(22,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',21),(23,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',38),(24,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',22),(25,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',39),(26,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',23),(27,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',24),(28,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',25),(29,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',26),(30,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',27),(31,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',28),(32,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',41),(33,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',29),(34,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',42),(35,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',30),(36,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',40),(37,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',31),(38,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',32),(39,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',33),(40,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',34),(41,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',43),(42,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',44),(43,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',35),(44,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',36),(45,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',45),(46,'2026-06-06','Nạp hàng loạt theo khu vực','2025-12-06',46),(47,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',47),(48,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',48),(49,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',49),(50,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',50),(51,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',56),(52,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',51),(53,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',57),(54,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',52),(55,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',58),(56,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',53),(57,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',59),(58,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',54),(59,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',60),(60,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',55),(61,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',61),(62,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',62),(63,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',63),(64,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',64),(65,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',69),(66,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',70),(67,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',71),(68,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',65),(69,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',66),(70,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',67),(71,'2026-04-30','Nạp hàng loạt theo khu vực','2025-10-30',68),(72,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',72),(73,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',73),(74,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',74),(75,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',75),(76,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',76),(77,'2026-09-03','Nạp hàng loạt theo khu vực','2026-03-03',77);
/*!40000 ALTER TABLE `fire_extinguisher_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_item_detail`
--

DROP TABLE IF EXISTS `import_item_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_item_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `import_receipt_id` bigint DEFAULT NULL,
  `material_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1vsttcyptooei4a3rib9slibj` (`import_receipt_id`),
  KEY `FKhc1dij1xfq6bg08mlg0gxeusd` (`material_id`),
  CONSTRAINT `FK1vsttcyptooei4a3rib9slibj` FOREIGN KEY (`import_receipt_id`) REFERENCES `import_receipt` (`id`),
  CONSTRAINT `FKhc1dij1xfq6bg08mlg0gxeusd` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_item_detail`
--

LOCK TABLES `import_item_detail` WRITE;
/*!40000 ALTER TABLE `import_item_detail` DISABLE KEYS */;
INSERT INTO `import_item_detail` VALUES (2,3,7,179),(9,1,8,181),(10,5,8,133),(11,10,8,88),(12,5,8,169),(13,5,8,34),(14,5,8,142),(15,4,8,182),(16,5,8,183),(17,5,8,148),(18,1,9,185),(19,3,9,184),(20,5,10,26),(21,5,10,34),(22,5,10,33);
/*!40000 ALTER TABLE `import_item_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `import_receipt`
--

DROP TABLE IF EXISTS `import_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `import_date` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `receipt_code` varchar(255) NOT NULL,
  `status` enum('CANCELLED','COMPLETED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6oxggxdy98ehnuhq2q626fk9j` (`receipt_code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `import_receipt`
--

LOCK TABLES `import_receipt` WRITE;
/*!40000 ALTER TABLE `import_receipt` DISABLE KEYS */;
INSERT INTO `import_receipt` VALUES (1,'Admin','2026-04-14','Nhập kho','PN-20260414-001','COMPLETED'),(2,'Admin','2026-04-13','','PN-20260413-001','COMPLETED'),(3,'Admin','2026-04-10','','PN-20260410-001','COMPLETED'),(4,'Admin','2026-04-08','','PN-20260408-001','COMPLETED'),(5,'Admin','2026-04-06','','PN-20260406-001','COMPLETED'),(6,'Admin','2026-04-03','','PN-20260403-001','COMPLETED'),(7,'Admin','2026-04-20','','PN-20260420-001','COMPLETED'),(8,'Admin','2026-04-20','','PN-20260420-002','COMPLETED'),(9,'Admin','2025-01-22','','PN-20250122-001','COMPLETED'),(10,'Admin','2026-04-23','','PN-20260423-001','COMPLETED');
/*!40000 ALTER TABLE `import_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `zone_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKairbravxei9ggr7lysb7c6bu0` (`zone_id`),
  CONSTRAINT `FKairbravxei9ggr7lysb7c6bu0` FOREIGN KEY (`zone_id`) REFERENCES `zone` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Kho ',1),(2,'Phòng Bar',1),(3,'Văn phòng KT-DL ',1),(4,'Phòng Bếp Âu',1),(5,'Phòng Phó Hiệu trưởng ',1),(6,'Phòng Học sinh-Sinh viên ',1),(7,'Thư viện ',1),(8,'Phòng Hiệu trường ',1),(9,'Phòng Phó Hiệu trường 1',1),(10,'Phòng Thanh tra',1),(11,'Phòng Phó Hiệu trường 2',1),(12,'Phòng Đào tạo ',1),(13,'Nhà xe 2',1),(14,'Phòng KT-L (cũ) ',1),(15,'Phòng Hội đồng thưởng trực',1),(16,'Phòng Đào tạo láI xe',1),(17,'Không gian văn hóa HCM ',1),(18,'Y tế',1),(19,'Sư phạm mầm non ',1),(20,'Phòng Buồng',1),(21,'Phòng Bếp Á',1),(22,'Phòng Sự kiện ',1),(23,'Phòng Nhà hàng',1),(24,'Phòng Tổ chức - Hành chính',1),(25,'Phía sau phòng Tổ chức - Hành chính',1),(26,'Kế hoạch - Tài chính',1),(27,'Phía sau kế hoạch - Tài chính',1),(28,'Lầu 2 ',1),(29,'Giáo dục - Đại cương',1),(30,'Hội trường A',1),(31,'Bảo vệ ',1),(32,'Ghi danh',1),(33,'Nhà xe 4',1),(34,'Nhà xe 3',1),(35,'Phòng thực hành CNTT',2),(36,'Phòng thực hành Điện',2),(37,'Phòng thực hành khoa Y dược',2),(38,'Trệt',2),(39,'Lầu 1',2),(40,'Lầu 2',2),(41,'Lầu 3',2),(42,'Lầu 4',2),(43,'Sân thượng',2),(44,'Ký túc xá',3),(45,'Phòng thực hành Ô tô',4),(46,'Phòng thực hành Cơ khí',4),(47,'Phân hiệu Tạ Quang Bửu',5),(48,'Phân hiệu Bùi Minh Trực',7);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_ticket`
--

DROP TABLE IF EXISTS `maintenance_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completion_date` date DEFAULT NULL,
  `general_note` varchar(255) DEFAULT NULL,
  `start_date` date NOT NULL,
  `technician` varchar(255) DEFAULT NULL,
  `ticket_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_ticket`
--

LOCK TABLES `maintenance_ticket` WRITE;
/*!40000 ALTER TABLE `maintenance_ticket` DISABLE KEYS */;
INSERT INTO `maintenance_ticket` VALUES (1,'2026-04-21','','2026-04-21','bb','BT-C53F97DD'),(2,'2026-04-21','','2026-04-21','a','BT-D5C99137');
/*!40000 ALTER TABLE `maintenance_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `inventory` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `unit_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKffb619hl9g5jnro5kpyxfsld6` (`unit_id`),
  CONSTRAINT `FKffb619hl9g5jnro5kpyxfsld6` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
INSERT INTO `material` VALUES (1,_binary '\0',11,'Mực 12A',1),(2,_binary '\0',5,'Mực 107A',1),(3,_binary '\0',5,'Mực 1103A',1),(4,_binary '\0',2,'Mực 230A (051)',1),(5,_binary '\0',3,'Mực 49A',1),(6,_binary '\0',4,'Mực 30A',1),(7,_binary '\0',5,'Mực 505A',1),(8,_binary '\0',3,'Mực 217A',1),(9,_binary '\0',0,'Mực 76A',1),(10,_binary '\0',3,'Mực 226A',1),(11,_binary '\0',3,'Mực 85A',1),(12,_binary '\0',1,'Mực 7515',1),(13,_binary '\0',2,'Mực Olivetti',1),(14,_binary '\0',5,'Đèn tròn 30W',2),(15,_binary '\0',3,'Đèn tròn 20W',2),(16,_binary '\0',8,'Đèn trần 9W-tròn-đổi màu',2),(17,_binary '\0',4,'Đèn pha 50W',2),(18,_binary '\0',6,'Đèn 12W trần-tròn',2),(19,_binary '\0',2,'Đèn trụ 40W',2),(20,_binary '\0',10,'Mặt 3 ổ cắm',3),(21,_binary '\0',13,'Mặt 2 thiết bị',3),(22,_binary '\0',4,'Khóa thủ kính',3),(23,_binary '\0',4,'Khóa cửa 15Fx12',3),(24,_binary '\0',85,'Rj45-đầu bấm mạng',3),(25,_binary '\0',2,'Stator-44',3),(26,_binary '\0',7,'Stator-46',3),(27,_binary '\0',3,'CB C40-3 cực',3),(28,_binary '\0',6,'CB C32-2 cực',3),(29,_binary '\0',9,'CB C32-1 cực',3),(30,_binary '\0',5,'CB 30A-cóc',3),(31,_binary '\0',2,'CB 10A-cóc',3),(32,_binary '\0',2,'CB 40A-cóc',3),(33,_binary '\0',7,'Phích cái',3),(34,_binary '\0',6,'Phích đực',3),(35,_binary '\0',2,'Hít cửa',4),(36,_binary '\0',7,'Đuôi đèn tròn',3),(37,_binary '\0',12,'Cầu chì',3),(38,_binary '\0',5,'Công tắc gắn hộp',3),(39,_binary '\0',4,'Công tắc không hộp',3),(40,_binary '\0',41,'Đèn tuýp 1m2',2),(41,_binary '\0',2,'Đèn bán nguyệt 1m2',2),(42,_binary '\0',6,'Nẹp 4',3),(43,_binary '\0',4,'Nẹp 2',3),(44,_binary '\0',11,'Đèn 60cm',2),(45,_binary '\0',9,'Tụ quạt',3),(46,_binary '\0',6,'Ổ cắm đơn',3),(47,_binary '\0',6,'Đế nổi',3),(48,_binary '\0',4,'Hộp CB-cóc',3),(49,_binary '\0',3,'Hộp CB-3 pha',3),(50,_binary '\0',10,'Co 21',3),(51,_binary '\0',4,'Co 27',3),(52,_binary '\0',5,'Co 34',3),(53,_binary '\0',6,'Co 42',3),(54,_binary '\0',8,'T 21',3),(55,_binary '\0',5,'T 27',3),(56,_binary '\0',5,'T 34',3),(57,_binary '\0',10,'T 42',3),(58,_binary '\0',15,'Nối 21',3),(59,_binary '\0',15,'Nối 27',3),(60,_binary '\0',7,'Nối 34',3),(61,_binary '\0',9,'Nối 42',3),(62,_binary '\0',7,'Thu 27-21',3),(63,_binary '\0',6,'Thu 34-27',3),(64,_binary '\0',7,'Thu 42-34',3),(65,_binary '\0',5,'T thu 27-21',3),(66,_binary '\0',4,'T thu 34-21',3),(67,_binary '\0',9,'Nối RT 21',3),(68,_binary '\0',7,'Nối RT 27',3),(69,_binary '\0',8,'Nối RT 34',3),(70,_binary '\0',5,'Nối RT 42',3),(71,_binary '\0',9,'Nối RN 27',3),(72,_binary '\0',7,'Nối RN 34',3),(73,_binary '\0',5,'Nối RN 42',3),(74,_binary '\0',6,'Van 21',3),(75,_binary '\0',4,'Van 27',3),(76,_binary '\0',4,'Van 34',3),(77,_binary '\0',1,'Van 42',3),(78,_binary '\0',10,'Van nước gắn tường',3),(79,_binary '\0',2,'Ruột đồng hồ',3),(80,_binary '\0',4,'Vòi nước nóng',3),(81,_binary '\0',0,'Vòi nước lạnh',3),(82,_binary '\0',5,'Vòi xịt',3),(83,_binary '\0',12,'Cần gạt nước bồn cầu',3),(84,_binary '\0',5,'Cóc bồn cầu',3),(85,_binary '\0',1,'Rắc co 1 chiều 42',3),(86,_binary '\0',12,'Hố gas',3),(87,_binary '\0',3,'Van nước 2 đầu xã',3),(88,_binary '\0',13,'Băng keo điện',5),(89,_binary '\0',2,'Keo silicon',6),(90,_binary '',4,'Cờ Tổ quốc 185x120',9),(91,_binary '\0',7,'Cờ Đảng',9),(92,_binary '',3,'Cờ Tổ quốc 100x140',9),(93,_binary '\0',1,'Cờ phướng',4),(94,_binary '\0',40,'Ly giấy',18),(95,_binary '\0',2,'Ki rác nhựa',3),(96,_binary '\0',7,'Cây ki rác ',10),(97,_binary '\0',13,'Giấy cuộn lớn',5),(98,_binary '\0',0,'Cây cào nước',10),(99,_binary '\0',1,'Miếng cào nước',3),(100,_binary '\0',5,'Cây cào nước-Bộ',4),(101,_binary '\0',3,'Chổi chà',10),(102,_binary '\0',10,'Chổi cỏ',10),(103,_binary '\0',14,'Tẩy Vim',6),(104,_binary '\0',13,'Tẩy Javel',6),(105,_binary '\0',14,'Tẩy Okay',6),(106,_binary '\0',15,'Bột giặt',8),(107,_binary '\0',8,'Bao rác cực đại',7),(108,_binary '\0',5,'Bao rác trung',7),(109,_binary '\0',9,'Bao rác tiểu',7),(110,_binary '\0',3,'Cây lao bọt biển',10),(111,_binary '\0',6,'Bao tay cao su',13),(112,_binary '\0',1,'Khăn giấy rút',1),(113,_binary '\0',3,'Bộ xã Lavabo',4),(114,_binary '\0',9,'Ống Lavabo',15),(115,_binary '\0',7,'Dây cấp nước 1m2',16),(116,_binary '\0',4,'Tấm lao nhà-90Cm',9),(117,_binary '\0',1,'Tấm lao nhà-45Cm',9),(118,_binary '\0',0,'Cùm Omega 21',3),(119,_binary '\0',9,'Cùm Omega 27',3),(120,_binary '\0',7,'Cùm Omega 34',3),(121,_binary '\0',5,'Cùm Omega 42',3),(122,_binary '\0',3,'Xà bông cục',14),(123,_binary '\0',3,'Dây HDMI-3m',16),(124,_binary '\0',4,'T bồn cầu',3),(125,_binary '\0',7,'Xịt côn trùng Raid',12),(126,_binary '\0',2,'Vòi sen',3),(127,_binary '\0',4,'Phao cấp nước',3),(128,_binary '\0',24,'Nước rữa chén',6),(129,_binary '\0',12,'Nước lau sàn',6),(130,_binary '\0',8,'Xám thơm',14),(131,_binary '\0',2,'Dây HDMI-15m',16),(132,_binary '\0',2,'Dây điện đôi 1.5',5),(133,_binary '\0',4,'Keo dán nẹp',5),(134,_binary '\0',15,'Nước lau kính',6),(135,_binary '\0',530,'Túi vải NSG',8),(136,_binary '\0',3,'Ổ khóa số',3),(137,_binary '\0',0,'Cây cào lá',10),(138,_binary '\0',0,'Băng keo vải',5),(139,_binary '\0',1,'Dĩa cước',3),(140,_binary '\0',1,'Tẩy Somo',6),(141,_binary '\0',11,'Chân đèn đơn',13),(142,_binary '\0',8,'Chân đèn đôi',13),(143,_binary '\0',8,'Dimmer quạt',3),(144,_binary '\0',50,'Lưỡi cắt sắt',3),(145,_binary '\0',4,'Dây cấp nước 1m5',16),(146,_binary '\0',4,'Dây cấp nước 60cm',16),(147,_binary '\0',1,'Vít xà gồ-1 phân',8),(148,_binary '\0',6,'Kẽm',5),(149,_binary '\0',0,'Dây dăn cảnh báo',5),(150,_binary '\0',2,'Cây lao nhà',10),(151,_binary '\0',2,'Keo AB',20),(152,_binary '\0',3700,'Bao thư NSG (nhỏ)',3),(153,_binary '\0',0,'Xi lanh',3),(154,_binary '\0',0,'Công tắc ấm đun siêu tốc',3),(155,_binary '\0',44,'Bình trà NSG',4),(156,_binary '\0',9,'Máng đèn đôi',3),(157,_binary '\0',20,'Túi nilong trong',7),(158,_binary '\0',4,'Ống 21',15),(159,_binary '\0',4,'Ống 27',15),(160,_binary '\0',8,'Tép long',19),(161,_binary '\0',10,'Dây rút',8),(162,_binary '\0',1260,'Viết NSG',10),(163,_binary '\0',0,'Đất Mekong',18),(164,_binary '\0',0,'Ống tưới cây 21',16),(165,_binary '\0',5,'Nước tẩy bồn cầu',6),(166,_binary '\0',0,'Nguồn 400w-12v',3),(167,_binary '\0',3,'Máy lạnh',4),(168,_binary '\0',0,'Quạt hút H250',4),(169,_binary '\0',6,'CB C63 - 2 cực',3),(170,_binary '\0',1,'Lavabo gắn tường',3),(171,_binary '\0',5,'Vòi lavabo',3),(172,_binary '\0',6,'Ấn xã tiểu nam',3),(173,_binary '\0',2,'Đồng hồ điện',3),(174,_binary '\0',4,'Ống 114',15),(175,_binary '\0',8,'Bánh xe 100x38',3),(176,_binary '\0',0,'Sắt vuông 20 - 1 ly 2',10),(177,_binary '\0',0,'Bảng lề 15cm',3),(178,_binary '\0',0,'CB C50 - 1 cực',3),(179,_binary '\0',0,'Nắp bồn cầu',3),(180,_binary '\0',0,'Mũi khoan 10',3),(181,_binary '\0',0,'Họp mạch điện',3),(182,_binary '\0',2,'Nẹp bán nguyệt 3 phân',10),(183,_binary '\0',5,'Cánh quạt ',3),(184,_binary '\0',3,'	Cờ Tổ quốc 185x120',9),(185,_binary '\0',1,'Cờ Tổ quốc 100x140',9);
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_document`
--

DROP TABLE IF EXISTS `media_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_document` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `public_id` varchar(255) NOT NULL,
  `upload_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_document`
--

LOCK TABLES `media_document` WRITE;
/*!40000 ALTER TABLE `media_document` DISABLE KEYS */;
INSERT INTO `media_document` VALUES (1,'Biên bản giao nhận hàng của Kho',NULL,486286,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657332/warehouse_documents/zukouultpbkdadnxomye.pdf','12-01-26 Phieu giao hang tui vai','warehouse_documents/zukouultpbkdadnxomye','2026-04-20 10:55:33.238293'),(2,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,775544,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657344/warehouse_documents/fmly5gka80krwc2ktgfq.pdf','15-01-26 ban giao K-KTDL','warehouse_documents/fmly5gka80krwc2ktgfq','2026-04-20 10:55:44.794672'),(3,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,601507,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657355/warehouse_documents/e5m6xtvbsyo1xcdp5ret.pdf','21-01-26 ban giao K-CK','warehouse_documents/e5m6xtvbsyo1xcdp5ret','2026-04-20 10:55:56.583714'),(4,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,1419060,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657369/warehouse_documents/ookkrdak8sqf2ajcxc9g.pdf','09-02-26 ban giao K-CK','warehouse_documents/ookkrdak8sqf2ajcxc9g','2026-04-20 10:56:10.072067'),(5,'Biên bản giao nhận hàng của Kho',NULL,859401,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657383/warehouse_documents/pjcfq0cfrwrkwxxvwna8.pdf','10-02-26 bien ban nap muc','warehouse_documents/pjcfq0cfrwrkwxxvwna8','2026-04-20 10:56:23.538342'),(6,'Biên bản giao nhận hàng của Kho',NULL,908865,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657397/warehouse_documents/k7re1kgmabjx7b7bfmix.pdf','12-02-26 bien ban tra muc','warehouse_documents/k7re1kgmabjx7b7bfmix','2026-04-20 10:56:37.535829'),(7,'Biên bản giao nhận hàng của Kho',NULL,698346,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657408/warehouse_documents/ohqzbjaxowvzmtqsy00f.pdf','12-02-26 bien ban tra muc loi','warehouse_documents/ohqzbjaxowvzmtqsy00f','2026-04-20 10:56:49.279188'),(8,'Biên bản giao nhận hàng của Kho',NULL,541585,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657425/warehouse_documents/jyoyax2hp71vkrqlynrb.pdf','12-02-26 bien ban giao hang TNP','warehouse_documents/jyoyax2hp71vkrqlynrb','2026-04-20 10:57:06.000413'),(9,'Biên bản giao nhận hàng của Kho',NULL,782753,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657445/warehouse_documents/m95dindi4lqqpdl4yvbb.pdf','03-03-26 giao may chieu','warehouse_documents/m95dindi4lqqpdl4yvbb','2026-04-20 10:57:25.877951'),(10,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,578843,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657476/warehouse_documents/iwkqnsz7dmiqbogk7xjb.pdf','05-03-26 ban giao P-TS','warehouse_documents/iwkqnsz7dmiqbogk7xjb','2026-04-20 10:57:56.737466'),(11,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,674418,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657494/warehouse_documents/ea81grnek7gmr6518jir.pdf','11-03-26 ban giao K-CNTT','warehouse_documents/ea81grnek7gmr6518jir','2026-04-20 10:58:14.951239'),(12,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,674418,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657507/warehouse_documents/aiegfp7jb5bjybkgpp82.pdf','11-03-26 ban giao K-CNTT','warehouse_documents/aiegfp7jb5bjybkgpp82','2026-04-20 10:58:28.055108'),(13,'Biên bản giao nhận hàng của Kho',NULL,943777,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657524/warehouse_documents/ravcnrb6syteljkdytmr.pdf','14-03-26 giao may chieu','warehouse_documents/ravcnrb6syteljkdytmr','2026-04-20 10:58:45.075688'),(14,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,737652,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657535/warehouse_documents/zzsc8g6upfvc5g2ye3hm.pdf','19-03-26 ban giao K-CNTT','warehouse_documents/zzsc8g6upfvc5g2ye3hm','2026-04-20 10:58:55.898165'),(15,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,1453336,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657554/warehouse_documents/aa9la4xumpplv1t7q029.pdf','27-03-26 ban giao K-YD','warehouse_documents/aa9la4xumpplv1t7q029','2026-04-20 10:59:14.783859'),(16,'Biên bản giao nhận hàng của Kho',NULL,528232,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657566/warehouse_documents/gmbkwbypmegxvzc3jhvz.pdf','31-03-26 bien giao hang TNP','warehouse_documents/gmbkwbypmegxvzc3jhvz','2026-04-20 10:59:27.306839'),(17,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,1354344,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657576/warehouse_documents/mgcrfx244jxzmpqt0sgs.pdf','01-04-26 ban giao K-CNTT','warehouse_documents/mgcrfx244jxzmpqt0sgs','2026-04-20 10:59:37.104858'),(18,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,1255219,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657597/warehouse_documents/ku3qte8btlgs6m9aacm2.pdf','08-04-26 ban giao K-CK','warehouse_documents/ku3qte8btlgs6m9aacm2','2026-04-20 10:59:57.643023'),(19,'Biên bản bàn giao giữa P.QTTB và Phòng/Khoa',NULL,1181854,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657610/warehouse_documents/zgcftjytvf7eawunxqet.pdf','08-04-26 ban giao K-CK','warehouse_documents/zgcftjytvf7eawunxqet','2026-04-20 11:00:10.659979'),(20,'Biên bản giao nhận hàng của Kho',NULL,752493,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776657619/warehouse_documents/kzmzsrpnozkaggkjubl3.pdf','13-04-26 bien ban giao hang TNP','warehouse_documents/kzmzsrpnozkaggkjubl3','2026-04-20 11:00:19.980832'),(21,'Biên bản giao nhận hàng của Kho',NULL,586219,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776762351/warehouse_documents/vdyxnvkiwaeybjcpfy0v.pdf','21-04-26 bang ke ban phe lieu','warehouse_documents/vdyxnvkiwaeybjcpfy0v','2026-04-21 16:05:52.549383'),(22,'Biên bản giao nhận hàng của Kho',NULL,838638,'image/pdf','https://res.cloudinary.com/petrol-user/image/upload/v1776847411/warehouse_documents/tbrfrfsvmpblo7hd0sd5.pdf','22-04-26 giao mực','warehouse_documents/tbrfrfsvmpblo7hd0sd5','2026-04-22 15:43:31.046917');
/*!40000 ALTER TABLE `media_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projector`
--

DROP TABLE IF EXISTS `projector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projector` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `serial_number` varchar(255) NOT NULL,
  `status` enum('AVAILABLE','BORROWED','BROKEN','IN_USE','UNDER_MAINTENANCE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKafajvxo9kej9a34mpvx7tf5e6` (`serial_number`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projector`
--

LOCK TABLES `projector` WRITE;
/*!40000 ALTER TABLE `projector` DISABLE KEYS */;
INSERT INTO `projector` VALUES (1,'Eiki','E89A2278','UNDER_MAINTENANCE'),(2,'Eiki','E89A2266','UNDER_MAINTENANCE'),(3,'Eiki','E66A1042','BROKEN'),(4,'Eiki','E89A2256','BORROWED');
/*!40000 ALTER TABLE `projector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projector_loan`
--

DROP TABLE IF EXISTS `projector_loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projector_loan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `borrow_date` datetime(6) NOT NULL,
  `borrower` varchar(255) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `return_date` datetime(6) DEFAULT NULL,
  `status` enum('BORROWING','RETURNED') NOT NULL,
  `projector_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg3irgpk9wji8chevcdy8pi0qq` (`projector_id`),
  CONSTRAINT `FKg3irgpk9wji8chevcdy8pi0qq` FOREIGN KEY (`projector_id`) REFERENCES `projector` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projector_loan`
--

LOCK TABLES `projector_loan` WRITE;
/*!40000 ALTER TABLE `projector_loan` DISABLE KEYS */;
INSERT INTO `projector_loan` VALUES (1,'2026-03-25 14:48:00.000000','B0.15',' | Ghi chú khi trả: ','2026-04-20 14:48:58.873239','RETURNED',1),(2,'2026-04-03 14:49:00.000000','B0.15',' | Ghi chú khi trả: ','2026-04-20 14:50:24.134432','RETURNED',2),(3,'2026-04-03 14:50:00.000000','B0.15',' | Ghi chú khi trả: ','2026-04-20 14:50:48.776546','RETURNED',3),(4,'2026-04-03 14:51:00.000000','B0.15','',NULL,'BORROWING',4);
/*!40000 ALTER TABLE `projector_loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projector_maintenance_detail`
--

DROP TABLE IF EXISTS `projector_maintenance_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projector_maintenance_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cost` double DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `projector_id` bigint NOT NULL,
  `ticket_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnghpi9ovpkxekkw3ad552bpoj` (`projector_id`),
  KEY `FKjj6iyy0sv1vk4tgrluolsympv` (`ticket_id`),
  CONSTRAINT `FKjj6iyy0sv1vk4tgrluolsympv` FOREIGN KEY (`ticket_id`) REFERENCES `maintenance_ticket` (`id`),
  CONSTRAINT `FKnghpi9ovpkxekkw3ad552bpoj` FOREIGN KEY (`projector_id`) REFERENCES `projector` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projector_maintenance_detail`
--

LOCK TABLES `projector_maintenance_detail` WRITE;
/*!40000 ALTER TABLE `projector_maintenance_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `projector_maintenance_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uniform`
--

DROP TABLE IF EXISTS `uniform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uniform` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `size` varchar(255) NOT NULL,
  `stock` bigint DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKwcvsmhqxl2pv79t7s2h4bt2n` (`type`,`size`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uniform`
--

LOCK TABLES `uniform` WRITE;
/*!40000 ALTER TABLE `uniform` DISABLE KEYS */;
INSERT INTO `uniform` VALUES (1,'7XL',10,'Đồng phục Thể dục '),(2,'6XL',49,'Đồng phục Thể dục'),(3,'5XL',8,'Đồng phục Thể dục'),(4,'4XL',72,'Đồng phục Thể dục'),(5,'3XL',292,'Đồng phục Thể dục'),(6,'2XL',197,'Đồng phục Thể dục'),(7,'XL',0,'Đồng phục Thể dục'),(8,'L',196,'Đồng phục Thể dục'),(9,'M',74,'Đồng phục Thể dục'),(10,'S',66,'Đồng phục Thể dục'),(11,'7XL',10,'Áo sơ mi Nữ'),(12,'6XL',0,'Áo sơ mi Nữ'),(13,'5XL',90,'Áo sơ mi Nữ'),(14,'4XL',135,'Áo sơ mi Nữ'),(15,'3XL',224,'Áo sơ mi Nữ'),(17,'2XL',195,'Áo sơ mi Nữ'),(18,'XL',306,'Áo sơ mi Nữ'),(19,'L',92,'Áo sơ mi Nữ'),(20,'M',316,'Áo sơ mi Nữ'),(21,'S',279,'Áo sơ mi Nữ'),(22,'7XL',10,'Áo sơ mi Nam'),(23,'6XL',99,'Áo sơ mi Nam'),(24,'5XL',38,'Áo sơ mi Nam'),(26,'4XL',182,'Áo sơ mi Nam'),(27,'3XL',193,'Áo sơ mi Nam'),(28,'2XL',189,'Áo sơ mi Nam'),(29,'XL',152,'Áo sơ mi Nam'),(30,'L',64,'Áo sơ mi Nam'),(31,'M',179,'Áo sơ mi Nam'),(32,'S',25,'Áo sơ mi Nam');
/*!40000 ALTER TABLE `uniform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uniform_import`
--

DROP TABLE IF EXISTS `uniform_import`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uniform_import` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `name_response` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uniform_import`
--

LOCK TABLES `uniform_import` WRITE;
/*!40000 ALTER TABLE `uniform_import` DISABLE KEYS */;
INSERT INTO `uniform_import` VALUES (1,'2026-04-20','Nhà cung cấp ĐP','Nhập định kì',NULL);
/*!40000 ALTER TABLE `uniform_import` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uniform_import_detail`
--

DROP TABLE IF EXISTS `uniform_import_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uniform_import_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` bigint DEFAULT NULL,
  `uniform_id` bigint DEFAULT NULL,
  `uniform_import_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq8mhgndd2uxad0ekp77rm9n4` (`uniform_id`),
  KEY `FKdwaqw8omsxp8uu7ewou0iu5f` (`uniform_import_id`),
  CONSTRAINT `FKdwaqw8omsxp8uu7ewou0iu5f` FOREIGN KEY (`uniform_import_id`) REFERENCES `uniform_import` (`id`),
  CONSTRAINT `FKq8mhgndd2uxad0ekp77rm9n4` FOREIGN KEY (`uniform_id`) REFERENCES `uniform` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uniform_import_detail`
--

LOCK TABLES `uniform_import_detail` WRITE;
/*!40000 ALTER TABLE `uniform_import_detail` DISABLE KEYS */;
INSERT INTO `uniform_import_detail` VALUES (1,10,1,1),(2,26,2,1),(3,8,3,1),(4,72,4,1),(5,292,5,1),(6,98,6,1),(7,99,6,1),(8,196,8,1),(9,74,9,1),(10,66,10,1),(11,10,11,1),(12,23,2,1),(13,90,13,1),(14,135,14,1),(15,224,15,1),(16,195,17,1),(17,307,18,1),(18,92,19,1),(19,316,20,1),(20,279,21,1),(21,10,22,1),(22,99,23,1),(23,38,24,1),(24,182,26,1),(25,193,27,1),(26,189,28,1),(27,152,29,1),(28,64,30,1),(29,179,31,1),(30,25,32,1);
/*!40000 ALTER TABLE `uniform_import_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uniform_receipt`
--

DROP TABLE IF EXISTS `uniform_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uniform_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cus_name` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `total_quantity` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uniform_receipt`
--

LOCK TABLES `uniform_receipt` WRITE;
/*!40000 ALTER TABLE `uniform_receipt` DISABLE KEYS */;
INSERT INTO `uniform_receipt` VALUES (1,'Lê Nguyễn Mỹ Kim','2026-04-20',1);
/*!40000 ALTER TABLE `uniform_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uniform_receipt_detail`
--

DROP TABLE IF EXISTS `uniform_receipt_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uniform_receipt_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` bigint DEFAULT NULL,
  `uniform_id` bigint DEFAULT NULL,
  `uniform_receipt_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcd77sxdvyfdmdu3edcm86hk5s` (`uniform_id`),
  KEY `FKgia9sj6lext3d55yb17ii3an` (`uniform_receipt_id`),
  CONSTRAINT `FKcd77sxdvyfdmdu3edcm86hk5s` FOREIGN KEY (`uniform_id`) REFERENCES `uniform` (`id`),
  CONSTRAINT `FKgia9sj6lext3d55yb17ii3an` FOREIGN KEY (`uniform_receipt_id`) REFERENCES `uniform_receipt` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uniform_receipt_detail`
--

LOCK TABLES `uniform_receipt_detail` WRITE;
/*!40000 ALTER TABLE `uniform_receipt_detail` DISABLE KEYS */;
INSERT INTO `uniform_receipt_detail` VALUES (1,1,18,1);
/*!40000 ALTER TABLE `uniform_receipt_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaa58c9de9eu0v585le47w25my` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (17,'Bao'),(12,'Bình'),(4,'Bộ'),(2,'Bóng'),(3,'Cái'),(13,'Cặp'),(10,'Cây'),(6,'Chai'),(14,'Cục'),(5,'Cuộn'),(16,'Dây'),(1,'Hộp'),(7,'Kg'),(18,'Ly'),(11,'Met'),(15,'Ống'),(9,'Tấm'),(20,'Tép'),(19,'Thùng'),(8,'Túi'),(21,'Tuýp');
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone`
--

DROP TABLE IF EXISTS `zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zone` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` text,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK27s7q3vqft9a76yi9k7e7rroi` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone`
--

LOCK TABLES `zone` WRITE;
/*!40000 ALTER TABLE `zone` DISABLE KEYS */;
INSERT INTO `zone` VALUES (1,'','Khu A'),(2,'','Khu B'),(3,'','Ký túc xá'),(4,'','Khoa Cơ khí'),(5,'','Phân hiệu Tạ Quang Bửu'),(7,'','Phân hiệu Bùi Minh Trực');
/*!40000 ALTER TABLE `zone` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-24  9:10:31
