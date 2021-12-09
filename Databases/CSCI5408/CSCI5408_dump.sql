CREATE DATABASE CSCI5408;

DROP TABLE IF EXISTS `assignments`;
CREATE TABLE `assignments` (
	`_id` Integer,
 	`aId` INT,
 	`description` TEXT NOT NULL,
 	`grade` TEXT,
 	PRIMARY KEY (aId)
);

INSERT INTO `assignments` VALUES ('1','1','dwma','A'), ('2','2','asdc','B'), ('3','3','cs','A');

DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
	`_id` Integer,
 	`aId` INT,
 	`name` TEXT NOT NULL,
 	`sId` INT,
 	PRIMARY KEY (sId)
	FOREIGN KEY (aId) REFERENCES assignments(aId)
);

INSERT INTO `students` VALUES ('1','1','devarshi','1'), ('2','1','Zhaoling','2'), ('3','2','Rajath','3'), ('4','2','Abhishek','4'), ('5','3','Vivek','5');

