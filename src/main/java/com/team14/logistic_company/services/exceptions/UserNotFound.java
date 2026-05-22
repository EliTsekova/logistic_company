package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a user cannot be found in the system.
 */
public class UserNotFound extends RuntimeException {

  /**
   * Constructs a new UserNotFound exception with a custom message.
   *
   * @param message detailed message describing the exception
   */
  public UserNotFound(String message) {
    super(message);
  }
}