package com.team14.logistic_company.database_tests;


import com.team14.logistic_company.entities.LogisticCompany;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LogisticCompany} entity.
 *
 * These tests verify the validation rules and
 * behavior of the LogisticCompany model.
 */
class LogisticCompanyTest {

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
     * Creates a valid LogisticCompany object
     * used in the test methods.
     *
     * @return valid LogisticCompany instance
     */
    private LogisticCompany buildValidCompany() {

        LogisticCompany company =
                new LogisticCompany();

        company.setName(
                "Speed Logistics"
        );

        company.setUic(
                "123456789"
        );

        company.setPhone(
                "0888123456"
        );

        company.setEmail(
                "office@speedlogistics.com"
        );

        company.setAddress(
                "Sofia, Bulgaria"
        );

        return company;
    }

    /**
     * Tests that a valid LogisticCompany object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidCompany() {

        LogisticCompany company =
                buildValidCompany();

        Set<ConstraintViolation<LogisticCompany>>
                violations =
                validator.validate(company);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the company name is blank.
     */
    @Test
    void shouldFailWhenNameIsBlank() {

        LogisticCompany company =
                buildValidCompany();

        company.setName("");

        Set<ConstraintViolation<LogisticCompany>>
                violations =
                validator.validate(company);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the UIC field is blank.
     */
    @Test
    void shouldFailWhenUicIsBlank() {

        LogisticCompany company =
                buildValidCompany();

        company.setUic("");

        Set<ConstraintViolation<LogisticCompany>>
                violations =
                validator.validate(company);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the company name
     * is assigned correctly.
     */
    @Test
    void shouldSetNameCorrectly() {

        LogisticCompany company =
                buildValidCompany();

        company.setName(
                "Express Delivery"
        );

        assertEquals(
                "Express Delivery",
                company.getName()
        );
    }

    /**
     * Tests that the UIC field
     * is assigned correctly.
     */
    @Test
    void shouldSetUicCorrectly() {

        LogisticCompany company =
                buildValidCompany();

        company.setUic(
                "987654321"
        );

        assertEquals(
                "987654321",
                company.getUic()
        );
    }

    /**
     * Tests that the phone field
     * is assigned correctly.
     */
    @Test
    void shouldSetPhoneCorrectly() {

        LogisticCompany company =
                buildValidCompany();

        company.setPhone(
                "0899999999"
        );

        assertEquals(
                "0899999999",
                company.getPhone()
        );
    }

    /**
     * Tests that the email field
     * is assigned correctly.
     */
    @Test
    void shouldSetEmailCorrectly() {

        LogisticCompany company =
                buildValidCompany();

        company.setEmail(
                "new@company.com"
        );

        assertEquals(
                "new@company.com",
                company.getEmail()
        );
    }

    /**
     * Tests that the address field
     * is assigned correctly.
     */
    @Test
    void shouldSetAddressCorrectly() {

        LogisticCompany company =
                buildValidCompany();

        company.setAddress(
                "Plovdiv, Bulgaria"
        );

        assertEquals(
                "Plovdiv, Bulgaria",
                company.getAddress()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        LogisticCompany company =
                buildValidCompany();

        assertNull(
                company.getId()
        );
    }
}