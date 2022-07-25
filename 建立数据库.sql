#创建数据库
CREATE DATABASE IF NOT EXISTS db_bank;

#创建表
CREATE TABLE IF NOT EXISTS `db_bank`.`t_user` (
  `userId` INT (10) NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR (10),
  `password` VARCHAR (20),
  `personId` VARCHAR (12) UNIQUE,
  `phoneNum` VARCHAR (11) UNIQUE,
  `sex` VARCHAR (1),
  `birthDate` DATE NULL DEFAULT NULL,
  `balance` DOUBLE,
  PRIMARY KEY (`userId`)
);

#插入管理员信息
INSERT INTO `db_bank`.`t_user` (`userName`, `password`, `personId`, `phoneNum`, `sex`, `balance`) VALUES ('管理员', 'abcd1234', '000000000000', NULL, 'M', '2000'); 