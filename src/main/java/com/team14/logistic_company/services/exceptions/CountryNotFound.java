package com.team14.logistic_company.services.exceptions;

public class CountryNotFound extends RuntimeException {
  public CountryNotFound(Integer id) {
    super("Country not found with id: " + id);
  }
  public CountryNotFound(String message) {
    super(message);
  }
}
