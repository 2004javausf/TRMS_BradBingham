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
PENDING_REIMBURSEMENT NUMBER,
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
PENDING_REIMBURSEMENT NUMBER,
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
        PENDING_REIMBURSEMENT,
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
    INSERT INTO MESSAGES VALUES(MESSAGE_SEQ.NEXTVAL,CURRENT_TIMESTAMP,0,EMPLOYEE_ID,R_FORM_SEQ.CURRVAL,'Reimbursement form '||R_FORM_SEQ.CURRVAL||' submitted');
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
(R_FORM_ID NUMBER,EMP_ID NUMBER,APPROVER_ID NUMBER,APPROVER VARCHAR2,NEW_STATUS VARCHAR2, REASON VARCHAR2)
AS
BEGIN
    IF APPROVER = 'Supervisor' THEN
            UPDATE R_FORMS SET APPROVE_SUPERVISOR = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET SUPERVISOR_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    ELSIF APPROVER = 'Head' THEN
            UPDATE R_FORMS SET APPROVE_HEAD = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET HEAD_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    ELSIF APPROVER = 'Associate' THEN
        IF NEW_STATUS = 'Accept' THEN
            UPDATE R_FORMS SET ALTERED_FORM = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
        ELSE
            UPDATE R_FORMS SET ALTERED_FORM = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET FORM_STATUS = 'Canceled' WHERE R_FORMS.ID = R_FORM_ID;
        END IF;
    ELSE
            UPDATE R_FORMS SET APPROVE_COORDINATOR = NEW_STATUS WHERE R_FORMS.ID = R_FORM_ID;
            UPDATE R_FORMS SET COORDINATOR_SUBMIT_DATE = CURRENT_TIMESTAMP WHERE R_FORMS.ID = R_FORM_ID;
    END IF;
    
    IF NEW_STATUS = 'Denied' THEN
            UPDATE R_FORMS SET REJECTION_JUSTIFY = REASON WHERE R_FORMS.ID = R_FORM_ID;
            INSERT INTO MESSAGES VALUES(
                MESSAGE_SEQ.NEXTVAL,
                CURRENT_TIMESTAMP,
                APPROVER_ID,
                EMP_ID,
                R_FORM_ID,
                APPROVER||'set form: '||R_FORM_ID||' to '||NEW_STATUS||' '+REASON);
    ELSE
        INSERT INTO MESSAGES VALUES(
        MESSAGE_SEQ.NEXTVAL,
        CURRENT_TIMESTAMP,
        APPROVER_ID,
        EMP_ID,
        R_FORM_ID,
        (APPROVER||' set form: '||R_FORM_ID||' to '||NEW_STATUS)
        );
    END IF;
    COMMIT;
END;
/
 

CREATE OR REPLACE PROCEDURE FINAL_STATUS_CHANGE
(BENCO_ID NUMBER,R_FORM NUMBER,EMP_ID NUMBER, NEW_GRADE_STATUS VARCHAR2, NEW_PRESENTATION_STATUS VARCHAR2)
AS
BEGIN
    IF NEW_GRADE_STATUS != NULL THEN
            UPDATE R_FORMS SET APPROVE_GRADE = NEW_GRADE_STATUS WHERE R_FORMS.ID = R_FORM;
            IF
                NEW_GRADE_STATUS = 'Passed' THEN
                UPDATE EMPLOYEES SET AVAILABLE_AMOUNT = (AVAILABLE_AMOUNT - (SELECT PENDING_REIMBURSEMENT FROM R_FORMS WHERE R_FORMS.ID= R_FORM)) WHERE EMPLOYEES.ID = (SELECT EMPLOYEE_ID FROM R_FORMS WHERE R_FORMS.ID = R_FORM);
                INSERT INTO MESSAGES VALUES(
                    MESSAGE_SEQ.NEXTVAL,
                    CURRENT_TIMESTAMP,
                    BENCO_ID,
                    EMP_ID,
                    R_FORM,
                    ('BenCo set form: '||R_FORM||' to '||NEW_GRADE_STATUS)
                    );
            ELSE
                INSERT INTO MESSAGES VALUES(
                MESSAGE_SEQ.NEXTVAL,
                CURRENT_TIMESTAMP,
                BENCO_ID,
                EMP_ID,
                R_FORM,
                'BenCo set form: '||R_FORM||' to '||NEW_GRADE_STATUS
                );
            END IF;
    ELSE
            UPDATE R_FORMS SET APPROVE_PRESENTATION = NEW_PRESENTATION_STATUS WHERE R_FORMS.ID = R_FORM;
            IF
                NEW_PRESENTATION_STATUS = 'Passed' THEN
                UPDATE EMPLOYEES SET AVAILABLE_AMOUNT = (AVAILABLE_AMOUNT - (SELECT PENDING_REIMBURSEMENT FROM R_FORMS WHERE R_FORMS.ID= R_FORM)) WHERE EMPLOYEES.ID = (SELECT EMPLOYEE_ID FROM R_FORMS WHERE R_FORMS.ID = R_FORM);
                INSERT INTO MESSAGES VALUES(
                    MESSAGE_SEQ.NEXTVAL,
                    CURRENT_TIMESTAMP,
                    BENCO_ID,
                    EMP_ID,
                    R_FORM,
                    ('BenCo set form: '||R_FORM||' to '||NEW_PRESENTATION_STATUS)
                    );
             ELSE
                INSERT INTO MESSAGES VALUES(
                MESSAGE_SEQ.NEXTVAL,
                CURRENT_TIMESTAMP,
                BENCO_ID,
                EMP_ID,
                R_FORM,
                'BenCo set form: '||R_FORM||' to '||NEW_GRADE_STATUS
                );
            END IF;
    
    END IF;
    COMMIT;
