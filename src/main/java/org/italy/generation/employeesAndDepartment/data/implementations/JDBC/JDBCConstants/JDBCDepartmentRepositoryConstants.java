package org.italy.generation.employeesAndDepartment.data.implementations.JDBC.JDBCConstants;

public class JDBCDepartmentRepositoryConstants {
   public static final String URL = "jdbc:postgresql://localhost:5432/employees&departments";
   public static final String USER_NAME = "postgresMaster";
   public static final String PASSWORD = "goPostgresGo";
   public static final String INSERT_DEPARTMENT_RETURNING_ID = """
          INSERT INTO department(department_id, name, mail, max_capacity)
          VALUES (nextval('department_sequence'), ?, ?, ?)
          RETURNING department_id;
          """;
   public static final String DELETE_DEPARTMENT_BY_ID_AND_NULL_IN_EMPLOYEE = """
          UPDATE employee
          SET department_id = NULL
          WHERE department_id = ?;
          
          DELETE FROM department
          WHERE department_id = ?
          """;
   public static final String DELETE_DEPARTMENT_BY_ID = """
          DELETE FROM department
          WHERE department_id = ?
          """;
   public static final String SET_DEPARTMENT_NULL_EMPLOYEE = """
          UPDATE employee
          SET department_id = NULL
          WHERE department_id = ?;
          """;
   public static String COUNT_DEPARTEMENT_EMPLOYEE = """
          SELECT COUNT (department_id)
          FROM employee
          WHERE department_id = ?
          """;
   public static final String DEPARTMENTS_FROM_PART_NAME = """
          SELECT department_id, name, mail, max_capacity,
                 employee_id, firstname, lastname, enrollment_date, sex
          FROM department LEFT OUTER JOIN employee
          USING (department_id)
          WHERE name LIKE ?
          """;
   public static final String DEPARTMENT_FIND_BY_ID = """
          SELECT department_id, name, mail, max_capacity
          FROM department
          WHERE department_id = ?  
          """;

}
