package com.team14.logistic_company.database_tests;
import com.team14.logistic_company.entities.Country;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CountryTest {

    private List<String> validate(Country country) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(country)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private Country buildCountry(String name) {
        Country country = new Country();
        country.setName(name);
        return country;
    }

    @Test
    void whenDataIsValid() {
        Country country = buildCountry("Bulgaria");

        List<String> messages = validate(country);

        assertEquals(0, messages.size());
    }

    @Test
    void whenNameIsBlank() {
        Country country = buildCountry("");

        List<String> messages = validate(country);

        // "" нарушава @NotBlank и @Size(min=3)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("The name of the country cannot be blank!"));
        assertTrue(messages.contains("The name of the country has to be between 3 and 50 characters!"));
    }

    @Test
    void whenNameIsLessThan3Symbols() {
        Country country = buildCountry("co");

        List<String> messages = validate(country);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The name of the country has to be between 3 and 50 characters!"));
    }
}
