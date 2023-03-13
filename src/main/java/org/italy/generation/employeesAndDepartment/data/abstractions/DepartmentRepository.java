package org.italy.generation.employeesAndDepartment.data.abstractions;

import org.italy.generation.employeesAndDepartment.entities.Department;

import java.sql.SQLException;

public interface DepartmentRepository {
   long insert(Department department) throws SQLException; //ritorna l'id assegnato su tabella
   boolean deleteById(long id) throws SQLException;         //ritorna se è riuscito a cancellarlo
   boolean deleteById(Department department) throws SQLException;
   Iterable<Department> departmentsByNameLike(String part) throws SQLException; //ritornerà i dipartimenti con dentro ognuno la propria lista d' impiegati
}
