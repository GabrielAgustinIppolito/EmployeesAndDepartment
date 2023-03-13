package org.italy.generation.employeesAndDepartment.data.abstractions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PSSetter {
   void setParams(PreparedStatement ps) throws SQLException;
}
