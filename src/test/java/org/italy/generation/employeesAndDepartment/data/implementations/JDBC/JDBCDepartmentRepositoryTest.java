package org.italy.generation.employeesAndDepartment.data.implementations.JDBC;

import org.italy.generation.employeesAndDepartment.entities.Department;
import org.italy.generation.employeesAndDepartment.entities.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;

import static org.italy.generation.employeesAndDepartment.data.implementations.JDBC.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.italy.generation.employeesAndDepartment.data.implementations.JDBC.JDBCConstants.
              JDBCDepartmentRepositoryConstants.*;

class JDBCDepartmentRepositoryTest {
   Employee employee_1;
   Employee employee_2;
   Employee employee_3;
   Department department_1;
   Department department_2;
   private Connection conn;
   private JDBCDepartmentRepository repo;
   @BeforeEach
   void setUp() throws SQLException {
      employee_1 = new Employee(EMP_1_NAME, EMP_1_LASTNAME, EMP_1_ENROLL_DATE, EMP_1_SEX);
      employee_2 = new Employee(EMP_2_NAME, EMP_2_LASTNAME, EMP_2_ENROLL_DATE, EMP_2_SEX);
      employee_3 = new Employee(EMP_3_NAME, EMP_3_LASTNAME, EMP_3_ENROLL_DATE, EMP_3_SEX);
      conn = DriverManager.getConnection(URL,USER_NAME, PASSWORD);
      conn.setAutoCommit(false);
      repo = new JDBCDepartmentRepository(conn);
      update(ADD_EMPLOYEE, employee_1.getFirstname(), employee_1.getLastname(), employee_1.getEnrollmentDate(),
            employee_1.getSex());
      update(ADD_EMPLOYEE, employee_2.getFirstname(), employee_2.getLastname(), employee_2.getEnrollmentDate(),
            employee_2.getSex());
      update(ADD_EMPLOYEE, employee_3.getFirstname(), employee_3.getLastname(), employee_3.getEnrollmentDate(),
            employee_3.getSex());
   }
   private void update(String query, Object... params) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(query)){
         for(int i = 0; i < params.length; i++){
            if (params[i] instanceof Enum<?>) { // verifichiamo che la classe sia un Enum di qualsiasi tipo per convertirlo correttamente nel suo corrispettivo in SQL
               ps.setObject(i+1, params[i], Types.OTHER);
            } else {
               ps.setObject(i+1,params[i]);
            }
         }
         ps.executeUpdate();
      }
   }
   @AfterEach
   void tearDown() {
      try {
         if(conn != null){
            conn.rollback();
         }
      } catch (SQLException e){
         throw new RuntimeException(e);
      }
   }

   @org.junit.jupiter.api.Test
   void departmentInsert() {
   }

   @org.junit.jupiter.api.Test
   void deleteDepartmentById() {
   }

   @org.junit.jupiter.api.Test
   void departmentsFromPartName() {
   }
}