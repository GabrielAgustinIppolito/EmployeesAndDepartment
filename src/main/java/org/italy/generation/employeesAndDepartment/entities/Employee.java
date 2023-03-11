package org.italy.generation.employeesAndDepartment.entities;

import java.time.LocalDate;

public class Employee {
   private Long id;
   private String firstname;
   private String lastname;
   private LocalDate enrollmentDate;
   Sex sex;
   Department department;
   //employee_id, firstname, lastname, enrollment_date, sex, department_id
   public Employee(String firstname, String lastname, LocalDate enrollmentDate, Sex sex) {
      this(null, firstname, lastname, enrollmentDate, sex, null);
   }
   public Employee(Long id, String firstname, String lastname, LocalDate enrollmentDate, Sex sex) {
      this(id, firstname, lastname, enrollmentDate, sex, null);
   }

   public Employee(Long id, String firstname, String lastname, LocalDate enrollmentDate, Sex sex, Department department) {
      this.id = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.enrollmentDate = enrollmentDate;
      this.sex = sex;
      this.department = department;
   }

   public Long getId() {
      return id;
   }

   public String getFirstname() {
      return firstname;
   }

   public String getLastname() {
      return lastname;
   }

   public LocalDate getEnrollmentDate() {
      return enrollmentDate;
   }

   public Sex getSex() {
      return sex;
   }

   public Department getDepartment() {
      return department;
   }

   public void setDepartment(Department department) {
      this.department = department;
   }
}
