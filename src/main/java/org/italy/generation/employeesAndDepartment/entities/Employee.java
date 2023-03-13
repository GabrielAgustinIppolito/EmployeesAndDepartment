package org.italy.generation.employeesAndDepartment.entities;

import java.time.LocalDate;

public class Employee {
   private long id;
   private String firstname;
   private String lastname;
   private LocalDate enrollmentDate;
   private Sex sex;
   private Department department;
   //employee_id, firstname, lastname, enrollment_date, sex, department_id
   public Employee(String firstname, String lastname, LocalDate enrollmentDate, Sex sex) {
      this(0, firstname, lastname, enrollmentDate, sex, null);
   }
   public Employee(long id, String firstname, String lastname, LocalDate enrollmentDate, Sex sex) {
      this(id, firstname, lastname, enrollmentDate, sex, null);
   }

   public Employee(long id, String firstname, String lastname, LocalDate enrollmentDate, Sex sex, Department department) {
      this.id = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.enrollmentDate = enrollmentDate;
      this.sex = sex;
      this.department = department;
   }

   public long getId() {
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
   @Override
   public String toString() {
       return "ID: " + getId() +
            ", Name: " + getFirstname() +
            ", Last Name: " + getLastname() +
            ", Enrollment Date: " + getEnrollmentDate().toString() +
            ", Sex: " + getSex() + "\n";
   }

}
