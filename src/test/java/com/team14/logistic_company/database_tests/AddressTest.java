package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Address} entity.
 *
 * These tests verify the validation rules and
 * behavior of the Address model.
 */
class AddressTest {

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
     * Creates a valid Address object
     * used in the test methods.
     *
     * @return valid Address instance
     */
    private Address buildValidAddress() {

        Address address =
                new Address();

        address.setCity(
                new City()
        );

        address.setStreet(
                "Vitosha Blvd"
        );

        address.setPostalCode(
                "1000"
        );

        return address;
    }

    /**
     * Tests that a valid Address object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidAddress() {

        Address address =
                buildValidAddress();

        Set<ConstraintViolation<Address>>
                violations =
                validator.validate(address);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the street field is blank.
     */
    @Test
    void shouldFailWhenStreetIsBlank() {

        Address address =
                buildValidAddress();

        address.setStreet("");

        Set<ConstraintViolation<Address>>
                violations =
                validator.validate(address);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the street name is shorter than 5 characters.
     */
    @Test
    void shouldFailWhenStreetTooShort() {

        Address address =
                buildValidAddress();

        address.setStreet("abc");

        Set<ConstraintViolation<Address>>
                violations =
                validator.validate(address);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the street name is longer than 20 characters.
     */
    @Test
    void shouldFailWhenStreetTooLong() {

        Address address =
                buildValidAddress();

        address.setStreet(
                "ThisStreetNameIsDefinitelyTooLong"
        );

        Set<ConstraintViolation<Address>>
                violations =
                validator.validate(address);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the postal code is blank.
     */
    @Test
    void shouldFailWhenPostalCodeIsBlank() {

        Address address =
                buildValidAddress();

        address.setPostalCode("");

        Set<ConstraintViolation<Address>>
                violations =
                validator.validate(address);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the street field
     * is assigned correctly.
     */
    @Test
    void shouldSetStreetCorrectly() {

        Address address =
                buildValidAddress();

        address.setStreet(
                "Tsarigradsko Shose"
        );

        assertEquals(
                "Tsarigradsko Shose",
                address.getStreet()
        );
    }

    /**
     * Tests that the postal code field
     * is assigned correctly.
     */
    @Test
    void shouldSetPostalCodeCorrectly() {

        Address address =
                buildValidAddress();

        address.setPostalCode(
                "4000"
        );

        assertEquals(
                "4000",
                address.getPostalCode()
        );
    }

    /**
     * Tests that the city relation
     * is assigned correctly.
     */
    @Test
    void shouldSetCityCorrectly() {

        City city =
                new City();

        Address address =
                buildValidAddress();

        address.setCity(city);

        assertEquals(
                city,
                address.getCity()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Address address =
                buildValidAddress();

        assertNull(
                address.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Address address =
                buildValidAddress();

        assertNull(
                address.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Address address =
                buildValidAddress();

        assertNull(
                address.getUpdatedOn()
        );
    }
}