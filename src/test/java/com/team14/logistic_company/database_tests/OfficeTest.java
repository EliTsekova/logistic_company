package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.Office;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Office} entity.
 *
 * These tests verify the validation rules and
 * behavior of the Office model.
 */
class OfficeTest {

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
     * Creates a valid Office object
     * used in the test methods.
     *
     * @return valid Office instance
     */
    private Office buildValidOffice() {

        Office office = new Office();

        office.setTitle("Office Sofia");

        office.setAddress(
                new Address()
        );

        return office;
    }

    /**
     * Tests that a valid Office object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidOffice() {

        Office office =
                buildValidOffice();

        Set<ConstraintViolation<Office>>
                violations =
                validator.validate(office);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the title is blank.
     */
    @Test
    void shouldFailWhenTitleIsBlank() {

        Office office =
                buildValidOffice();

        office.setTitle("");

        Set<ConstraintViolation<Office>>
                violations =
                validator.validate(office);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the title is shorter than 5 characters.
     */
    @Test
    void shouldFailWhenTitleTooShort() {

        Office office =
                buildValidOffice();

        office.setTitle("abc");

        Set<ConstraintViolation<Office>>
                violations =
                validator.validate(office);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the title is longer than 20 characters.
     */
    @Test
    void shouldFailWhenTitleTooLong() {

        Office office =
                buildValidOffice();

        office.setTitle(
                "ThisOfficeTitleIsWayTooLong"
        );

        Set<ConstraintViolation<Office>>
                violations =
                validator.validate(office);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the address
     * is assigned correctly.
     */
    @Test
    void shouldSetAddressCorrectly() {

        Address address =
                new Address();

        Office office =
                buildValidOffice();

        office.setAddress(address);

        assertEquals(
                address,
                office.getAddress()
        );
    }

    /**
     * Tests that the title
     * is assigned correctly.
     */
    @Test
    void shouldSetTitleCorrectly() {

        Office office =
                buildValidOffice();

        office.setTitle("Office Plovdiv");

        assertEquals(
                "Office Plovdiv",
                office.getTitle()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Office office =
                buildValidOffice();

        assertNull(
                office.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Office office =
                buildValidOffice();

        assertNull(
                office.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Office office =
                buildValidOffice();

        assertNull(
                office.getUpdatedOn()
        );
    }
}