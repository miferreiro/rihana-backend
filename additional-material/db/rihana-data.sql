--
-- Data for table `User`
--
INSERT INTO `user` (`login`, `password`, `role`)
VALUES
	('admin', '25e4ee4e9229397b6b17776bfceaf8e7', 'ADMIN'),
	('radiologist', 'b30eee257b8e9f9500a17b32e65f810d', 'RADIOLOGIST'),
	('user', '63e780c3f321d13109c71bf81805476e', 'USER');

--
-- Data for table `Patient`
--
INSERT INTO `patient` (`id`, `birthdate`, `patientID`, `sex`, `creation_date`, `update_date`)
VALUES
	('1', '1962-11-15 23:00:00', '356a192b7913b04c54574d18c28d46e6395428ab', 'FEMALE', '2019-01-22 12:20:09.000', '2020-02-24 17:22:06.000'),
	('2', '1948-01-02 23:00:00', 'da4b9237bacccdf19c0760cab7aec4a8359010b0', 'MALE', '2019-10-18 10:38:13.000', '2020-02-24 17:22:01.000'),
	('3', '1950-02-16 23:00:00', '77de68daecd823babbb58edb1c8e14d7106e83bb', 'FEMALE', '2018-12-14 13:03:16.000', '2020-02-24 17:22:10.000'),
	('4', '1946-08-15 22:00:00', '1b6453892473a467d07372d45eb05abc2031647a', 'MALE', '2019-01-29 12:12:55.000', '2020-02-24 17:22:02.000'),
	('5', '1950-03-25 23:00:00', 'ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4', 'MALE', '2018-11-23 14:28:54.000', '2020-02-24 17:22:13.000');

--
-- Data for table `Exploration code`
--
INSERT INTO `explorationcode` (`code`, `description`)
VALUES
    ('70102', 'TORAX, PA Y LAT');
