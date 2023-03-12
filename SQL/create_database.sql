DROP TABLE IF EXISTS employee, department;
DROP TYPE IF EXISTS sex;

CREATE TYPE sex AS ENUM('MALE', 'FEMALE', 'UNDEFINED');


CREATE TABLE department
(
   department_id     BIGINT         NOT NULL,
   name              VARCHAR(100)   NOT NULL,
   mail              VARCHAR(100)   NOT NULL,
   max_capacity      INT                NULL,
   CONSTRAINT PK_department PRIMARY KEY (department_id)
);
CREATE SEQUENCE department_sequence
OWNED BY department.department_id;

CREATE TABLE employee
(
   employee_id       BIGINT         NOT NULL,
   firstname         VARCHAR(32)    NOT NULL,
   lastname          VARCHAR(32)    NOT NULL,
   enrollment_date   DATE               NULL,
   sex               sex                NULL,
   department_id     BIGINT             NULL, --fatto apposta per poter cancellare il department
   CONSTRAINT PK_employee PRIMARY KEY(employee_id),
   CONSTRAINT FK_employee_department FOREIGN KEY(department_id)
      REFERENCES department(department_id)
);
CREATE SEQUENCE employee_sequence
OWNED BY employee.employee_id;