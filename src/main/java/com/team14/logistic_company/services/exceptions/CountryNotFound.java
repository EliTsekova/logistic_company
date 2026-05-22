package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a country cannot be found in the system.
 */
public class CountryNotFound extends RuntimeException {

  /**
   * Constructs a new CountryNotFound exception for a missing country by id.
   *
   * @param id the id of the country that was not found
   */
  public CountryNotFound(Integer id) {
    super("Country not found with id: " + id);
  }

  /**
   * Constructs a new CountryNotFound exception with a custom message.
   *
   * @param message detailed message describing the exception
   */
  public CountryNotFound(String message) {
    super(message);
  }
}