package org.italy.generation.employeesAndDepartment.data.abstractions;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RSRowMapper <T> {
   T mapRow(ResultSet rs) throws SQLException;
}
