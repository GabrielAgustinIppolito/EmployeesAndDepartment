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
   public final static String DEPARTMENT_1_NAME = "<3 Dipartimento della Felicità <3";
   public final static String DEPARTMENT_1_MAIL = "allegrìa@amore.lv";
   public final static int DEPARTMENT_1_MAX_CAPACITY = 2;
   public final static String DEPARTMENT_2_NAME = "<3 Dipartimento della Gaiezza <3";
   public final static String DEPARTMENT_2_MAIL = "gioia@amore.lv";
   public final static int DEPARTMENT_2_MAX_CAPACITY = 50;
   public final static String DEPARTMENT_AUX_NAME = "Perché essere tu, quando puoi essere di più";
   public final static String DEPARTMENT_AUX_MAIL = "dell_amore@della_morte.lv";
   public final static int DEPARTMENT_AUX_MAX_CAPACITY = 999;
   public final static String ADD_EMPLOYEE = """
          INSERT INTO employee(employee_id, firstname, lastname, enrollment_date, sex, department_id)
          VALUES (nextval('employee_sequence'), ?, ?, ?, ?, ?)
          """;
   public static final String INSERT_DEPARTMENT = """
          INSERT INTO department(department_id, name, mail, max_capacity)
          VALUES (nextval('department_sequence'), ?, ?, ?)
          RETURNING department_id;
          """;
   public static final String COUNT_DEPARTMENT = """
          SELECT COUNT (department_id)
          FROM department
          """;
   public static final String RETRUN_ID_BY_NAME = """
          SELECT department_id
          FROM department
          WHERE name = ?
          """;
   public static final String PRINT_ALL_DEPARTMENT = """
          SELECT department_id, name, mail, max_capacity
          FROM department
          """;
}
