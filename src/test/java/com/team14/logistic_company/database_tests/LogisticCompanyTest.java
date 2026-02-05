package com.team14.logistic_company.database_tests;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.LogisticCompany;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class LogisticCompanyTest {

    private List<String> validate(LogisticCompany company) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(company)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private LogisticCompany buildCompany(String name, String uic) {
        LogisticCompany company = new LogisticCompany();
        company.setName(name);
        company.setUic(uic);
        company.setPhone("0888888888");
        company.setEmail("test@company.com");
        company.setAddress(new Address());
        return company;
    }

    @Test
    void whenDataIsValid() {
        LogisticCompany company = buildCompany("Speedy Express", "123456789");

        List<String> messages = validate(company);

        assertEquals(0, messages.size());
    }

    @Test
    void whenNameIsBlank() {
        LogisticCompany company = buildCompany("", "123456789");

        List<String> messages = validate(company);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Company name is required."));
    }

    @Test
    void whenUicIsBlank() {
        LogisticCompany company = buildCompany("Speedy Express", "");

        List<String> messages = validate(company);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("UIC (EIK) is required."));
    }

    @Test
    void whenNameAndUicAreBlank() {
        LogisticCompany company = buildCompany("", "");

        List<String> messages = validate(company);

        assertEquals(2, messages.size());
        assertTrue(messages.contains("Company name is required."));
        assertTrue(messages.contains("UIC (EIK) is required."));
    }
}
