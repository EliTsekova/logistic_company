package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Country;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link City} entity.
 *
 * These tests verify the validation rules and
 * behavior of the City model.
 */
class CityTest {

    private Validator validator;

    /**
     * Initializes the validator before each test.
     */
    @BeforeEach
    void setUp() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    /**
     * Creates a valid City object
     * used in the test methods.
     *
     * @return valid City instance
     */
    private City buildValidCity() {

        City city =
                new City();

        city.setName(
                "Sofia"
        );

        city.setCountry(
                new Country()
        );

        return city;
    }

    /**
     * Tests that a valid City object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidCity() {

        City city =
                buildValidCity();

        Set<ConstraintViolation<City>>
                violations =
                validator.validate(city);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the city name is blank.
     */
    @Test
    void shouldFailWhenNameIsBlank() {

        City city =
                buildValidCity();

        city.setName("");

        Set<ConstraintViolation<City>>
                violations =
                validator.validate(city);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the city name is shorter than 3 characters.
     */
    @Test
    void shouldFailWhenNameTooShort() {

        City city =
                buildValidCity();

        city.setName("AB");

        Set<ConstraintViolation<City>>
                violations =
                validator.validate(city);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the city name is longer than 20 characters.
     */
    @Test
    void shouldFailWhenNameTooLong() {

        City city =
                buildValidCity();

        city.setName(
                "ThisCityNameIsDefinitelyTooLong"
        );

        Set<ConstraintViolation<City>>
                violations =
                validator.validate(city);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the city name
     * is assigned correctly.
     */
    @Test
    void shouldSetNameCorrectly() {

        City city =
                buildValidCity();

        city.setName(
                "Plovdiv"
        );

        assertEquals(
                "Plovdiv",
                city.getName()
        );
    }

    /**
     * Tests that the country relation
     * is assigned correctly.
     */
    @Test
    void shouldSetCountryCorrectly() {

        Country country =
                new Country();

        City city =
                buildValidCity();

        city.setCountry(country);

        assertEquals(
                country,
                city.getCountry()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        City city =
                buildValidCity();

        assertNull(
                city.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        City city =
                buildValidCity();

        assertNull(
                city.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        City city =
                buildValidCity();

        assertNull(
                city.getUpdatedOn()
        );
    }
}