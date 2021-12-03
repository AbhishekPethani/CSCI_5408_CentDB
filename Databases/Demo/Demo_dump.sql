DROP TABLE IF EXISTS `table1`;
CREATE TABLE `table1` (
	`_id` Integer,
 	`age` Integer,
 	`name` String,
 	`sex` String,
 	`student_id` String,
 	PRIMARY KEY (student_id)
);
INSERT INTO `table1` VALUES ('1','24','Zhaoling Sun','female','B00871417');
DROP TABLE IF EXISTS `table2`;
CREATE TABLE `table2` (
	`_id` Integer,
 	`course_id` int(10),
 	`student_id` String,
 	`course_name` varchar(100),
 	PRIMARY KEY (course_id),
	FOREIGN KEY (student_id) REFERENCES table2(student_id)
);
INSERT INTO `table2` VALUES ('1','csci_5100','1','CS'), ('2','csci_5408','1','DMWA'), ('3','csci_5408','3','DMWA');
