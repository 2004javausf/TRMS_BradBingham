CREATE USER trms
IDENTIFIED BY p4ssw0rd
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT connect to trms;
GRANT resource to trms;
GRANT create session TO trms;
GRANT create table TO trms;
GRANT create view TO trms;

conn trms/p4ssw0rd
--------------------------------------------------
CREATE TABLE REIMBURSE_FORM(
id int PRIMARY KEY,
contents VARCHAR2(255)
);
INSERT INTO REIMBURSE_FORM VALUES(1,'This is the first form');
INSERT INTO REIMBURSE_FORM VALUES(2,'This is the second form');
INSERT INTO REIMBURSE_FORM VALUES(3,'This is the third form');
SELECT * FROM REIMBURSE_FORM;