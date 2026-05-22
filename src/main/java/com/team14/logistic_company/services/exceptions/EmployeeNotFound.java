package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when an employee cannot be found in the system.
 */
public class EmployeeNotFound extends RuntimeException {

  /**
   * Constructs a new EmployeeNotFound exception for a missing employee by id.
   *
   * @param id the id of the employee that was not found
   */
  public EmployeeNotFound(Integer id) {
    super("Employee not found with id: " + id);
  }

  /**
   * Constructs a new EmployeeNotFound exception with a custom message.
   *
   * @param message detailed message describing the exception
   */
  public EmployeeNotFound(String message) {
    super(message);
  }
}