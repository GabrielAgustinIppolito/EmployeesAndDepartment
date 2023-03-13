package org.italy.generation.employeesAndDepartment.entities;

import java.util.*;

public class Department {
   private long id;
   private String name;
   private String mail;
   private int maxCapacity;
   private Map<Long,Employee> employees = new HashMap<>();

   public void setId(long id) {
      this.id = id;
   }

   public Department(String name, String mail, int maxCapacity) {
      this(0, name, mail, maxCapacity);
   }
   public Department(long id, String name, String mail, int maxCapacity) {
      this.id = id;
      this.name = name;
      this.mail = mail;
      this.maxCapacity = maxCapacity;

   }
   public long getId() {
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

   @Override
   public boolean equals(Object obj) {
      if (obj == null){
         return false;
      }
//      if(obj instanceof Department){ //l'oggetto puntato da obj é un ogg puntato da Dpartment o figlia
      if(obj.getClass() != Department.class){ //se obj è figlia di department da false
         return false;
      }
      Department other = (Department) obj;
      return this.getId() == other.getId();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getId());  //genera un hashcode valido dall'id passato
   }
}
