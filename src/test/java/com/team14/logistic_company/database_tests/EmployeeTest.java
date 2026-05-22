package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Employee} entity.
 *
 * These tests verify the behavior and
 * field assignments of the Employee model.
 */
class EmployeeTest {

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
     * Creates a valid Employee object
     * used in the test methods.
     *
     * @return valid Employee instance
     */
    private Employee buildValidEmployee() {

        Employee employee =
                new Employee();

        employee.setUser(
                new User()
        );

        employee.setOffice(
                new Office()
        );

        employee.setPositionType(
                PositionType.DELIVERYMAN
        );
        return employee;
    }

    /**
     * Tests that a valid Employee object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidEmployee() {

        Employee employee =
                buildValidEmployee();

        Set<ConstraintViolation<Employee>>
                violations =
                validator.validate(employee);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that the user relation
     * is assigned correctly.
     */
    @Test
    void shouldSetUserCorrectly() {

        User user = new User();

        Employee employee =
                buildValidEmployee();

        employee.setUser(user);

        assertEquals(
                user,
                employee.getUser()
        );
    }

    /**
     * Tests that the office relation
     * is assigned correctly.
     */
    @Test
    void shouldSetOfficeCorrectly() {

        Office office =
                new Office();

        Employee employee =
                buildValidEmployee();

        employee.setOffice(office);

        assertEquals(
                office,
                employee.getOffice()
        );
    }

    /**
     * Tests that the position type
     * is assigned correctly.
     */
    @Test
    void shouldSetPositionTypeCorrectly() {

        Employee employee =
                buildValidEmployee();

        employee.setPositionType(
                PositionType.COORDINATOR
        );

        assertEquals(
                PositionType.COORDINATOR,
                employee.getPositionType()
        );
    }

    /**
     * Tests that the office field
     * can be null because couriers
     * may not have a fixed office.
     */
    @Test
    void shouldAllowNullOffice() {

        Employee employee =
                buildValidEmployee();

        employee.setOffice(null);

        Set<ConstraintViolation<Employee>>
                violations =
                validator.validate(employee);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Employee employee =
                buildValidEmployee();

        assertNull(
                employee.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Employee employee =
                buildValidEmployee();

        assertNull(
                employee.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Employee employee =
                buildValidEmployee();

        assertNull(
                employee.getUpdatedOn()
        );
    }
}