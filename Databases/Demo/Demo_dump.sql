CREATE DATABASE Demo;

DROP TABLE IF EXISTS `courseInfo`;
CREATE TABLE `courseInfo` (
	`_id` Integer,
 	`courseId` Integer NOT NULL UNIQUE,
 	`name` String NOT NULL,
 	PRIMARY KEY (courseId)
);

INSERT INTO `courseInfo` VALUES ('1','5408','DMWA'), ('2','5308','ADC');

DROP TABLE IF EXISTS `professorInfo`;
CREATE TABLE `professorInfo` (
	`_id` Integer,
 	`professorId` Integer NOT NULL UNIQUE,
 	`name` String NOT NULL,
 	`courseId` Integer NOT NULL UNIQUE,
 	PRIMARY KEY (professorId),
	FOREIGN KEY (courseId) REFERENCES courseInfo(courseId)
);

INSERT INTO `professorInfo` VALUES ('1','5408','zhaoling','B00871417');

DROP TABLE IF EXISTS `studentInfo`;
CREATE TABLE `studentInfo` (
	`_id` Integer,
 	`studentId` Integer NOT NULL UNIQUE,
 	`name` String NOT NULL,
 	`courseId` Integer NOT NULL,
 	PRIMARY KEY (studentId),
	FOREIGN KEY (courseId) REFERENCES courseInfo(courseId)
);

INSERT INTO `studentInfo` VALUES ('1','B00999999','Julie','5308');

