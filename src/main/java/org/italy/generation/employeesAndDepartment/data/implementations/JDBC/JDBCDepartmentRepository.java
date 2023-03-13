package org.italy.generation.employeesAndDepartment.data.implementations.JDBC;

import org.italy.generation.employeesAndDepartment.data.abstractions.DepartmentRepository;
import org.italy.generation.employeesAndDepartment.data.abstractions.PSSetter;
import org.italy.generation.employeesAndDepartment.data.abstractions.RSRowMapper;
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
   public Optional<Department> findById(long id) throws SQLException{
      return queryForObject(DEPARTMENT_FIND_BY_ID, rs->
         new Department(rs.getLong("department_id"),
                               rs.getString("name"),
                               rs.getString("mail"),
                               rs.getInt("max_capacity"))
      ,id);
   }
   public Optional<Department> findById2(long id) throws SQLException{
      return queryForObject(DEPARTMENT_FIND_BY_ID, this::fromResultSet,id);
   }
   public Department fromResultSet (ResultSet rs) throws SQLException{
      return new Department(rs.getLong("department_id"),
            rs.getString("name"),
            rs.getString("mail"),
            rs.getInt("max_capacity"));
   }
   public Optional<Department> findById3(long id) throws SQLException{
      return queryForObject(DEPARTMENT_FIND_BY_ID,
            ps -> ps.setLong(1,id),
            rs->
                  new Department(rs.getLong("department_id"),
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getInt("max_capacity"))
                  );
   }
   @Override
   public long insert(Department department) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(INSERT_DEPARTMENT_RETURNING_ID,
                                                        Statement.RETURN_GENERATED_KEYS)){
         ps.setString(1, department.getName());
         ps.setString(2, department.getMail());
         ps.setInt(3, department.getMaxCapacity());
         ps.executeUpdate();
         try (ResultSet keys = ps.getGeneratedKeys()){
            keys.next();
            long key = keys.getLong(1);
            department.setId(key);
            return key;
         }
      }
   }

//   @Override
//   public boolean deleteById(long id) throws SQLException {
////      se il dipartimento non è presente nei dipendenti cancella direttamente
//      int rowUpdated;
//      if (departmentHasEmployees(id)) {
//         try (PreparedStatement ps = conn.prepareStatement(DELETE_DEPARTMENT_BY_ID_AND_NULL_IN_EMPLOYEE)) {
//            ps.setLong(1, id);
//            ps.setLong(2, id);
//            rowUpdated = ps.executeUpdate();
//            if (rowUpdated >= 2) {
//               return true;
//            }
//         }
//      } else {
//         try (PreparedStatement ps = conn.prepareStatement(DELETE_DEPARTMENT_BY_ID)) {
//            ps.setLong(1, id);
//            rowUpdated = ps.executeUpdate();
//            if (rowUpdated == 1) {
//               return true;
//            }
//         }
//      }
//      //se le rawUpdated non sono quelle sopra
//      return false;
//   }
   @Override
   public boolean deleteById(long id) throws SQLException {
//      se il dipartimento non è presente nei dipendenti cancella direttamente
      try (PreparedStatement setDepNull = conn.prepareStatement(SET_DEPARTMENT_NULL_EMPLOYEE);
           PreparedStatement delDep = conn.prepareStatement(DELETE_DEPARTMENT_BY_ID)) {
         setDepNull.setLong(1, id);
         int empRemoved = setDepNull.executeUpdate(); //Numero degli impiegati a cui è stato settato null il dipartimento
         delDep.setLong(1, id);
         int depDeleted = delDep.executeUpdate();
         return depDeleted == 1;
      }
   }
   @Override
   public boolean deleteById(Department department) throws SQLException {
      return deleteById(department.getId());
   }

   @Override
