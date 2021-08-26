SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `user`;
DELETE FROM `patient`;
DELETE FROM `exploration`;
DELETE FROM `report`;
DELETE FROM `requestedexploration`;
DELETE FROM `performedexploration`;
DELETE FROM `explorationcode`;
DELETE FROM `radiography`;
DELETE FROM `signtype`;
DELETE FROM `sign`;
DELETE FROM `signlocation`;

SET FOREIGN_KEY_CHECKS = 1;
