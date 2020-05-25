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

conn trms/p4ssw0rd;
-------------------------------------------------
--TABLES
CREATE TABLE EMPLOYEES(
ID INT PRIMARY KEY,
FIRST_NAME VARCHAR2(30),
LAST_NAME VARCHAR2(30),
USERNAME VARCHAR2(20),
PASSWORD VARCHAR2(20),
AVAILABLE_AMOUNT NUMBER,
TITLE VARCHAR2(20),
DEPARTMENT VARCHAR2(20),
OFFICE_LOC VARCHAR2(20)
);
--EXAMPLE head,sales,region
--EXAMPLE sup,sales,east
--EXAMPLE asc,sales,east


CREATE TABLE R_FORMS(
ID INT PRIMARY KEY,
EMPLOYEE_ID NUMBER,
FORM_STATUS VARCHAR2(20),
APPROVE_SUPERVISOR VARCHAR2(20),
SUPERVISOR_SUBMIT_DATE TIMESTAMP,
APPROVE_HEAD VARCHAR2(20),
HEAD_SUBMIT_DATE TIMESTAMP,
APPROVE_COORDINATOR VARCHAR2(20),
COORDINATOR_SUBMIT_DATE TIMESTAMP,
ALTERED_FORM VARCHAR2(20),
REJECTION_JUSTIFY VARCHAR2(500),
SUBMIT_DATE TIMESTAMP,
START_DATE DATE,
START_TIME VARCHAR2(20),
EVENT_LOCATION VARCHAR2(50),
EVENT_COST NUMBER,
EVENT_DESCRIPTION VARCHAR2(500),
EVENT_JUSTIFY VARCHAR2(500),
GRADING_FORMAT_ID NUMBER,
EVENT_TYPE VARCHAR2(50),
OPT_ON_SUBMIT VARCHAR2(50),
ON_FINISH_GRADE NUMBER,
APPROVE_GRADE VARCHAR2(20),
ON_FINISH_PRESENTATION VARCHAR2(50),
APPROVE_PRESENTATION VARCHAR2(20)
);

CREATE TABLE MESSAGES(
ID INT PRIMARY KEY,
SUBMIT_DATE TIMESTAMP,
SENDER_EMPLOYEE_ID NUMBER,
RECIPIANT_EMPLOYEE_ID NUMBER,
R_FORM_ID NUMBER,
MESSAGE VARCHAR2(255)
);
-------------------------------------
--REF TABLES
CREATE TABLE GRADING_FORMAT(
ID INT PRIMARY KEY,
FORMAT_TYPE VARCHAR2(20)
);
INSERT INTO GRADING_FORMAT VALUES(1,'Pass/Fail');
INSERT INTO GRADING_FORMAT VALUES(2,'>80%');
INSERT INTO GRADING_FORMAT VALUES(3,'>70%');
INSERT INTO GRADING_FORMAT VALUES(4,'Presentation');
INSERT INTO GRADING_FORMAT VALUES(5,'In description');
------------------------------------
--PROCEEDURES

CREATE OR REPLACE PROCEDURE INSERT_EMPLOYEE
(FIRST_NAME VARCHAR2,
LAST_NAME VARCHAR2,
USERNAME VARCHAR2,
PASS VARCHAR2,
AVAILABLE_AMOUNT NUMBER,
TITLE VARCHAR2,
DEPARTMENT VARCHAR2,
OFFICE_LOC VARCHAR2)
AS
BEGIN
    INSERT INTO EMPLOYEES VALUES(EMPLOYEE_SEQ.NEXTVAL,FIRST_NAME,LAST_NAME,USERNAME,PASS,AVAILABLE_AMOUNT,TITLE,DEPARTMENT,OFFICE_LOC);
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE INSERT_R_FORM
(EMPLOYEE_ID NUMBER,
START_DATE DATE,
START_TIME VARCHAR2,
EVENT_LOCATION VARCHAR2,
EVENT_COST NUMBER,
EVENT_DESCRIPTION VARCHAR2,
EVENT_JUSTIFY VARCHAR2,
GRADING_FORMAT_ID NUMBER,
EVENT_TYPE VARCHAR2,
OPT_ON_SUBMIT VARCHAR2)
AS
BEGIN
    INSERT INTO R_FORMS VALUES(
        R_FORM_SEQ.NEXTVAL,
        EMPLOYEE_ID,
        'In-review',
        'False',
        NULL,
        'False',
        NULL,
        'False',
        NULL,
        'False',
        NULL,
        CURRENT_TIMESTAMP(2),
        START_DATE,
        START_TIME,
        EVENT_LOCATION,
        EVENT_COST,
        EVENT_DESCRIPTION,
        EVENT_JUSTIFY,
        GRADING_FORMAT_ID,
        EVENT_TYPE,
        OPT_ON_SUBMIT,
        NULL,
        'False',
        NULL,
        'False'
    );
    COMMIT;
