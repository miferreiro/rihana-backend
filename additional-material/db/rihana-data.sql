--
-- Data for table `User`
--
INSERT INTO `user` (`login`, `password`, `role`, `deleted`, `delete_date`)
VALUES
    ('admin', '25e4ee4e9229397b6b17776bfceaf8e7', 'ADMIN', 0, null),
    ('radiologist', 'b30eee257b8e9f9500a17b32e65f810d', 'RADIOLOGIST', 0, null),
    ('senior', 'c1a1738648ecda410dc3a0dbbb3be683', 'SENIORRADIOLOGIST', 0, null),
    ('user', '63e780c3f321d13109c71bf81805476e', 'USER', 0, null);

--
-- Data for table `Patient`
--
INSERT INTO `patient` (`id`, `birthdate`, `patientID`, `sex`, `creation_date`, `update_date`, `deleted`, `delete_date`)
VALUES
    ('1', '1962-11-15 23:00:00', '356a192b7913b04c54574d18c28d46e6395428ab', 'FEMALE', '2019-01-22 12:20:09.000', '2020-02-24 17:22:06.000', 0, null),
    ('2', '1948-01-02 23:00:00', 'da4b9237bacccdf19c0760cab7aec4a8359010b0', 'MALE', '2019-10-18 10:38:13.000', '2020-02-24 17:22:01.000', 0, null),
    ('3', '1950-02-16 23:00:00', '77de68daecd823babbb58edb1c8e14d7106e83bb', 'FEMALE', '2018-12-14 13:03:16.000', '2020-02-24 17:22:10.000', 0, null),
    ('4', '1946-08-15 22:00:00', '1b6453892473a467d07372d45eb05abc2031647a', 'MALE', '2019-01-29 12:12:55.000', '2020-02-24 17:22:02.000', 0, null),
    ('5', '1950-03-25 23:00:00', 'ac3478d69a3c81fa62e60f5c3696165a4e5e6ac4', 'MALE', '2018-11-23 14:28:54.000', '2020-02-24 17:22:13.000', 0, null);

