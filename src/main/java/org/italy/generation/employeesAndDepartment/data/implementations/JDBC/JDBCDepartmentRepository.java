package org.italy.generation.employeesAndDepartment.data.implementations.JDBC;

import org.italy.generation.employeesAndDepartment.data.abstractions.DepartmentRepository;
import org.italy.generation.employeesAndDepartment.entities.Department;
import org.italy.generation.employeesAndDepartment.entities.Employee;
import org.italy.generation.employeesAndDepartment.entities.Sex;

import java.sql.*;
import java.util.*;

import static org.italy.generation.employeesAndDepartment.data.implementations.JDBC.JDBCConstants.
      JDBCDepartmentRepositoryConstants.*;

public class JDBCDepartmentRepository implements DepartmentRepository {
   private Connection conn;
   public JDBCDepartmentRepository (Connection connection) {
      this.conn = connection;
   }
   @Override
   public long departmentInsert(Department department) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(INSERT_DEPARTMENT_RETURNING_ID,
                                                        Statement.RETURN_GENERATED_KEYS)){
         ps.setString(1, department.getName());
         ps.setString(2, department.getMail());
         ps.setInt(3, department.getMaxCapacity());
         int rawUpdated =ps.executeUpdate();
         if (rawUpdated != 1){
            throw new SQLException("Errore nell'aggiunta del dipartimento");
         }
         try (ResultSet keys = ps.getGeneratedKeys()){
            keys.next();
            return keys.getLong(1);
         }
      } catch (SQLException e) {
         e.printStackTrace();
         throw new SQLException("Errore nell'inserimento del dipartimento. ",  e);
      }
   }

   @Override
   public boolean deleteDepartmentById(long id) throws SQLException {
//      se il dipartimento non è presente nei dipendenti cancella direttamente
      int rawUpdated;
      if (hasEmployeeDepartment(id)) {
         try (PreparedStatement ps = conn.prepareStatement(DELETE_DEPARTMENT_BY_ID_AND_NULL_IN_EMPLOYEE)) {
            ps.setLong(1, id);
            ps.setLong(2, id);
            rawUpdated = ps.executeUpdate();
//            System.out.println("Raw updated: " + rawUpdated);
            if (rawUpdated == 2) {
               return true;
            }
         }
      } else {
         try (PreparedStatement ps = conn.prepareStatement(DELETE_DEPARTMENT_BY_ID)) {
            ps.setLong(1, id);
            rawUpdated = ps.executeUpdate();
            if (rawUpdated == 1) {
               return true;
            }
         }
      }
      //se le rawUpdated non sono quelle sopra
      return false;
   }

   @Override
   public boolean deleteDepartmentById(Department department) throws SQLException {
      return deleteDepartmentById(department.getId());
   }

   @Override
   public Iterable<Department> departmentsFromPartName(String part) throws SQLException {
      part = String.format("%%%s%%", part);
      List<Department> departmentList = new ArrayList<>();
      try (PreparedStatement ps = conn.prepareStatement(DEPARTMENTS_FROM_PART_NAME)) {
         ps.setString(1, part);
         ResultSet rs = ps.executeQuery();
         while (rs.next()) {
            long idMoltoBelloESimpatico = rs.getLong("department_id");
            boolean departmentIsPresent = departmentList.stream()
                  .anyMatch(department ->
                        department.getId().longValue() == idMoltoBelloESimpatico);
            //secondo intellij tornerà sempre falso, secondo me è scemo
            if (departmentIsPresent) {
               if ((Long) rs.getLong("employee_id") != null) {
                  for (int i = 0; i < departmentList.size(); i++) {
                     Employee e = employeeFromRawWithoutDepartment(rs);
                     e.setDepartment(departmentList.get(i));
                     //aggiungo al departament il suo impiegato
                     departmentList.get(i).addEmployee(e);
//                  //all'ultimo impiegato aggiunto ↑ gli setto il suo department (puntatore del dep...)
//                  departmentList.get(i).getEmployees().last().setDepartment(departmentList.get(i));
                  }
               }
            } else {
               //aggiungo il nuovo department nella lista di department
               departmentList.add(departmentFromRaw(rs));
            }

         }
      }
      return departmentList;
   }

   private Employee employeeFromRawWithoutDepartment(ResultSet rs) throws SQLException {    //employee_id, firstname, lastname, enrollment_date, sex, department_id
      Sex sexy = Sex.UNDEFINED;
      if(rs.getObject("sex").toString().equalsIgnoreCase("male")){
         sexy = Sex.MALE;
      } else if (rs.getObject("sex").toString().equalsIgnoreCase("female")) {
         sexy = Sex.FEMALE;
      }
      Employee fantasticEmployee = new Employee(rs.getLong("employee_id"),
                                                rs.getString("firstname"),
                                                rs.getString("lastname"),
                                                rs.getDate("enrollment_date").toLocalDate(),
                                                sexy);
      return fantasticEmployee;
   }
   private Department departmentFromRaw(ResultSet rs) throws SQLException{    //department_id, name, mail, max_capacity,
      //creo il department
      Department amabileDepartment = new Department(rs.getLong("department_id"),
                                                    rs.getString("name"),
                                                    rs.getString("mail"),
                                                    rs.getInt("max_capacity")
                                                   );
      //aggiungo l'impiegato al department
      if ((Long) rs.getLong("employee_id") != null)
      {
         Employee emplo = employeeFromRawWithoutDepartment(rs);
         amabileDepartment.addEmployee(emplo);
         //aggiungo il department all' impiegato
         amabileDepartment.getEmployees().get(emplo.getId()).setDepartment(amabileDepartment);
      }
      return amabileDepartment;
   }
   private boolean hasEmployeeDepartment(long id) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(COUNT_DEPARTEMENT_EMPLOYEE)){
         ps.setLong(1,id);
         try (ResultSet rs = ps.executeQuery()){
            rs.next();
            return rs.getInt(1) > 0;
         }
      }
   }


   /**
    * Private valoreDiRitorno nomeMetodo(ps(che sarebbe la query parametrizzata, ?lambdaCheMappaUnaRiga?, oggettoDaCreare, args dell'oggetto))){}
    */
//* 3. (Opzionale) Creare due metodi per selezionare oggetti:
//         *   a. Uno prende una query parametrizzata, una lambda RawMapper che descriva come mappare una riga del ResultSet e
//   l' oggetto da creare, e i var args relativi all'oggetto da creare
//*   b. Un altro prende una query parametrizzata, una lambda StatementSetter che prende in input il PreparedStatement
//   e ne setta i parametri necessari, infine la lambda RawMapper (senza i var args perché ci penserà StatementSetter)
//   RowMapper<Person> rowMapper = (rs, rowNum) -> {
//      Person p = new Person();
//      p.setName(rs.getString("personName"));
//      p.setAddress(rs.getString("address"));
//      p.setAge(rs.getInt("age"));
//      return p;
//   };
//

//   public static void main(String[] args) {
//      System.out.println("Hello World");
//      String nice = String.format("%%%s%%", "hello sexy"); //diventa %hello sexy%
//      System.out.println(nice);
//   }

}













