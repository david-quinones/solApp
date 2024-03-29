-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: escola_bressol
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `alumne`
--

DROP TABLE IF EXISTS `alumne`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumne` (
  `id` int NOT NULL AUTO_INCREMENT,
  `actiu` tinyint(1) NOT NULL,
  `menjador` tinyint(1) NOT NULL,
  `acollida` tinyint(1) NOT NULL,
  `persona_id` int DEFAULT NULL,
  `aula_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_persona` (`persona_id`),
  KEY `fk_aula` (`aula_id`),
  CONSTRAINT `fk_aula` FOREIGN KEY (`aula_id`) REFERENCES `aula` (`id`),
  CONSTRAINT `fk_persona` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumne`
--

LOCK TABLES `alumne` WRITE;
/*!40000 ALTER TABLE `alumne` DISABLE KEYS */;
INSERT INTO `alumne` VALUES (1,1,0,0,31,NULL),(2,1,0,1,32,NULL),(3,1,1,1,33,NULL),(4,1,0,0,34,4),(5,1,1,1,5,NULL),(8,0,0,0,52,2),(26,1,0,0,73,NULL);
/*!40000 ALTER TABLE `alumne` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `alumne_AFTER_UPDATE` AFTER UPDATE ON `alumne` FOR EACH ROW BEGIN

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `aula`
--

DROP TABLE IF EXISTS `aula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aula` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `professor_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `professor_id` (`professor_id`),
  CONSTRAINT `aula_ibfk_1` FOREIGN KEY (`professor_id`) REFERENCES `empleat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aula`
--

LOCK TABLES `aula` WRITE;
/*!40000 ALTER TABLE `aula` DISABLE KEYS */;
INSERT INTO `aula` VALUES (2,'TestIntegracio',1),(4,'TestIntegracio',1),(5,'ProvaEliminarCorrecte',NULL);
/*!40000 ALTER TABLE `aula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleat`
--

DROP TABLE IF EXISTS `empleat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `actiu` tinyint(1) NOT NULL,
  `inici_contracte` date DEFAULT NULL,
  `final_contracte` date DEFAULT NULL,
  `persona_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `persona_id` (`persona_id`),
  CONSTRAINT `empleat_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleat`
--

LOCK TABLES `empleat` WRITE;
/*!40000 ALTER TABLE `empleat` DISABLE KEYS */;
INSERT INTO `empleat` VALUES (1,1,'2022-01-01','9999-12-31',1),(2,1,'2021-06-15','2024-06-14',2),(3,1,'2020-12-01',NULL,3),(4,1,'2023-01-01',NULL,4),(31,1,'2000-01-01','9999-12-31',53),(33,0,'2000-01-01','2024-01-01',74);
/*!40000 ALTER TABLE `empleat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `missatge`
--

DROP TABLE IF EXISTS `missatge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `missatge` (
  `id` int NOT NULL AUTO_INCREMENT,
  `remitent_id` int NOT NULL,
  `destinatari_id` int NOT NULL,
  `contingut` text NOT NULL,
  `data_enviament` datetime NOT NULL,
  `remitent_esborrat` tinyint(1) DEFAULT '0',
  `destinatari_esborrat` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `remitent_id` (`remitent_id`),
  KEY `destinatari_id` (`destinatari_id`),
  CONSTRAINT `missatge_ibfk_1` FOREIGN KEY (`remitent_id`) REFERENCES `persona` (`id`),
  CONSTRAINT `missatge_ibfk_2` FOREIGN KEY (`destinatari_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `missatge`
--

LOCK TABLES `missatge` WRITE;
/*!40000 ALTER TABLE `missatge` DISABLE KEYS */;
INSERT INTO `missatge` VALUES (3,2,1,'Estic fent una prova d\'un missatge.','2023-11-27 17:37:10',0,0),(6,1,1,'Missatge prova integració.','2023-11-29 17:50:30',0,0),(7,1,2,'Missatge prova integració.','2023-11-29 17:50:30',0,0),(8,2,1,'Estic fent una prova d\'un missatge.','2023-11-30 17:45:17',0,1);
/*!40000 ALTER TABLE `missatge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `cognom1` varchar(255) NOT NULL,
  `cognom2` varchar(255) DEFAULT NULL,
  `data_naixement` date NOT NULL,
  `dni` varchar(9) DEFAULT NULL,
  `telefon` varchar(9) DEFAULT NULL,
  `mail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Pau','Castell','Galtes','1983-08-07','46797529G','645878955','pau@gmail.com'),(2,'PauCastellGaltes','CognomPersona1','CognomPersona2','2000-01-01','1245874L','999999999','provapersona@gmail.com'),(3,'Carlos','González','Rodríguez','1988-12-10','54321678C','654987321','carlos@example.com'),(4,'Ana','Martínez','Sánchez','1992-03-08','87651234D','789654321','ana@example.com'),(5,'Pablo','Jiménez','Pérez','1995-07-12','43218765E','159753468','pablo@example.com'),(6,'Juan','Pérez','García','1990-05-15','12345678A','123456789','juan@example.com'),(7,'María','López','Fernández','1985-09-23','87654321B','987654321','maria@example.com'),(30,'prova2','prova2',NULL,'2000-01-01',NULL,NULL,NULL),(31,'Juan','Gomez','Lopez','2022-02-15',NULL,'999999999','juan@gmail.com'),(32,'Maria','Perez','Rodriguez','2021-09-10','12756678A','987654321','maria@gmail.com'),(33,'Pedro','Martinez','Gutierrez','2023-04-20','98765432B','654321987','pedro@gmail.com'),(34,'Laura','Garcia','Fernandez','2022-08-22',NULL,'789456123','laura@gmail.com'),(38,'TestModificat','UnitariModificat','PauCastellModificat','1983-08-07','00000000K','Modificat','Modificat'),(52,'testAlumne','cognomAlumne','cognomAlumne2','2021-08-06','8748555H','111111111','alumne@gmail.com'),(53,'testResposta','cognomResposta1','cognomResposta2','1983-02-06','1111111T','587458746','resposta@gmail.com'),(73,'AlumneIntegracioModificat','cognomIntegracio','cognomIntegracio2','2022-08-23','45645645P','111111111','alumneInt@gmail.com'),(74,'TestEmpleatIntegracioModificat','TestEMpleatIntegracio','TestIntegracio','1983-02-06','1111111G','587458745','TestIntegracio@gmail.com');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuari`
--

DROP TABLE IF EXISTS `usuari`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuari` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_usuari` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  `is_teacher` tinyint(1) unsigned zerofill DEFAULT '0',
  `persona_id` int DEFAULT NULL,
  `actiu` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nom_usuari_UNIQUE` (`nom_usuari`),
  KEY `persona_id` (`persona_id`),
  CONSTRAINT `usuari_ibfk_1` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuari`
--

LOCK TABLES `usuari` WRITE;
/*!40000 ALTER TABLE `usuari` DISABLE KEYS */;
INSERT INTO `usuari` VALUES (1,'nom_usuariProva','Ylil4Ot3KRHU+SvltdsOFFEe2+AdHQ3dHVosuduaVro=',1,0,NULL,1),(2,'PAU','siHZ27CDp/M0KNfCo8MZiuklYU1wIQ4ocWzKp81N23k=',1,0,1,1),(18,'testRespostaModificat','V1RsmLBkCI+iGQCu1arc7tFHCS3aSWe8VclqIJ9UeJs=',0,1,31,1),(22,'testConsulta','XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=',1,0,38,1),(23,'NouUsuariModificat','XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=',0,1,2,1),(37,'testAlumne','XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=',1,0,52,0),(38,'testResposta10','XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=',1,0,53,1),(58,'testAlumneIntegracio','dwqEp6HvGgmzLwiMPJzM2GvfzvWygxhMkV1B39JRLRI=',1,0,73,1),(59,'TestEmpleatIntegracio','L+AC4nQwHOIpWVRfFJpag7vVItd2kjkW72R+zrjbdFk=',1,0,74,0);
/*!40000 ALTER TABLE `usuari` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `usuari_AFTER_UPDATE` AFTER UPDATE ON `usuari` FOR EACH ROW BEGIN
IF NEW.actiu <> OLD.actiu THEN
        -- Actualizar empleado
        UPDATE empleat
        SET actiu = NEW.actiu
        WHERE persona_id = NEW.persona_id;

        -- Actualizar alumno
        UPDATE alumne
        SET actiu = NEW.actiu
        WHERE persona_id = NEW.persona_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-11 19:54:31
