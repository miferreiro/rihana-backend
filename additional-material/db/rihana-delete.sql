SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `action`;
DELETE FROM `functionality`;
DELETE FROM `role`;
DELETE FROM `user`;
DELETE FROM `functionalityaction`;
DELETE FROM `permission`;
DELETE FROM `patient`;
DELETE FROM `exploration`;
DELETE FROM `report`;
DELETE FROM `explorationcode`;
DELETE FROM `requestedexploration`;
DELETE FROM `performedexploration`;
DELETE FROM `radiograph`;
DELETE FROM `signtype`;
DELETE FROM `sign`;
DELETE FROM `signlocation`;

SET FOREIGN_KEY_CHECKS = 1;
