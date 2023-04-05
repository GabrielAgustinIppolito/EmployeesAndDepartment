package org.italy.generation.employeesAndDepartment.data.implementations.JDBC;

import org.italy.generation.employeesAndDepartment.entities.Department;
import org.italy.generation.employeesAndDepartment.entities.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
   Department department_aux;
   private Connection conn;
   private JDBCDepartmentRepository repo;
   @BeforeEach
   void setUp() throws SQLException {
      employee_1 = new Employee(EMP_1_NAME, EMP_1_LASTNAME, EMP_1_ENROLL_DATE, EMP_1_SEX);
      employee_2 = new Employee(EMP_2_NAME, EMP_2_LASTNAME, EMP_2_ENROLL_DATE, EMP_2_SEX);
      employee_3 = new Employee(EMP_3_NAME, EMP_3_LASTNAME, EMP_3_ENROLL_DATE, EMP_3_SEX);
      department_1 = new Department(DEPARTMENT_1_NAME, DEPARTMENT_1_MAIL, DEPARTMENT_1_MAX_CAPACITY);
      department_2 = new Department(DEPARTMENT_2_NAME, DEPARTMENT_2_MAIL, DEPARTMENT_2_MAX_CAPACITY);
      department_aux = new Department(DEPARTMENT_AUX_NAME, DEPARTMENT_AUX_MAIL, DEPARTMENT_AUX_MAX_CAPACITY);
      employee_1.setDepartment(department_1);
      employee_2.setDepartment(department_1);
      employee_3.setDepartment(department_2);
      department_1.addEmployee(employee_1);
      department_1.addEmployee(employee_2);
      department_2.addEmployee(employee_3);
      conn = DriverManager.getConnection(URL,USER_NAME, PASSWORD);
      conn.setAutoCommit(false);
      repo = new JDBCDepartmentRepository(conn);
      long id1 = update(INSERT_DEPARTMENT, department_1.getName(), department_1.getMail(), department_1.getMaxCapacity());
      long id2 = update(INSERT_DEPARTMENT, department_2.getName(), department_2.getMail(), department_2.getMaxCapacity());
      update(INSERT_DEPARTMENT, department_aux.getName(), department_aux.getMail(), department_aux.getMaxCapacity());

      update(ADD_EMPLOYEE, employee_1.getFirstname(), employee_1.getLastname(), employee_1.getEnrollmentDate(),
            employee_1.getSex(),id1);
      update(ADD_EMPLOYEE, employee_2.getFirstname(), employee_2.getLastname(), employee_2.getEnrollmentDate(),
            employee_2.getSex(),id1);
      update(ADD_EMPLOYEE, employee_3.getFirstname(), employee_3.getLastname(), employee_3.getEnrollmentDate(),
            employee_3.getSex(), id2);
   }
   private long update(String query, Object... params) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
         for(int i = 0; i < params.length; i++){
            if (params[i] instanceof Enum<?>) {
               ps.setObject(i+1, params[i], Types.OTHER);
            } else {
               ps.setObject(i+1,params[i]);
            }
         }
         ps.executeUpdate();
         try (ResultSet keys = ps.getGeneratedKeys()){
            keys.next();
            return keys.getLong(1);
         }
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
      try {
         try(Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(COUNT_DEPARTMENT)){
            rs.next();
            assertTrue(rs.getInt(1) == 2);
            Long newLong = repo.insert(department_aux);
            try(Statement st2 = conn.createStatement();
               ResultSet rs2 = st2.executeQuery(COUNT_DEPARTMENT)){
               rs2.next();
               assertEquals(rs2.getInt(1) , 3);
            }
            System.out.println(newLong);
            assertNotNull(newLong);
         }
      } catch (SQLException e) {
         throw new RuntimeException("Errore nel test insert",e);
      }
   }

   @org.junit.jupiter.api.Test
   void deleteDepartmentById() {
      Long newLong = null;
      try {
         try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(COUNT_DEPARTMENT)){
            rs.next();
            assertTrue(rs.getInt(1) == 2);
         }
         try(PreparedStatement ps = conn.prepareStatement(RETRUN_ID_BY_NAME);
             ){
            ps.setString(1,department_1.getName());
            try(ResultSet rs = ps.executeQuery()){
               rs.next();
               Long idCheGioia = rs.getLong("department_id");
               assertTrue(repo.deleteById(idCheGioia));
            }
         }
         try(Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(COUNT_DEPARTMENT)){
            rs.next();
            assertTrue(rs.getInt(1) == 1);
         }
         newLong = repo.insert(department_aux);
         System.out.println(newLong);
         assertNotNull(newLong);
         assertTrue(repo.deleteById(newLong));
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   @org.junit.jupiter.api.Test
   void departmentsFromPartName() {
      try {
//         try(Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(PRINT_ALL_DEPARTMENT)){
//            rs.next();
//            System.out.println(rs.getString("name"));
//            rs.next();
//            System.out.println(rs.getString("name"));
//            rs.next();
//            System.out.println(rs.getString("name"));
//         }
         System.out.println("********* Parte 1 *********");
         System.out.println("********** della **********");
         System.out.println("***************************");
         Iterable<Department> departmentList = repo.departmentsByNameLike("della");
         departmentList.forEach(department -> System.out.println(department.toString()));
         Collection<Department> ld = (Collection <Department>) departmentList;
         assertTrue(ld.size() == 2);

         System.out.println("********* Parte 2 *********");
         System.out.println("*********** lic ***********");
         System.out.println("***************************");
         departmentList = repo.departmentsByNameLike("lic");
         departmentList.forEach(department -> System.out.println(department.toString()));
         ld = (Collection <Department>) departmentList;
         assertTrue(ld.size() == 1);
//NON SONO RIUSCITO A FARMI TORNARE IL DIPARTIMENTO SENZA IMPIEGATI
         System.out.println("********* Parte 3 *********");
         System.out.println("*********** tu ***********");
         System.out.println("***************************");
         departmentList = repo.departmentsByNameLike("rc");
         departmentList.forEach(department -> System.out.println(department.toString()));
         ld = (Collection <Department>) departmentList;
         assertTrue(ld.size() == 1);


         System.out.println("********* Parte 4 *********");
         System.out.println("*********** 00 ***********");
         System.out.println("***************************");
         departmentList = repo.departmentsByNameLike("00");
         departmentList.forEach(department -> System.out.println(department.toString()));
         ld = (Collection <Department>) departmentList;
         assertTrue(ld.size() == 0);

      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}











