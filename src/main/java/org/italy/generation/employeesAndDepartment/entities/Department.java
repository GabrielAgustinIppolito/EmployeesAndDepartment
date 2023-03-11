package org.italy.generation.employeesAndDepartment.entities;

import java.util.SortedSet;

public class Department {
   private Long id;
   private String name;
   private String mail;
   private int maxCapacity;
   private SortedSet<Employee> employees;

   public Department(String name, String mail, int maxCapacity) {
      this(null, name, mail, maxCapacity, null);
   }
   public Department(Long id, String name, String mail, int maxCapacity) {
      this(id, name, mail, maxCapacity, null);
   }
   public Department(Long id, String name, String mail, int maxCapacity, SortedSet<Employee> employees) {
      this.id = id;
      this.name = name;
      this.mail = mail;
      this.maxCapacity = maxCapacity;
      this.employees = employees;
   }
   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getMail() {
      return mail;
   }

   public int getMaxCapacity() {
      return maxCapacity;
   }

   public SortedSet<Employee> getEmployees() {
      return employees;
   }

   public void setEmployees(SortedSet<Employee> employees) {
      this.employees = employees;
   }
   public void addEmployee(Employee employee){
      employees.add(employee);
   }
}
