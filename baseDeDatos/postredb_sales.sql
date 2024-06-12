-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: postredb
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `idSales` int NOT NULL AUTO_INCREMENT,
  `tableId` int DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `saleTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(10) DEFAULT 'No Pagada',
  PRIMARY KEY (`idSales`),
  KEY `tableId` (`tableId`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`tableId`) REFERENCES `tablescustomers` (`idTable`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,1,'Quesabirrias',45,NULL,'2024-06-11 02:54:40','Pagado'),(2,2,'Quesabirrias',45,NULL,'2024-06-11 02:55:43','Pagado'),(3,2,'pepsi Lata',25,NULL,'2024-06-11 03:03:40','Pagado'),(4,2,'Taco al pastor',35,NULL,'2024-06-11 03:03:40','Pagado'),(5,1,'pepsi Lata',25,NULL,'2024-06-11 03:39:14','Pagado'),(6,1,'pepsi Lata',25,NULL,'2024-06-11 04:38:02','Pagado'),(7,1,'Flan napolitano (Rebanada)',42,NULL,'2024-06-11 04:38:02','Pagado'),(8,3,'pepsi Lata',25,NULL,'2024-06-11 04:38:30','Pagado'),(9,4,'pepsi Lata',25,NULL,'2024-06-11 05:48:27','Pagado'),(10,4,'Taco al pastor',35,NULL,'2024-06-11 05:48:27','Pagado'),(11,4,'Flan napolitano (Rebanada)',42,NULL,'2024-06-11 05:48:27','Pagado'),(12,1,'Quesabirrias',45,NULL,'2024-06-11 08:31:49','Pagado'),(13,1,'Taco al pastor',35,NULL,'2024-06-11 08:31:49','Pagado'),(14,1,'Taco de cohinita',25,NULL,'2024-06-11 08:31:49','Pagado'),(15,1,'Flan napolitano (Rebanada)',42,NULL,'2024-06-11 08:31:49','Pagado'),(16,1,'Taco de cohinita',25,NULL,'2024-06-11 08:31:49','Pagado'),(17,1,'Taco de cohinita',25,NULL,'2024-06-11 08:31:49','Pagado'),(18,1,'pepsi Lata',25,NULL,'2024-06-11 18:38:40','No Pagada'),(19,1,'Quesabirrias',45,NULL,'2024-06-11 18:38:40','No Pagada'),(20,1,'Taco al pastor',35,NULL,'2024-06-11 18:38:40','No Pagada'),(21,1,'pepsi Lata',25,NULL,'2024-06-11 23:21:47','No Pagada');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-11 17:52:12