//   public Iterable<Department> departmentsByNameLike(String part) throws SQLException {
////      part = String.format("%%%s%%", part);
//      String partLike = "%" + part + "%";
//      List<Department> departmentList = new ArrayList<>();
//      try (PreparedStatement ps = conn.prepareStatement(DEPARTMENTS_FROM_PART_NAME)) {
//         ps.setString(1, partLike);
//         try(ResultSet rs = ps.executeQuery()){
//            while (rs.next()) {
//               long idDep = rs.getLong("department_id");
//               boolean departmentIsPresent = departmentList.stream()
//                     .anyMatch(department ->
//                           department.getId() == idDep);
//               if (departmentIsPresent) {
//                  if (rs.getLong("employee_id") != 0) {
//                     for (int i = 0; i < departmentList.size(); i++) {
//                        Employee e = employeeFromRowWithoutDepartment(rs);
//                        e.setDepartment(departmentList.get(i));
//                        //aggiungo al departament il suo impiegato
//                        departmentList.get(i).addEmployee(e);
//                     }
//                  }
//               } else {
//                  //aggiungo il nuovo department nella lista di department
//                  departmentList.add(departmentFromRaw(rs));
//               }
//            }
//         }
//      }
//      return departmentList;
//   }
   public Iterable<Department> departmentsByNameLike(String part) throws SQLException {
//      part = String.format("%%%s%%", part);
      String partLike = "%" + part + "%";
      Map<Long,Department> departmentMap = new HashMap<>();
      try (PreparedStatement ps = conn.prepareStatement(DEPARTMENTS_FROM_PART_NAME)) {
         ps.setString(1, partLike);
         try(ResultSet rs = ps.executeQuery()){
            while (rs.next()){
               //Leggo l'id del dipartimento, chiedo alla mappa di darmi quel dipartimento
               //se me lo da e c'è un impiegato, aggiungo un impiegato al ripartimento che
               //mi ha dato la mappa
               //Se non è presente creo il dipartimento, gli aggiungo l'impiegato se presente
               //poi aggiungo il dipartimento alla mappa
               long idDepartment = rs.getLong("department_id");
               Department found = departmentMap.get(idDepartment);
               if (found != null){
                  found.addEmployee(employeeFromRow(rs));
               } else  {
                  Department newDepartment = departmentWithEmployeeFromRow(rs);
                  departmentMap.put(newDepartment.getId(), newDepartment);
               }
            }
         }
      }
      return departmentMap.values();
   }

   private Employee employeeFromRow(ResultSet rs) throws SQLException {    //employee_id, firstname, lastname, enrollment_date, sex, department_id
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
   private Department departmentWithEmployeeFromRow(ResultSet rs) throws SQLException{    //department_id, name, mail, max_capacity,
      //creo il department
      Department amabileDepartment = new Department(rs.getLong("department_id"),
                                                    rs.getString("name"),
                                                    rs.getString("mail"),
                                                    rs.getInt("max_capacity")
                                                   );
      //aggiungo l'impiegato al department
      if (rs.getLong("employee_id") != 0)
      {
         Employee emplo = employeeFromRow(rs);
         amabileDepartment.addEmployee(emplo);
         //aggiungo il department all' impiegato
         emplo.setDepartment(amabileDepartment);
      }
      return amabileDepartment;
   }
   private boolean departmentHasEmployees(long id) throws SQLException {
      try (PreparedStatement ps = conn.prepareStatement(COUNT_DEPARTEMENT_EMPLOYEE)){
         ps.setLong(1,id);
         try (ResultSet rs = ps.executeQuery()){
            rs.next();
            return rs.getInt(1) > 0;
         }
      }
   }

   public <T> Optional<T> queryForObject(String query, RSRowMapper<T> mapper, Object... params) throws SQLException{
      try(PreparedStatement ps = conn.prepareStatement(query)){
         for(int i = 0; i < params.length; i++){
            if(params[i] instanceof Enum<?>){
               ps.setObject(i+1, params[i], Types.OTHER);
            } else {
               ps.setObject(i+1, params[i]);
            }
         }
         try(ResultSet rs = ps.executeQuery()){
            if (rs.next()){
               T result = mapper.mapRow(rs);
               return Optional.of(result);
            } else {
               return Optional.empty();
            }
         }
      }
   }

   public <T> Optional<T> queryForObject (String query, PSSetter setter, RSRowMapper<T> mapper) throws SQLException{
      try(PreparedStatement ps = conn.prepareStatement(query)){
         setter.setParams(ps);
         try(ResultSet rs = ps.executeQuery()){
            if (rs.next()){
               T result = mapper.mapRow(rs);
               return Optional.of(result);
            } else {
               return Optional.empty();
            }
         }
      }
   }

   public <T> List<T> query(String q, RSRowMapper<T> mapper, Object... params) throws SQLException{
      try(PreparedStatement ps = conn.prepareStatement(q)){
         for(int i = 0; i < params.length; i++){
            if(params[i] instanceof Enum<?>){
               ps.setObject(i+1, params[i], Types.OTHER);
            } else {
               ps.setObject(i+1, params[i]);
            }
         }
         try(ResultSet rs = ps.executeQuery()){
            List<T> result = new ArrayList<>();
            while (rs.next()){
               T element = mapper.mapRow(rs);
               result.add(element);
            }
            return result;
         }
      }
   }
}

//
//   a. Uno prende una query parametrizzata, una lambda RawMapper che descriva come mappare una riga
//   del ResultSet e
//   l' oggetto da creare, e i var args relativi all'oggetto da creare
//*   b. Un altro prende una query parametrizzata, una lambda StatementSetter che prende in input il
//    PreparedStatement e ne setta i parametri necessari, infine la lambda RawMapper (senza i var
//    args perché ci penserà StatementSetter)


//scoprire perché sichiama Template la JDBCTemplate, cos'è il design pattern Template












