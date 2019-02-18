-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `User` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXIST `User`(
  `userID` VARCHAR(64) NOT NULL PRIMARY KEY, 
  `accountID` VARCHAR(64) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `firstNames` VARCHAR(128) NOT NULL,
  `lastNames` VARCHAR(64) NOT NULL,
  `dateOfBirth` DATE NULL,
  `address` VARCHAR(45) NOT NULL,
  `email` VARCHAR(64) NOT NULL,
  `phoneNumber` VARCHAR(16) NULL, 
  `mobile1` VARCHAR(16) NULL, 
  `mobile2` VARCHAR(16) NULL);
SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Session` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Session` (
  `sessionID` VARCHAR(64) NOT NULL PRIMARY KEY,
  `userID` VARCHAR(64) NOT NULL,
  `timeIn` TIMESTAMP(10) NOT NULL,
  `timeOut` TIMESTAMP(10) NULL,
  `device` VARCHAR(64) NULL);

SHOW WARNING;

-- -----------------------------------------------------
-- Table `SessionLog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SessionLog` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `SessionLog` (
  `userID` VARCHAR(64) NOT NULL,
  `start` TIMESTAMP(10) NOT NULL,
  `end` TIMESTAMP(10) NOT NULL,
  `device` VARCHAR(64) NULL,
  PRIMARY KEY(`userID`, `start`, `end`));

SHOW WARNINGS;





-- -----------------------------------------------------
-- Table `SchoolAccount`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `SchoolAccount` ;

--SHOW WARNINGS;
--CREATE TABLE IF NOT EXISTS `SchoolAccount` (
--  `idSchoolAccount` INT NOT NULL,
--  `InstitutionName` VARCHAR(45) NULL,
--  `DateJoined` INT NULL,
--  PRIMARY KEY (`idSchoolAccount`))
--
--SHOW WARNINGS;
--

-- -----------------------------------------------------
-- Table `TeacherAccount`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `TeacherAccount` ;
--
--SHOW WARNINGS;
--CREATE TABLE IF NOT EXISTS `TeacherAccount` (
--  `idTeacherAccount` INT NOT NULL,
--  `DateJoined` INT NULL,
--  PRIMARY KEY (`idTeacherAccount`))
--
--SHOW WARNINGS;
--

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
