--
-- Table structure for table `user`
--
CREATE TABLE `user` (
    `login`         varchar(100) NOT NULL,
    `password`      varchar(32)  NOT NULL,
    `role`          varchar(255) NOT NULL,
    PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