--
-- Data for table `Exploration`
--
INSERT INTO `exploration` (`id`, `title`, `date`, `user_login`, `patient_id`, `creation_date`, `update_date`, `deleted`, `delete_date`)
VALUES
    ('1', 'Exploration 1', '2018-12-12 23:00:00', 'radiologist', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('2', 'Exploration 2', '2019-09-24 22:00:00', 'radiologist', '2', '2019-09-24 22:00:00', '2020-02-24 17:21:58', 0, null),
    ('3', 'Exploration 3', '2019-02-19 23:00:00', 'radiologist', '3', '2019-02-19 23:00:00', '2020-02-24 17:22:00', 0, null),
    ('4', 'Exploration 4', '2019-09-09 22:00:00', 'radiologist', '4', '2019-09-09 22:00:00', '2020-02-24 17:22:01', 0, null),
    ('5', 'Exploration 5', '2019-01-27 23:00:00', 'radiologist', '5', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('6', 'Exploration 6', '2019-01-27 23:00:00', 'radiologist', '2', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('7', 'Exploration 7', '2019-12-27 23:22:12', 'radiologist', '3', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('8', 'Exploration 8', '2019-11-27 23:00:00', 'radiologist', '5', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('9', 'Exploration 9', '2019-01-7 23:00:00', 'radiologist', '1', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('10', 'Exploration 10', '2019-02-27 10:00:00', 'radiologist', '1', '2019-02-27 10:00:00', '2019-02-27 10:00:00', 0, null),
    ('11', 'Exploration 11', '2020-01-27 23:00:00', 'radiologist', '2', '2020-01-27 23:00:00', '2020-01-27 23:00:00', 0, null),
    ('12', 'Exploration 12', '2021-01-27 00:00:00', 'radiologist', '3', '2021-01-27 00:00:00', '2021-01-27 00:00:00', 0, null);

--
-- Data for table `Report`
--
INSERT INTO `report` (`id`, `reportN`, `completion_date`, `applicant`, `priority`, `status`, `bed`, `clinical_data`, `findings`, `conclusions`, `exploration_id`, `creation_date`, `update_date`, `deleted`, `delete_date`)
VALUES
    ('1', '1', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 1', 'Aparente discreta mejoría radiológica.', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('2', '2', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 2', 'Aparente discreta mejoría radiológica.', '2', '2019-09-24 22:00:00', '2020-02-24 17:21:58', 0, null),
    ('3', '3', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 3', 'Aparente discreta mejoría radiológica.', '3', '2019-02-19 23:00:00', '2020-02-24 17:22:00', 0, null),
    ('4', '4', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 4', 'Aparente discreta mejoría radiológica.', '4', '2019-09-09 22:00:00', '2020-02-24 17:22:01', 0, null),
    ('5', '5', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 5', 'Aparente discreta mejoría radiológica.', '5', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('6', '56', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 6', 'Aparente discreta mejoría radiológica.', '6','2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('7', '7', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 7', 'Aparente discreta mejoría radiológica.', '7', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('8', '8', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 8', 'Aparente discreta mejoría radiológica.', '8', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('9', '9', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 9', 'Aparente discreta mejoría radiológica.', '9', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('10', '10', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 10', 'Aparente discreta mejoría radiológica.', '10', '2019-02-27 10:00:00', '2019-02-27 10:00:00', 0, null),
    ('11', '11', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 11', 'Aparente discreta mejoría radiológica.', '11', '2020-01-27 23:00:00', '2020-01-27 23:00:00', 0, null),
    ('12', '12', '2018-12-12', '- / ONCN - ONCOLOXIA MEDICA HOSP.', 'Ordinaria', 'Ingresado', 'H107B', 'Control radiológico de infección respiratoria en paciente con Ca pulmón IV / Rx tórax', 'Ejemplo 12', 'Aparente discreta mejoría radiológica.', '12', '2021-01-27 00:00:00', '2021-01-27 00:00:00', 0, null);

--
-- Data for table `Exploration code`
--
INSERT INTO `explorationcode` (`code`, `description`, `deleted`, `delete_date`)
VALUES
    ('70102', 'TORAX, PA Y LAT', 0, null);

--
-- Data for table `Requested exploration`
--
INSERT INTO `requestedexploration` (`id`, `date`, `report_id`, `exploration_code`)
VALUES
    ('1', '2018-12-12', '1', '70102'),
    ('2', '2018-12-12', '2', '70102'),
    ('3', '2018-12-12', '3', '70102'),
    ('4', '2018-12-12', '4', '70102'),
    ('5', '2018-12-12', '5', '70102'),
    ('6', '2018-12-12', '6', '70102'),
    ('7', '2018-12-12', '7', '70102'),
    ('8', '2018-12-12', '8', '70102'),
    ('9', '2018-12-12', '9', '70102'),
    ('10', '2018-12-12', '10', '70102'),
    ('11', '2018-12-12', '11', '70102'),
    ('12', '2018-12-12', '12', '70102');

--
-- Data for table `Performed exploration`
--
INSERT INTO `performedexploration` (`id`, `date`, `portable`, `surgery`, `report_id`, `exploration_code`)
VALUES
    ('1', '2018-12-12', 'N', 'N', '1', '70102'),
    ('2', '2018-12-12', 'N', 'N', '2', '70102'),
    ('3', '2018-12-12', 'N', 'N', '3', '70102'),
    ('4', '2018-12-12', 'N', 'N', '4', '70102'),
    ('5', '2018-12-12', 'N', 'N', '5', '70102'),
    ('6', '2018-12-12', 'N', 'N', '6', '70102'),
    ('7', '2018-12-12', 'N', 'N', '7', '70102'),
    ('8', '2018-12-12', 'N', 'N', '8', '70102'),
    ('9', '2018-12-12', 'N', 'N', '9', '70102'),
    ('10', '2018-12-12', 'N', 'N', '10', '70102'),
    ('11', '2018-12-12', 'N', 'N', '11', '70102'),
    ('12', '2018-12-12', 'N', 'N', '12', '70102');

--
-- Data for table `Radiograph`
--
INSERT INTO `radiograph` (`id`, `source`, `type`, `observations`, `exploration_id`, `creation_date`, `update_date`, `deleted`, `delete_date`)
VALUES
    ('1', 'source1', 'PA', 'Example of observations', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('2', 'source2', 'LAT', 'Example of observations', '1', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('3', 'source3', 'PA', 'Example of observations', '2', '2019-09-24 22:00:00', '2020-02-24 17:21:58', 0, null),
    ('4', 'source4', 'LAT', 'Example of observations', '2', '2019-09-24 22:00:00', '2020-02-24 17:21:58', 0, null),
    ('5', 'source5', 'AP', 'Example of observations', '3', '2019-02-19 23:00:00', '2020-02-24 17:22:00', 0, null),
    ('6', 'source6', 'PA', 'Example of observations', '4', '2019-09-09 22:00:00', '2020-02-24 17:22:01', 0, null),
    ('7', 'source7', 'LAT', 'Example of observations', '4', '2019-09-09 22:00:00', '2020-02-24 17:22:01', 0, null),
    ('8', 'source8', 'AP', 'Example of observations', '5', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('9', 'source9', 'AP', 'Example of observations', '6', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('10', 'source10', 'AP', 'Example of observations', '7', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('11', 'source11', 'AP', 'Example of observations', '8', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('12', 'source12', 'AP', 'Example of observations', '9', '2019-01-27 23:00:00', '2020-02-24 17:22:02', 0, null),
    ('13', 'source13', 'AP', 'Example of observations', '10', '2019-02-27 10:00:00', '2019-02-27 10:00:00', 0, null),
    ('14', 'source14', 'AP', 'Example of observations', '11', '2020-01-27 23:00:00', '2020-01-27 23:00:00', 0, null),
    ('15', 'source15', 'PA', 'Example of observations', '12', '2021-01-27 00:00:00', '2021-01-27 00:00:00', 0, null),
    ('16', 'source16', 'AP', 'Example of observations', '12', '2021-01-27 00:00:00', '2021-01-27 00:00:00', 0, null);

--
-- Data for table `Sign type`
--
INSERT INTO `signtype` (`code`, `name`, `description`, `target`, `primaryColor`, `secondaryColor`, `deleted`, `delete_date`)
VALUES
    ('CAR', 'Cardiomegaly', 'Cardiomegaly', '1500', '#E6B8B8', '#990F0B', 0, null),
    ('CON', 'Condensation', 'Condensation', '1500', '#A4FFAF', '#0E5102', 0, null),
    ('MAS', 'Masses', 'Masses', '1500', '#B5FCFF', '#024045', 0, null),
    ('NOD', 'Nodules', 'Nodules', '1500', '#70ACFF', '#091365', 0, null),
    ('PLE', 'Pleural effusion', 'Pleural effusion', '1500', '#F6A9FF', '#5609A9', 0, null),
    ('PNE', 'Pneumothorax', 'Pneumothorax', '1500', '#A1887F', '#4E342E', 0, null),
    ('RED', 'Redistribution', 'Redistribution', '1500', '#FFC37D', '#843E04', 0, null),
    ('NOF', 'No findings', 'No findings', '1500', '#000000', '#FFFFFF', 0, null),
    ('NON', 'Non-normal', 'Non-normal findings', '1500', '#D0BDF6', '#6F44C7', 0, null);

--
-- Data for table `sign`
--
INSERT INTO `sign` (`id`, `signtype_code`, `radiograph_id`, `brightness`, `contrast`, `creation_date`, `update_date`, `deleted`, `delete_date`)
VALUES
    ('1', 'CAR', '1', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('2', 'MAS', '1', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('3', 'CON', '1', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('4', 'MAS', '2', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('5', 'NOD', '2', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('6', 'PLE', '2', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('7', 'NOD', '3', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('8', 'CAR', '3', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('9', 'MAS', '3', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('10', 'PLE', '4', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('11', 'RED', '4', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('12', 'RED', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('13', 'PNE', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('14', 'CON', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('15', 'PNE', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('16', 'MAS', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('17', 'CON', '5', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('18', 'CON', '6', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('19', 'PNE', '7', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('20', 'NOD', '7', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('21', 'RED', '8', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('22', 'PLE', '8', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('23', 'CAR', '9', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('24', 'PNE', '9', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('25', 'MAS', '9', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('26', 'NOD', '9', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('27', 'NOD', '10', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('28', 'PLE', '10', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('29', 'NON', '11', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('30', 'CAR', '12', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('31', 'PLE', '13', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('32', 'PLE', '14', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('33', 'NOD', '14', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('34', 'CAR', '14', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('35', 'RED', '15', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('36', 'CON', '15', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null),
    ('37', 'NOF', '16', '100', '100', '2018-12-12 23:00:00', '2020-02-24 17:21:58', 0, null);

--
-- Data for table `Sign location`
--
INSERT INTO `signlocation` (`id`, `x`, `y`, `width`, `height`, `creation_date`, `sign_id`)
VALUES
    ('1', '1', '10', '20', '20', '2020-02-24 17:21:58', '1'),
    ('2', '2', '10', '20', '20', '2020-02-24 17:21:58', '2'),
    ('3', '3', '10', '20', '20', '2020-02-24 17:21:58', '3'),
    ('4', '4', '10', '20', '20', '2020-02-24 17:21:58', '4'),
    ('5', '5', '10', '20', '20', '2020-02-24 17:21:58', '5'),
    ('6', '5', '10', '20', '20', '2020-02-24 17:21:58', '6'),
    ('7', '5', '10', '20', '20', '2020-02-24 17:21:58', '7'),
    ('8', '5', '10', '20', '20', '2020-02-24 17:21:58', '8'),
    ('9', '5', '10', '20', '20', '2020-02-24 17:21:58', '9'),
    ('10', '5', '10', '20', '20', '2020-02-24 17:21:58', '10'),
    ('11', '5', '10', '20', '20', '2020-02-24 17:21:58', '11'),
    ('12', '5', '10', '20', '20', '2020-02-24 17:21:58', '12'),
    ('13', '5', '10', '20', '20', '2020-02-24 17:21:58', '13'),
    ('14', '5', '10', '20', '20', '2020-02-24 17:21:58', '14'),
    ('15', '5', '10', '20', '20', '2020-02-24 17:21:58', '15'),
    ('16', '5', '10', '20', '20', '2020-02-24 17:21:58', '16'),
    ('17', '5', '10', '20', '20', '2020-02-24 17:21:58', '17'),
    ('18', '5', '10', '20', '20', '2020-02-24 17:21:58', '18'),
    ('19', '5', '10', '20', '20', '2020-02-24 17:21:58', '19'),
    ('20', '5', '10', '20', '20', '2020-02-24 17:21:58', '20'),
    ('21', '5', '10', '20', '20', '2020-02-24 17:21:58', '21'),
    ('22', '5', '10', '20', '20', '2020-02-24 17:21:58', '22'),
    ('23', '5', '10', '20', '20', '2020-02-24 17:21:58', '23'),
    ('24', '5', '10', '20', '20', '2020-02-24 17:21:58', '24'),
    ('25', '5', '10', '20', '20', '2020-02-24 17:21:58', '25'),
    ('26', '5', '10', '20', '20', '2020-02-24 17:21:58', '26'),
    ('27', '5', '10', '20', '20', '2020-02-24 17:21:58', '27'),
    ('28', '5', '10', '20', '20', '2020-02-24 17:21:58', '28'),
    ('29', '5', '10', '20', '20', '2020-02-24 17:21:58', '30'),
    ('30', '5', '10', '20', '20', '2020-02-24 17:21:58', '31'),
    ('31', '5', '10', '20', '20', '2020-02-24 17:21:58', '32'),
    ('32', '5', '10', '20', '20', '2020-02-24 17:21:58', '33'),
    ('33', '5', '10', '20', '20', '2020-02-24 17:21:58', '34'),
    ('34', '5', '10', '20', '20', '2020-02-24 17:21:58', '35'),
    ('35', '5', '10', '20', '20', '2020-02-24 17:21:58', '36');