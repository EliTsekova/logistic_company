package com.team14.logistic_company.database_tests;

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
 * Unit tests for the {@link Country} entity.
 *
 * These tests verify the validation rules and
 * behavior of the Country model.
 */
class CountryTest {

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
     * Creates a valid Country object
     * used in the test methods.
     *
     * @return valid Country instance
     */
    private Country buildValidCountry() {

        Country country =
                new Country();

        country.setName(
                "Bulgaria"
        );

        return country;
    }

    /**
     * Tests that a valid Country object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidCountry() {

        Country country =
                buildValidCountry();

        Set<ConstraintViolation<Country>>
                violations =
                validator.validate(country);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the country name is blank.
     */
    @Test
    void shouldFailWhenNameIsBlank() {

        Country country =
                buildValidCountry();

        country.setName("");

        Set<ConstraintViolation<Country>>
                violations =
                validator.validate(country);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the country name is shorter than 3 characters.
     */
    @Test
    void shouldFailWhenNameTooShort() {

        Country country =
                buildValidCountry();

        country.setName("AB");

        Set<ConstraintViolation<Country>>
                violations =
                validator.validate(country);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the country name is longer than 50 characters.
     */
    @Test
    void shouldFailWhenNameTooLong() {

        Country country =
                buildValidCountry();

        country.setName(
                "ThisCountryNameIsDefinitelyWayTooLongForValidationRules"
        );

        Set<ConstraintViolation<Country>>
                violations =
                validator.validate(country);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the country name
     * is assigned correctly.
     */
    @Test
    void shouldSetNameCorrectly() {

        Country country =
                buildValidCountry();

        country.setName(
                "Germany"
        );

        assertEquals(
                "Germany",
                country.getName()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Country country =
                buildValidCountry();

        assertNull(
                country.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Country country =
                buildValidCountry();

        assertNull(
                country.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Country country =
                buildValidCountry();

        assertNull(
                country.getUpdatedOn()
        );
    }
}