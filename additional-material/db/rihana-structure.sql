--
-- Table structure for table `user`
--
CREATE TABLE `user` (
    `login`         varchar(100) NOT NULL,
    `password`      varchar(32)  NOT NULL,
    `role`          varchar(255) NOT NULL,
    PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `patient`
--
CREATE TABLE `patient` (
    `id`            varchar(255)    NOT NULL,
    `birthdate`     datetime(0)     DEFAULT NULL,
    `patientID`     varchar(255)    NOT NULL,
    `sex`           varchar(255)    NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;