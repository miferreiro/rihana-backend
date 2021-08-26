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
-- Data for table `Exploration`
--
INSERT INTO `exploration` (`id`, `title`, `date`, `user_login`, `patient_id`, `creation_date`, `update_date`)
VALUES
    ('1', 'Exploration 1', '2018-12-12 23:00:00', 'radiologist', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('2', 'Exploration 2', '2019-09-24 22:00:00', 'radiologist', '2', '2019-09-24 22:00:00', '2020-02-24 17:21:58.000'),
    ('3', 'Exploration 3', '2019-02-19 23:00:00', 'radiologist', '3', '2019-02-19 23:00:00', '2020-02-24 17:22:00.000'),
    ('4', 'Exploration 4', '2019-09-09 22:00:00', 'radiologist', '4', '2019-09-09 22:00:00', '2020-02-24 17:22:01.000'),
    ('5', 'Exploration 5', '2019-01-27 23:00:00', 'radiologist', '5', '2019-01-27 23:00:00', '2020-02-24 17:22:02.000');

--
-- Data for table `Report`
--
INSERT INTO `report` (`id`, `reportN`, `completion_date`, `applicant`, `priority`, `status`, `bed`, `clinical_data`, `findings`, `conclusions`, `exploration_id`, `creation_date`, `update_date`)
VALUES
    ('1', '1', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 1', 'Aparente discreta mejoría radiológica.', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('2', '2', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 2', 'Aparente discreta mejoría radiológica.', '2', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('3', '3', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 3', 'Aparente discreta mejoría radiológica.', '3', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('4', '4', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 4', 'Aparente discreta mejoría radiológica.', '4', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('5', '5', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 5', 'Aparente discreta mejoría radiológica.', '5', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000');

--
-- Data for table `Exploration code`
--
INSERT INTO `explorationcode` (`code`, `description`)
VALUES
    ('70102', 'TORAX, PA Y LAT');

--
-- Data for table `Requested exploration`
--
INSERT INTO `requestedexploration` (`id`, `date`, `report_id`, `exploration_code`)
VALUES
    ('1', '2018-12-12', '1', '70102'),
    ('2', '2018-12-12', '2', '70102'),
    ('3', '2018-12-12', '3', '70102'),
    ('4', '2018-12-12', '4', '70102'),
    ('5', '2018-12-12', '5', '70102');

--
-- Data for table `Performed exploration`
--
INSERT INTO `performedexploration` (`id`, `date`, `portable`, `surgery`, `report_id`, `exploration_code`)
VALUES
    ('1', '2018-12-12', 'N', 'N', '1', '70102'),
    ('2', '2018-12-12', 'N', 'N', '2', '70102'),
    ('3', '2018-12-12', 'N', 'N', '3', '70102'),
    ('4', '2018-12-12', 'N', 'N', '4', '70102'),
    ('5', '2018-12-12', 'N', 'N', '5', '70102');

--
-- Data for table `Radiography`
--
INSERT INTO `radiography` (`id`, `source`, `type`, `exploration_id`, `creation_date`, `update_date`)
VALUES
    ('1', 'source1', 'PA', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('2', 'source2', 'LAT', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('3', 'source3', 'PA', '2', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('4', 'source4', 'LAT', '2', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('5', 'source5', 'AP', '3', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('6', 'source6', 'PA', '4', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('7', 'source7', 'LAT', '4', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('8', 'source8', 'AP', '5', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000');

--
-- Data for table `Sign type`
--
INSERT INTO `signtype` (`code`, `name`, `description`)
VALUES
    ('CAR', 'Cardiomegaly', 'Cardiomegaly'),
    ('CON', 'Condensation', 'Condensation'),
    ('MAS', 'Masses', 'Masses'),
    ('NOD', 'Nodules', 'Nodules'),
    ('PLE', 'Pleural effusion', 'Pleural effusion'),
    ('PNE', 'Pneumothorax', 'Pneumothorax'),
    ('RED', 'Redistribution', 'Redistribution');

--
-- Data for table `sign`
--
INSERT INTO `sign` (`id`, `signtype_code`, `radiography_id`, `creation_date`, `update_date`)
VALUES
    ('1', 'CAR', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('2', 'MAS', '2', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('3', 'NOD', '3', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('4', 'PLE', '4', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000'),
    ('5', 'RED', '5', '2018-12-12 23:00:00', '2020-02-24 17:21:58.000');

--
-- Data for table `Sign location`
--
INSERT INTO `signlocation` (`id`, `x`, `y`, `width`, `height`, `brightness`, `contrast`, `creation_date`, `sign_id`)
VALUES
    ('1', '1', '10', '20', '20', '100', '100', '2020-02-24 17:21:58.000', '1'),
    ('2', '2', '10', '20', '20', '100', '100', '2020-02-24 17:21:58.000', '2'),
    ('3', '3', '10', '20', '20', '100', '100', '2020-02-24 17:21:58.000', '3'),
    ('4', '4', '10', '20', '20', '100', '100', '2020-02-24 17:21:58.000', '4'),
    ('5', '5', '10', '20', '20', '100', '100', '2020-02-24 17:21:58.000', '5');
