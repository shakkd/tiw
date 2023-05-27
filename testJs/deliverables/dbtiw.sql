CREATE DATABASE  IF NOT EXISTS `dbtest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dbtest`;
-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: 127.0.0.1    Database: dbtest
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `Appello`
--

DROP TABLE IF EXISTS `Appello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Appello` (
  `nomeCorso` varchar(25) NOT NULL,
  `data` date NOT NULL,
  PRIMARY KEY (`nomeCorso`,`data`),
  KEY `DATE` (`data`),
  CONSTRAINT `nomecors` FOREIGN KEY (`nomeCorso`) REFERENCES `Corso` (`nomeCorso`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Appello`
--

LOCK TABLES `Appello` WRITE;
/*!40000 ALTER TABLE `Appello` DISABLE KEYS */;
INSERT INTO `Appello` VALUES ('Analisi 1','2023-01-01'),('Analisi 2','2023-01-02'),('Algoritmi','2023-01-04'),('Analisi 1','2023-01-05'),('Analisi 2','2023-01-25'),('Fisica 1','2023-02-03'),('Analisi 1','2023-03-09');
/*!40000 ALTER TABLE `Appello` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Corso`
--

DROP TABLE IF EXISTS `Corso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Corso` (
  `nomeCorso` varchar(25) NOT NULL,
  `idUtente` int NOT NULL,
  PRIMARY KEY (`nomeCorso`),
  KEY `abc_idx` (`idUtente`),
  CONSTRAINT `id` FOREIGN KEY (`idUtente`) REFERENCES `Utente` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Corso`
--

LOCK TABLES `Corso` WRITE;
/*!40000 ALTER TABLE `Corso` DISABLE KEYS */;
INSERT INTO `Corso` VALUES ('Algoritmi',2),('Fisica 1',4),('Analisi 1',7),('Analisi 2',7);
/*!40000 ALTER TABLE `Corso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CorsoLaurea`
--

DROP TABLE IF EXISTS `CorsoLaurea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CorsoLaurea` (
  `nomeCorsoLaurea` varchar(25) NOT NULL,
  PRIMARY KEY (`nomeCorsoLaurea`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CorsoLaurea`
--

LOCK TABLES `CorsoLaurea` WRITE;
/*!40000 ALTER TABLE `CorsoLaurea` DISABLE KEYS */;
INSERT INTO `CorsoLaurea` VALUES ('Chimica'),('Elettrica'),('Elettronica'),('Informatica'),('Matematica');
/*!40000 ALTER TABLE `CorsoLaurea` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IscrizAppello`
--

DROP TABLE IF EXISTS `IscrizAppello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IscrizAppello` (
  `nomeCorso` varchar(25) NOT NULL,
  `data` date NOT NULL,
  `idUtente` int NOT NULL,
  `esito` varchar(45) DEFAULT NULL,
  `stato` varchar(45) NOT NULL,
  PRIMARY KEY (`nomeCorso`,`idUtente`,`data`),
  KEY `dataappell_idx` (`data`),
  KEY `idappell_idx` (`idUtente`),
  KEY `voto_idx` (`esito`),
  CONSTRAINT `corsoappell` FOREIGN KEY (`nomeCorso`) REFERENCES `Corso` (`nomeCorso`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dataappell` FOREIGN KEY (`data`) REFERENCES `Appello` (`data`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idappell` FOREIGN KEY (`idUtente`) REFERENCES `Utente` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `voto` FOREIGN KEY (`esito`) REFERENCES `Voto` (`nomeVoto`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `iscrizappello_chk_1` CHECK (((`stato` = _utf8mb4'Non inserito') or (`stato` = _utf8mb4'Inserito') or (`stato` = _utf8mb4'Pubblicato') or (`stato` = _utf8mb4'Rifiutato') or (`stato` = _utf8mb4'Verbalizzato')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IscrizAppello`
--

LOCK TABLES `IscrizAppello` WRITE;
/*!40000 ALTER TABLE `IscrizAppello` DISABLE KEYS */;
INSERT INTO `IscrizAppello` VALUES ('Algoritmi','2023-01-04',3,NULL,'Non inserito'),('Algoritmi','2023-01-04',8,NULL,'Non inserito'),('Analisi 1','2023-01-01',1,NULL,'Non inserito'),('Analisi 1','2023-01-02',1,NULL,'Non inserito'),('Analisi 1','2023-01-01',3,NULL,'Non inserito'),('Analisi 1','2023-01-02',3,NULL,'Non inserito'),('Analisi 1','2023-01-01',5,NULL,'Non inserito'),('Analisi 1','2023-01-02',8,NULL,'Non inserito'),('Analisi 2','2023-01-02',1,NULL,'Non inserito'),('Analisi 2','2023-01-02',5,NULL,'Non inserito'),('Analisi 2','2023-01-25',8,NULL,'Non inserito'),('Fisica 1','2023-02-03',1,NULL,'Non inserito'),('Fisica 1','2023-02-03',5,NULL,'Non inserito'),('Fisica 1','2023-02-03',8,NULL,'Non inserito');
/*!40000 ALTER TABLE `IscrizAppello` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IscrizCorso`
--

DROP TABLE IF EXISTS `IscrizCorso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IscrizCorso` (
  `idUtente` int NOT NULL,
  `nomeCorso` varchar(45) NOT NULL,
  PRIMARY KEY (`idUtente`,`nomeCorso`),
  CONSTRAINT `idiscriz` FOREIGN KEY (`idUtente`) REFERENCES `Utente` (`idUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IscrizCorso`
--

LOCK TABLES `IscrizCorso` WRITE;
/*!40000 ALTER TABLE `IscrizCorso` DISABLE KEYS */;
INSERT INTO `IscrizCorso` VALUES (1,'Algoritmi'),(1,'Analisi 1'),(1,'Analisi 2'),(3,'Algoritmi'),(3,'Analisi 1'),(5,'Analisi 1'),(8,'Algoritmi'),(8,'Analisi 1'),(8,'Analisi 2'),(8,'Fisica 1');
/*!40000 ALTER TABLE `IscrizCorso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utente`
--

DROP TABLE IF EXISTS `Utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utente` (
  `idUtente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `cognome` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `flag` varchar(45) NOT NULL,
  `matricola` int DEFAULT NULL,
  `nomeCorsoLaurea` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idUtente`),
  KEY `CdL_idx` (`nomeCorsoLaurea`),
  CONSTRAINT `CdL` FOREIGN KEY (`nomeCorsoLaurea`) REFERENCES `CorsoLaurea` (`nomeCorsoLaurea`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `utente_chk_1` CHECK (((`flag` = _utf8mb4'S') or (`flag` = _utf8mb4'D')))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utente`
--

LOCK TABLES `Utente` WRITE;
/*!40000 ALTER TABLE `Utente` DISABLE KEYS */;
INSERT INTO `Utente` VALUES (1,'Mirko','Gentile','mirko.gentile@mail.polimi.it','provamirko','S',123456,'Informatica'),(2,'Piero','Bianchi','piero.bianchi@mail.polimi.it','pieropiero9','D',NULL,NULL),(3,'Francesco','Gangi','francesco.gangi@mail.polimi.it','bigfranci','S',232143,'Elettronica'),(4,'Franco','Rossi','franco.rossi@mail.polimi.it','rossi9','D',NULL,NULL),(5,'Rosa','Ianigro','rosa.ianigro@mail.polimi.it','rosarosa','S',374632,'Elettrica'),(6,'Francesca','Verdi','francesca.verdi@mail.polimi.it','fraverdi','D',NULL,NULL),(7,'Carlo','Rosi','carlo.rosi@mail.polimi.it','carletto10','D',NULL,NULL),(8,'Donato','Angeli','donato.angeli@mail.polimi.it','angedona','S',653754,'Chimica');
/*!40000 ALTER TABLE `Utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Voto`
--

DROP TABLE IF EXISTS `Voto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Voto` (
  `nomeVoto` varchar(25) NOT NULL,
  PRIMARY KEY (`nomeVoto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Voto`
--

LOCK TABLES `Voto` WRITE;
/*!40000 ALTER TABLE `Voto` DISABLE KEYS */;
INSERT INTO `Voto` VALUES ('18'),('19'),('20'),('21'),('22'),('23'),('24'),('25'),('26'),('27'),('28'),('29'),('30'),('30L'),('Assente'),('Rimandato'),('Riprovato');
/*!40000 ALTER TABLE `Voto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'dbtest'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-17 17:03:33
