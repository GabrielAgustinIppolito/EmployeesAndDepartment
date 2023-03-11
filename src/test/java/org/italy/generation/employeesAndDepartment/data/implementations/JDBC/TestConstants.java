package org.italy.generation.employeesAndDepartment.data.implementations.JDBC;

import org.italy.generation.employeesAndDepartment.entities.Sex;

import java.time.LocalDate;

public class TestConstants {
   public final static String EMP_1_NAME = "Aileen";
   public final static String EMP_1_LASTNAME= "Wuornos";
   public final static LocalDate EMP_1_ENROLL_DATE = LocalDate.of(1956,2,29);
   public final static Sex EMP_1_SEX = Sex.FEMALE;
   public final static String EMP_2_NAME = "Richard Leonard";
   public final static String EMP_2_LASTNAME= "Kuklinsk";
   public final static LocalDate EMP_2_ENROLL_DATE = LocalDate.of(1935,4,11);
   public final static Sex EMP_2_SEX = Sex.MALE;
   public final static String EMP_3_NAME = "Aleister";
   public final static String EMP_3_LASTNAME= "Crowley";
   public final static LocalDate EMP_3_ENROLL_DATE = LocalDate.of(1947,12,1);
   public final static Sex EMP_3_SEX = Sex.UNDEFINED;
   public final static String ADD_EMPLOYEE = """
          INSERT INTO employee(employee_id, firstname, lastname, enrollment_date, sex)
          VALUES (nextval('employee_sequence'), ?, ?, ?,?)
          """;

}