END;
/



UPDATE EMPLOYEES SET AVAILABLE_AMOUNT = (AVAILABLE_AMOUNT - (SELECT PENDING_REIMBURSEMENT FROM R_FORMS WHERE R_FORMS.ID= 1)) WHERE EMPLOYEES.ID = (SELECT EMPLOYEE_ID FROM R_FORMS WHERE R_FORMS.ID = 1);

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
EXECUTE INSERT_EMPLOYEE('UserFour','LastNm','User4','Password4',700,'Associate','Benefits','Main');
EXECUTE INSERT_EMPLOYEE('UserFive','LastNm','User5','Password5',600,'Supervisor','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserSix','LastNm','User6','Password6',500,'Associate','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserSeven','LastNm','User7','Password7',400,'Associate','Sales','Main');
EXECUTE INSERT_EMPLOYEE('UserEight','LastNm','User8','Password8',300,'Associate','Sales','East');

--supervisor

SELECT * FROM R_FORMS;
SELECT R_FORMS.ID ID,
R_FORMS.EMPLOYEE_ID EMPLOYEE_ID,
R_FORMS.FORM_STATUS FORM_STATUS,
R_FORMS.APPROVE_SUPERVISOR APPROVE_SUPERVISOR,
R_FORMS.SUPERVISOR_SUBMIT_DATE SUPERVISOR_SUBMIT_DATE,
R_FORMS.APPROVE_HEAD APPROVE_HEAD,
R_FORMS.HEAD_SUBMIT_DATE HEAD_SUBMIT_DATE,
R_FORMS.APPROVE_COORDINATOR APPROVE_COORDINATOR,
R_FORMS.COORDINATOR_SUBMIT_DATE COORDINATOR_SUBMIT_DATE,
R_FORMS.ALTERED_FORM ALTERED_FORM,
R_FORMS.REJECTION_JUSTIFY REJECTION_JUSTIFY,
R_FORMS.SUBMIT_DATE SUBMIT_DATE,
R_FORMS.START_DATE START_DATE,
R_FORMS.START_TIME START_TIME,
R_FORMS.EVENT_LOCATION EVENT_LOCATION,
R_FORMS.EVENT_COST EVENT_COST,
R_FORMS.PENDING_REIMBURSEMENT PENDING_REIMBURSEMENT,
R_FORMS.EVENT_DESCRIPTION EVENT_DESCRIPTION,
R_FORMS.EVENT_JUSTIFY EVENT_JUSTIFY,
R_FORMS.GRADING_FORMAT_ID GRADING_FORMAT_ID,
R_FORMS.EVENT_TYPE EVENT_TYPE,
R_FORMS.OPT_ON_SUBMIT OPT_ON_SUBMIT,
R_FORMS.ON_FINISH_GRADE ON_FINISH_GRADE,
R_FORMS.APPROVE_GRADE APPROVE_GRADE,
R_FORMS.ON_FINISH_PRESENTATION ON_FINISH_PRESENTATION,
R_FORMS.APPROVE_PRESENTATION APPROVE_PRESENTATION FROM R_FORMS INNER JOIN EMPLOYEES ON (R_FORMS.ID = EMPLOYEES.ID) 
WHERE DEPARTMENT = 'IT' AND OFFICE_LOC = 'Main';

--EMPLOYEE_ID NUMBER,START_DATE DATE,START_TIME VARCHAR2,EVENT_LOCATION VARCHAR2,EVENT_COST NUMBER,PENDING_REIMBURSEMENT,
--EVENT_DESCRIPTION VARCHAR2,EVENT_JUSTIFY VARCHAR2,GRADING_FORMAT_ID NUMBER,EVENT_TYPE VARCHAR2,OPT_ON_SUBMIT BLOB
TRUNCATE TABLE R_FORMS;
DROP SEQUENCE R_FORM_SEQ;
CREATE SEQUENCE R_FORM_SEQ
  INCREMENT BY 1 MAXVALUE 5000 CYCLE;
EXECUTE INSERT_R_FORM(1,'01-MAY-2021','1 AM','Leader School',1000,800,'School for leader','I will lead better',1,'University Course',NULL);
EXECUTE INSERT_R_FORM(2,'01-MAY-2021','2 AM','Supervisor Seminar',900,700,'Seminar for Supervisors','It will help',2,'Seminar',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','3 PM','Associate prep class',800,600,'Prep class for associates','It will help',3,'Certification Prep Class',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','4 PM','Associate Certification',600,500,'Certification for Associates','It will help',4,'Certification',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','5 PM','Associate Technical Training',500,400,'Technical Training for Associates','It will help',5,'Technical Training',NULL);
EXECUTE INSERT_R_FORM(3,'01-MAY-2021','6 PM','Associate therapy',400,300,'Therapy for Associates','It will help',5,'Other',NULL);
--R_FORM_ID ,EMP_ID ,APPROVER_ID, title ,NEW_STATUS , REASON 
EXECUTE R_FORM_STATUS_CHANGE(1,1,1,'Supervisor','Approved',NULL);
EXECUTE R_FORM_STATUS_CHANGE(3,3,2,'Supervisor','Approved',NULL);
EXECUTE R_FORM_STATUS_CHANGE(3,3,1,'Head','Approved',NULL);
EXECUTE R_FORM_STATUS_CHANGE(6,3,3,'Associate','Canceled',NULL);
--R_FORM_id ,EMP_ID , NEW_GRADE_STATUS , NEW_PRESENTATION_STATUS 
EXECUTE FINAL_STATUS_CHANGE(4,6,3,'Passed',null);
EXECUTE FINAL_STATUS_CHANGE(4,5,3,null,'Passed');


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
