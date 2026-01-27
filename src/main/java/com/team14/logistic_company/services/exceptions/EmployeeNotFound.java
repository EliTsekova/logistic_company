package com.team14.logistic_company.services.exceptions;

public class EmployeeNotFound extends RuntimeException {
  public EmployeeNotFound(Integer id) {
    super("Employee not found with id: " + id);
  }

  public EmployeeNotFound(String message) {
    super(message);
  }
}