END;
/
CREATE OR REPLACE PROCEDURE INSERT_MESSAGE
(SENDER_EMPLOYEE_ID NUMBER,
RECIPIANT_EMPLOYEE_ID NUMBER,
R_FORM_ID NUMBER,
MESSAGE VARCHAR2)
AS
BEGIN
    INSERT INTO MESSAGES VALUES(
        MESSAGE_SEQ.NEXTVAL,
        CURRENT_TIMESTAMP,
        SENDER_EMPLOYEE_ID,
        RECIPIANT_EMPLOYEE_ID,
        R_FORM_ID,
        MESSAGE
    );
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE R_FORM_STATUS_CHANGE
(R_FORM_ID NUMBER,APPROVER VARCHAR2,NEW_STATUS VARCHAR2, REASON VARCHAR2)
AS
BEGIN
    IF APPROVER = 'SUPERVISOR' THEN
            UPDATE R_FORMS SET APPROVE_SUPERVISOR = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET SUPERVISOR_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    ELSIF APPROVER = 'HEAD' THEN
            UPDATE R_FORMS SET APPROVE_HEAD = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET HEAD_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    ELSIF APPROVER = 'COORDINATOR' THEN
            UPDATE R_FORMS SET APPROVE_COORDINATOR = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET COORDINATOR_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    END IF;
    IF NEW_STATUS = 'Denied' THEN
            UPDATE R_FORMS SET REJECTION_JUSTIFY = REASON WHERE R_FORMS.ID = R_FORM_ID;
    END IF;
    COMMIT;
END;
/
CREATE OR REPLACE PROCEDURE FINAL_STATUS_CHANGE
(R_FORM NUMBER, NEW_GRADE_STATUS VARCHAR2, NEW_PRESENTATION_STATUS VARCHAR2)
AS
BEGIN
    IF NEW_GRADE_STATUS != NULL THEN
            UPDATE R_FORMS SET APPROVE_GRADE = NEW_GRADE_STATUS WHERE R_FORMS.ID = R_FORM_ID;
    ELSE
            UPDATE R_FORMS SET APPROVE_PRESENTATION = NEW_PRESENTATION_STATUS WHERE R_FORMS.ID = R_FORM_ID;
    END IF;
    COMMIT;
END;
/
-------------------------------------------
--MOCK TABLE DATA
--FIRST_NAME VARCHAR2,LAST_NAME VARCHAR2,AVAILABLE_AMOUNT NUMBER,TITLE VARCHAR2,DEPARTMENT VARCHAR2,OFFICE_LOC VARCHAR2
TRUNCATE TABLE EMPLOYEES;
DROP SEQUENCE EMPLOYEE_SEQ;
CREATE SEQUENCE EMPLOYEE_SEQ
  INCREMENT BY 1 MAXVALUE 5000 CYCLE;
EXECUTE INSERT_EMPLOYEE('UserOne','LastNm','User1','Password1',1000,'Head','Management','Main');
EXECUTE INSERT_EMPLOYEE('UserTwo','LastNm','User2','Password2',900,'Supervisor','IT','Main');
EXECUTE INSERT_EMPLOYEE('UserThree','LastNm','User3','Password3',800,'Associate','IT','Main');
EXECUTE INSERT_EMPLOYEE('UserFour','LastNm','User4','Password4',700,'Associate','IT','Main');
EXECUTE INSERT_EMPLOYEE('UserFive','LastNm','User5','Password5',600,'Supervisor','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserSix','LastNm','User6','Password6',500,'Associate','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserSeven','LastNm','User7','Password7',400,'Associate','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserEight','LastNm','User8','Password8',300,'Associate','Sales','East');


--EMPLOYEE_ID NUMBER,START_DATE DATE,START_TIME VARCHAR2,EVENT_LOCATION VARCHAR2,EVENT_COST NUMBER,
--EVENT_DESCRIPTION VARCHAR2,EVENT_JUSTIFY VARCHAR2,GRADING_FORMAT_ID NUMBER,EVENT_TYPE VARCHAR2,OPT_ON_SUBMIT BLOB
TRUNCATE TABLE R_FORMS;
DROP SEQUENCE R_FORM_SEQ;
CREATE SEQUENCE R_FORM_SEQ
  INCREMENT BY 1 MAXVALUE 5000 CYCLE;
EXECUTE INSERT_R_FORM(1,'01-MAY-2021','1 AM','Leader School',1000,'School for leader','I will lead better',1,'University Course',NULL);
EXECUTE INSERT_R_FORM(2,'01-MAY-2021','2 AM','Supervisor Seminar',900,'Seminar for Supervisors','It will help',2,'Seminar',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','3 PM','Associate prep class',800,'Prep class for associates','It will help',3,'Certification Prep Class',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','4 PM','Associate Certification',600,'Certification for Associates','It will help',4,'Certification',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','5 PM','Associate Technical Training',500,'Technical Training for Associates','It will help',5,'Technical Training',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','6 PM','Associate therapy',400,'Therapy for Associates','It will help',5,'Other',NULL);

--SENDER_EMPLOYEE_ID NUMBER,RECIPIANT_EMPLOYEE_ID NUMBER,R_FORM_ID NUMBER,MESSAGE VARCHAR2
TRUNCATE TABLE MESSAGES;
DROP SEQUENCE MESSAGE_SEQ;
CREATE SEQUENCE MESSAGE_SEQ
  INCREMENT BY 1 MAXVALUE 5000 CYCLE;
EXECUTE INSERT_MESSAGE(2,3,4,'message one from User2');
EXECUTE INSERT_MESSAGE(0,3,3,'message two from System');
EXECUTE INSERT_MESSAGE(3,2,4,'message three from User3');
EXECUTE INSERT_MESSAGE(1,2,3,'message four from User1');
EXECUTE INSERT_MESSAGE(0,2,3,'message five from System');
EXECUTE INSERT_MESSAGE(2,1,4,'message six from User2');
EXECUTE INSERT_MESSAGE(0,1,3,'message seven from System');
-----------------------------------------------------------
--VIEWS
CREATE VIEW MYVIEW AS
    SELECT * FROM R_FORMS
    INNER JOIN GRADING_FORMAT ON  R_FORMS.GRADING_FORMAT_ID = grading_format.id;
SELECT * FROM R_FORMS WHERE EMPLOYEE_ID = 1;
