--
-- Table structure for table `cotizacion`
--
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `cotizacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cotizacion` (
                              `id_folio` int NOT NULL AUTO_INCREMENT,
                              `id_usuario` int NOT NULL,
                              `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `tipo_proyecto` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                              `descripcion` mediumtext COLLATE utf8mb4_general_ci NOT NULL,
                              `cliente` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
                              `precio_final` decimal(10,2) NOT NULL,
                              `is_cotizacion_aprobada` tinyint(1) DEFAULT '0',
                              `is_contrato_aprobado` tinyint(1) DEFAULT '0',
                              `fecha` date NOT NULL,
                              PRIMARY KEY (`id_folio`),
                              KEY `fk_usuario` (`id_usuario`),
                              CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
                              CONSTRAINT `chk_precio_final` CHECK ((`precio_final` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cotizacion`
--

LOCK TABLES `cotizacion` WRITE;
/*!40000 ALTER TABLE `cotizacion` DISABLE KEYS */;
INSERT INTO `cotizacion` VALUES (1,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(3,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(5,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(7,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(9,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(11,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14'),(13,2,NULL,'Caja Luminosa','Descripción Automática del proyecto Playwright','Cliente Automatizado',2586.00,0,0,'2026-04-14');
/*!40000 ALTER TABLE `cotizacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cotizacion_mano_de_obra`
--

DROP TABLE IF EXISTS `cotizacion_mano_de_obra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cotizacion_mano_de_obra` (
                                           `id_folio` int NOT NULL,
                                           `num_responsable` int NOT NULL,
                                           `costo_hora` decimal(10,2) NOT NULL,
                                           `cantidad_horas` decimal(10,2) NOT NULL,
                                           `subtotal` decimal(10,2) NOT NULL,
                                           PRIMARY KEY (`id_folio`,`num_responsable`),
                                           CONSTRAINT `fk_cotizacion_mano` FOREIGN KEY (`id_folio`) REFERENCES `cotizacion` (`id_folio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cotizacion_mano_de_obra`
--

LOCK TABLES `cotizacion_mano_de_obra` WRITE;
/*!40000 ALTER TABLE `cotizacion_mano_de_obra` DISABLE KEYS */;
/*!40000 ALTER TABLE `cotizacion_mano_de_obra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cotizacion_material`
--

DROP TABLE IF EXISTS `cotizacion_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cotizacion_material` (
                                       `id_folio` int NOT NULL,
                                       `id_material` int NOT NULL,
                                       `cantidad` decimal(10,2) NOT NULL,
                                       `subtotal` decimal(10,2) NOT NULL,
                                       PRIMARY KEY (`id_folio`,`id_material`),
                                       KEY `fk_material` (`id_material`),
                                       CONSTRAINT `fk_cotizacion` FOREIGN KEY (`id_folio`) REFERENCES `cotizacion` (`id_folio`) ON DELETE CASCADE ON UPDATE CASCADE,
                                       CONSTRAINT `fk_material` FOREIGN KEY (`id_material`) REFERENCES `material` (`id_material`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cotizacion_material`
--

LOCK TABLES `cotizacion_material` WRITE;
/*!40000 ALTER TABLE `cotizacion_material` DISABLE KEYS */;
INSERT INTO `cotizacion_material` VALUES (1,12,10.00,2155.00),(3,12,10.00,2155.00),(5,12,10.00,2155.00),(7,12,10.00,2155.00),(9,12,10.00,2155.00),(11,12,10.00,2155.00),(13,12,10.00,2155.00);
/*!40000 ALTER TABLE `cotizacion_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datos_encuesta`
--

DROP TABLE IF EXISTS `datos_encuesta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datos_encuesta` (
                                  `id_encuesta` int NOT NULL AUTO_INCREMENT,
                                  `q1` tinyint(1) DEFAULT NULL,
                                  `q2` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `q3` int DEFAULT NULL,
                                  PRIMARY KEY (`id_encuesta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datos_encuesta`
--

LOCK TABLES `datos_encuesta` WRITE;
/*!40000 ALTER TABLE `datos_encuesta` DISABLE KEYS */;
/*!40000 ALTER TABLE `datos_encuesta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material` (
                            `id_material` int NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(128) COLLATE utf8mb4_general_ci NOT NULL,
                            `tipo_material` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `costo` decimal(10,2) NOT NULL,
                            `tipo_unidad` enum('lts','m²','kgs','pzas','mts','otro') COLLATE utf8mb4_general_ci NOT NULL,
                            PRIMARY KEY (`id_material`),
                            KEY `idx_nombre_material` (`nombre`),
                            CONSTRAINT `chk_costo_material` CHECK ((`costo` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
INSERT INTO `material` VALUES (1,'Vinil de Corte Negro Matte','Vinil',150.00,'mts'),(2,'Lona Frontlit 13oz','Lona',85.50,'mts'),(3,'Placa de Acrílico Transparente 3mm','Acrílico',1200.00,'pzas'),(4,'Tubo de Neón Flex LED Azul','Iluminación',210.00,'mts'),(5,'Lámina de Alucobond Blanco','Estructura',850.00,'pzas'),(6,'Adhesivo de Montaje Rápido','Pegamento',120.00,'pzas'),(7,'Tinta de Impresión UV Cian','Consumible',3400.00,'lts'),(8,'Electrodos para Soldadura','Ferretería',45.00,'kgs'),(9,'Malla Mesh para Fachada','Lona',95.00,'m²'),(10,'Barniz Protector UV','Acabado',560.00,'lts'),(12,'cemento cruz azul','construccion',215.50,'otro'),(13,'cemento cruz azul','construccion',250.00,'otro'),(19,'Material de Prueba 1776194169365','Lona',200.50,'lts'),(20,'Material de Prueba 1776194287753','Lona',200.50,'lts'),(21,'Material de Prueba 1776194450780','Lona',200.50,'lts'),(22,'Material de Prueba 1776194556072','Lona',200.50,'lts'),(23,'Material de Prueba 1776194610254','Lona',200.50,'lts'),(24,'Material de Prueba 1776194724504','Lona',200.50,'lts'),(25,'Material de Prueba 1776194843576','Lona',200.50,'lts'),(26,'Material de Prueba 1776194921575','Lona',200.50,'lts'),(27,'Material de Prueba 1776195017983','Lona',200.50,'lts'),(28,'Material de Prueba 1776195161383','Lona',200.50,'lts'),(29,'Material de Prueba 1776195307619','Lona',250.75,'lts'),(47,'Material de Prueba 1776225726843','Lona',250.75,'lts');
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_taller`
--

DROP TABLE IF EXISTS `pedidos_taller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_taller` (
                                  `id_folio` int NOT NULL,
                                  `estado_en_taller` enum('Por iniciar','En producción','Con impedimentos','Terminado') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Por iniciar',
                                  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `prioridad` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  PRIMARY KEY (`id_folio`),
                                  CONSTRAINT `fk_pedido_cotizacion` FOREIGN KEY (`id_folio`) REFERENCES `cotizacion` (`id_folio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_taller`
--

LOCK TABLES `pedidos_taller` WRITE;
/*!40000 ALTER TABLE `pedidos_taller` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_taller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `us_datos_sensibles`
--

DROP TABLE IF EXISTS `us_datos_sensibles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `us_datos_sensibles` (
                                      `id_usuario` int NOT NULL,
                                      `rfc` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
                                      `email` varchar(254) COLLATE utf8mb4_general_ci NOT NULL,
                                      PRIMARY KEY (`id_usuario`),
                                      UNIQUE KEY `rfc` (`rfc`),
                                      UNIQUE KEY `email` (`email`),
                                      UNIQUE KEY `idx_correo_usuario` (`email`),
                                      CONSTRAINT `fk_usdatossensibles_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `chk_rfc_length` CHECK ((char_length(`rfc`) = 13))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `us_datos_sensibles`
--

LOCK TABLES `us_datos_sensibles` WRITE;
/*!40000 ALTER TABLE `us_datos_sensibles` DISABLE KEYS */;
INSERT INTO `us_datos_sensibles` VALUES (1,'hfki28djfngue','john.garcia.viray@uabc.edu.mx'),(2,'uwndie082937d','abraham.flores.cabanillas@uabc.edu.mx'),(3,'djwmei019284h','brayan.leon93@uabc.edu.mx');
/*!40000 ALTER TABLE `us_datos_sensibles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `us_psswds`
--

DROP TABLE IF EXISTS `us_psswds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `us_psswds` (
                             `id_usuario` int NOT NULL,
                             `psswd` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
                             `ultima_modificacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id_usuario`),
                             CONSTRAINT `fk_uspsswd_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `us_psswds`
--

LOCK TABLES `us_psswds` WRITE;
/*!40000 ALTER TABLE `us_psswds` DISABLE KEYS */;
INSERT INTO `us_psswds` VALUES (1,'0a1e4211dda7f090808df28c3d7cf52663cf6f427939581413aa7c38f574b0a2','2026-04-13 20:13:26'),(2,'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','2026-04-14 18:52:36'),(3,'03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','2026-04-14 18:52:36');
/*!40000 ALTER TABLE `us_psswds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
                           `id_usuario` int NOT NULL AUTO_INCREMENT,
                           `codigo_tipo_usuario` int NOT NULL,
                           `nombre` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                           `apellido_prim` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                           `apellido_seg` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `fecha_nacimiento` date NOT NULL,
                           `fecha_creacion_usuario` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `estado` tinyint(1) NOT NULL DEFAULT '1',
                           PRIMARY KEY (`id_usuario`),
                           KEY `idx_nombre_usuario` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,0,'John','Garcia','Viray','2005-01-09','2026-04-13 20:11:41',0),(2,1,'Abraham','Flores','Razo','2005-06-14','2026-04-14 18:51:12',0),(3,2,'Brayan','Leon','Frausto','2005-04-19','2026-04-14 18:51:40',0);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS = 1;