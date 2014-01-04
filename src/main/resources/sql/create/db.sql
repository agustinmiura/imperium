-- Dumping database structure for imperium
DROP DATABASE IF EXISTS `imperium`;
CREATE DATABASE IF NOT EXISTS `imperium` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `imperium`;


-- Dumping structure for table imperium.im_application
DROP TABLE IF EXISTS `im_application`;
CREATE TABLE IF NOT EXISTS `im_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apiKey` varchar(255) NOT NULL,
  `description` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_application: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_application` ENABLE KEYS */;


-- Dumping structure for table imperium.im_permission
DROP TABLE IF EXISTS `im_permission`;
CREATE TABLE IF NOT EXISTS `im_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `resource` varchar(255) NOT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3C652FCAD38AFFCC` (`application_id`),
  CONSTRAINT `FK3C652FCAD38AFFCC` FOREIGN KEY (`application_id`) REFERENCES `im_application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_permission: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_permission` ENABLE KEYS */;


-- Dumping structure for table imperium.im_permission_role
DROP TABLE IF EXISTS `im_permission_role`;
CREATE TABLE IF NOT EXISTS `im_permission_role` (
  `permission_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `FK4CA357CB89DAD108` (`permission_id`),
  KEY `FK4CA357CBA905D628` (`role_id`),
  CONSTRAINT `FK4CA357CBA905D628` FOREIGN KEY (`role_id`) REFERENCES `im_role` (`id`),
  CONSTRAINT `FK4CA357CB89DAD108` FOREIGN KEY (`permission_id`) REFERENCES `im_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_permission_role: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_permission_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_permission_role` ENABLE KEYS */;


-- Dumping structure for table imperium.im_role
DROP TABLE IF EXISTS `im_role`;
CREATE TABLE IF NOT EXISTS `im_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK71DEC2B1D38AFFCC` (`application_id`),
  CONSTRAINT `FK71DEC2B1D38AFFCC` FOREIGN KEY (`application_id`) REFERENCES `im_application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_role: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_role` ENABLE KEYS */;


-- Dumping structure for table imperium.im_subject
DROP TABLE IF EXISTS `im_subject`;
CREATE TABLE IF NOT EXISTS `im_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK707CA511D38AFFCC` (`application_id`),
  CONSTRAINT `FK707CA511D38AFFCC` FOREIGN KEY (`application_id`) REFERENCES `im_application` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_subject: ~3 rows (approximately)
/*!40000 ALTER TABLE `im_subject` DISABLE KEYS */;
INSERT INTO `im_subject` (`id`, `name`, `application_id`) VALUES
    (1, 'admin', NULL),
    (2, 'root', NULL),
    (3, 'user', NULL);
/*!40000 ALTER TABLE `im_subject` ENABLE KEYS */;


-- Dumping structure for table imperium.im_subject_role
DROP TABLE IF EXISTS `im_subject_role`;
CREATE TABLE IF NOT EXISTS `im_subject_role` (
  `subject_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`subject_id`,`role_id`),
  KEY `FK2F1516E4E577B4C` (`subject_id`),
  KEY `FK2F1516E4A905D628` (`role_id`),
  CONSTRAINT `FK2F1516E4A905D628` FOREIGN KEY (`role_id`) REFERENCES `im_role` (`id`),
  CONSTRAINT `FK2F1516E4E577B4C` FOREIGN KEY (`subject_id`) REFERENCES `im_subject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_subject_role: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_subject_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_subject_role` ENABLE KEYS */;


-- Dumping structure for table imperium.im_user
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE IF NOT EXISTS `im_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table imperium.im_user: ~0 rows (approximately)
/*!40000 ALTER TABLE `im_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `im_user` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
