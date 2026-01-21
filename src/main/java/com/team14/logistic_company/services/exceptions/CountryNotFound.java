package com.team14.logistic_company.services.exceptions;

public class CountryNotFound extends RuntimeException {
  public CountryNotFound(String message) {
    super(message);
  }
}
