package org.italy.generation.employeesAndDepartment.data.abstractions;

import org.italy.generation.employeesAndDepartment.entities.Department;

import java.sql.SQLException;

public interface DepartmentRepository {
   long departmentInsert(Department department) throws SQLException; //ritorna l'id assegnato su tabella
   boolean deleteDepartmentById(long id) throws SQLException;         //ritorna se è riuscito a cancellarlo
   boolean deleteDepartmentById(Department department) throws SQLException;
   Iterable<Department> departmentsFromPartName(String part) throws SQLException; //ritornerà i dipartimenti con dentro ognuno la propria lista d' impiegati
}
