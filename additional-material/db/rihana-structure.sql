--
-- Table structure for table `action`
--
CREATE TABLE `action` (
    `id`            int(11)    NOT NULL AUTO_INCREMENT,
    `name`          varchar(255)    NOT NULL,
    `description`   varchar(255)    NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `functionality`
--
CREATE TABLE `functionality` (
    `id`            int(11)    NOT NULL AUTO_INCREMENT,
    `name`          varchar(255)    NOT NULL,
    `description`   varchar(255)    NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `role`
--
CREATE TABLE `role` (
    `id`            int(11)    NOT NULL AUTO_INCREMENT,
    `name`          varchar(255)    NOT NULL,
    `description`   varchar(255)    NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `User`
--
CREATE TABLE `user` (
    `login`         varchar(100)    NOT NULL,
    `password`      varchar(32)     NOT NULL,
    `role_id`       int(11)    NOT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`login`),
    CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Functionality action`
--
CREATE TABLE `functionalityaction` (
    `functionality_id`  int(11)    NOT NULL,
    `action_id`         int(11)    NOT NULL,
    `creation_date`     datetime(3)     DEFAULT NULL,
    `update_date`       datetime(3)     DEFAULT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`functionality_id`, `action_id`),
    CONSTRAINT FOREIGN KEY (`functionality_id`) REFERENCES `functionality` (`id`),
    CONSTRAINT FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Permission`
--
CREATE TABLE `permission` (
    `role_id`                   int(11)    NOT NULL,
    `functionality_id`          int(11)    NOT NULL,
    `action_id`                 int(11)    NOT NULL,
    `creation_date`             datetime(3)     DEFAULT NULL,
    `update_date`               datetime(3)     DEFAULT NULL,
    `deleted`                   bit(1)          DEFAULT b'0',
    `delete_date`               datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`role_id`, `functionality_id`, `action_id`),
    CONSTRAINT FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT FOREIGN KEY (`functionality_id`) REFERENCES `functionalityaction` (`functionality_id`),
    CONSTRAINT FOREIGN KEY (`action_id`) REFERENCES `functionalityaction` (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Patient`
--
CREATE TABLE `patient` (
    `id`            varchar(255)    NOT NULL,
    `birthdate`     datetime(0)     DEFAULT NULL,
    `patientID`     varchar(255)    NOT NULL,
    `sex`           varchar(255)    DEFAULT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`patientID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Exploration`
--
CREATE TABLE `exploration` (
    `id`            varchar(255)    NOT NULL,
    `title`         varchar(255)    NOT NULL,
    `date`          datetime        NOT NULL,
    `source`        varchar(255)    DEFAULT NULL,
    `user_login`    varchar(255)    NOT NULL,
    `patient_id`    varchar(255)    NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `update_date`   datetime(3)     DEFAULT NULL,
    `deleted`       bit(1)          DEFAULT b'0',
    `delete_date`   datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`title`),
    CONSTRAINT FOREIGN KEY (`user_login`) REFERENCES `user` (`login`),
    CONSTRAINT FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Report`
--
CREATE TABLE `report` (
    `id`                varchar(255)    NOT NULL,
    `reportN`           varchar(255)    NOT NULL,
    `completion_date`   datetime(0)     DEFAULT NULL,
    `applicant`         varchar(255)    NOT NULL,
    `priority`          varchar(255)    NOT NULL,
    `status`            varchar(255)    NOT NULL,
    `bed`               varchar(255)    NOT NULL,
    `clinical_data`     MEDIUMTEXT      NOT NULL,
    `findings`          MEDIUMTEXT      NOT NULL,
    `conclusions`       MEDIUMTEXT      NOT NULL,
    `exploration_id`    varchar(255)    NOT NULL,
    `creation_date`     datetime(3)     DEFAULT NULL,
    `update_date`       datetime(3)     DEFAULT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`exploration_id`) REFERENCES `exploration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Exploration code`
--
CREATE TABLE `explorationcode` (
    `code`              varchar(255)    NOT NULL,
    `description`       varchar(255)    NOT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Requested exploration`
--
CREATE TABLE `requestedexploration` (
    `id`                varchar(255)    NOT NULL,
    `date`              datetime(0)     DEFAULT NULL,
    `report_id`         varchar(255)    NOT NULL,
    `exploration_code`  varchar(255)    NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`report_id`) REFERENCES `report` (`id`),
    CONSTRAINT FOREIGN KEY (`exploration_code`) REFERENCES `explorationcode` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Performed exploration`
--
CREATE TABLE `performedexploration` (
    `id`                varchar(255)    NOT NULL,
    `date`              datetime(0)     DEFAULT NULL,
    `portable`          varchar(255)    NOT NULL,
    `surgery`           varchar(255)    NOT NULL,
    `report_id`         varchar(255)    NOT NULL,
    `exploration_code`  varchar(255)    NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`report_id`) REFERENCES `report` (`id`),
    CONSTRAINT FOREIGN KEY (`exploration_code`) REFERENCES `explorationcode` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Radiograph`
--
CREATE TABLE `radiograph` (
    `id`                varchar(255)    NOT NULL,
    `source`            varchar(255)    NOT NULL,
    `type`              varchar(255)    NOT NULL,
    `observations`      varchar(3000)   DEFAULT NULL,
    `exploration_id`    varchar(255)    NOT NULL,
    `creation_date`     datetime(3)     DEFAULT NULL,
    `update_date`       datetime(3)     DEFAULT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`exploration_id`) REFERENCES `exploration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Sign type`
--
CREATE TABLE `signtype` (
    `code`              varchar(255)    NOT NULL,
    `name`              varchar(255)    NOT NULL,
    `description`       varchar(255)    NOT NULL,
    `target`            int             NOT NULL,
    `primaryColor`      varchar(8)      NOT NULL,
    `secondaryColor`    varchar(8)      NOT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Sign`
--
CREATE TABLE `sign` (
    `id`                varchar(255)    NOT NULL,
    `signtype_code`     varchar(255)    NOT NULL,
    `radiograph_id`     varchar(255)    NOT NULL,
    `brightness`        int             DEFAULT NULL,
    `contrast`          int             DEFAULT NULL,
    `creation_date`     datetime(3)     DEFAULT NULL,
    `update_date`       datetime(3)     DEFAULT NULL,
    `deleted`           bit(1)          DEFAULT b'0',
    `delete_date`       datetime(3)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`signtype_code`) REFERENCES `signtype` (`code`),
    CONSTRAINT FOREIGN KEY (`radiograph_id`) REFERENCES `radiograph` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Sign location`
--
CREATE TABLE `signlocation` (
    `id`            varchar(255)    NOT NULL,
    `x`             int             NOT NULL,
    `y`             int             NOT NULL,
    `width`         int             NOT NULL,
    `height`        int             NOT NULL,
    `creation_date` datetime(3)     DEFAULT NULL,
    `sign_id`       varchar(255)    NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`sign_id`) REFERENCES `sign` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;