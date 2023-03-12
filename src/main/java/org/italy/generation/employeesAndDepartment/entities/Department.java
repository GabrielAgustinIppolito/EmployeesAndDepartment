package org.italy.generation.employeesAndDepartment.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Department {
   private Long id;
   private String name;
   private String mail;
   private int maxCapacity;
   private Map<Long,Employee> employees = new HashMap<>();

   public Department(String name, String mail, int maxCapacity) {
      this(null, name, mail, maxCapacity);
   }
   public Department(Long id, String name, String mail, int maxCapacity) {
      this.id = id;
      this.name = name;
      this.mail = mail;
      this.maxCapacity = maxCapacity;

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

   public Map<Long,Employee> getEmployees() {
      return employees;
   }

   public void setEmployees(Map<Long,Employee> employees) {
      this.employees.putAll(employees);
   }
   public void addEmployee(Employee employee){
      employees.put(employee.getId(), employee);
   }

   @Override
   public String toString() {
      return "ID: " + getId() +
            ", Name: " + getName() +
            ", Mail: " + getMail() +
            ", Max Capacity: " + getMaxCapacity() +
            "\n Employees ID: " + getEmployees().toString() + "\n\n";
   }
}
