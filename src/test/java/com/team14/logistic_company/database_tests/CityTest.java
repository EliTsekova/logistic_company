package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Country;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    private Country country;

    private List<String> validate(City city) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(city)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @BeforeEach
    void init() {
        country = new Country();
    }

    private City buildCity(String name) {
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        return city;
    }

    @Test
    void whenDataIsValid() {
        City city = buildCity("Sofia");

        List<String> messages = validate(city);

        assertEquals(0, messages.size());
    }

    @Test
    void whenNameIsBlank() {
        City city = buildCity("");

        List<String> messages = validate(city);

        // "" нарушава @NotBlank и @Size(min=3)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("The city name cannot be blank!"));
        assertTrue(messages.contains("The city name has to be between 3 and 20 characters!"));
    }

    @Test
    void whenNameIsLessThan3Symbols() {
        City city = buildCity("c");

        List<String> messages = validate(city);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The city name has to be between 3 and 20 characters!"));
    }
}
