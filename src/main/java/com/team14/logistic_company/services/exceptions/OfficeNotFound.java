package com.team14.logistic_company.services.exceptions;

public class OfficeNotFound extends RuntimeException {
   public OfficeNotFound(Integer id) {
     super("Office not found with id: " + id);
   }

    public OfficeNotFound(String message) {
      super(message);
    }
}