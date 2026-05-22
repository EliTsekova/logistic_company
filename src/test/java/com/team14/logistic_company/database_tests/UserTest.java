package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} entity.
 *
 * These tests verify the validation rules defined in the User model,
 * such as required fields, username length, email format and role assignment.
 */
class UserTest {

    private Validator validator;

    /**
     * Initializes the Jakarta Bean Validation validator before each test.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Creates a valid User object used as a base object in the tests.
     *
     * @return a valid User instance
     */
    private User buildValidUser() {
        User user = new User();

        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setUsername("ivan123");
        user.setPassword("password123");
        user.setEmail("ivan@test.com");
        user.setRole(Role.CLIENT);

        return user;
    }

    /**
     * Tests that a completely valid user passes all validation rules.
     */
    @Test
    void shouldCreateValidUser() {
        User user = buildValidUser();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the first name is blank.
     */
    @Test
    void shouldFailWhenFirstNameIsBlank() {
        User user = buildValidUser();
        user.setFirstName("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the last name is blank.
     */
    @Test
    void shouldFailWhenLastNameIsBlank() {
        User user = buildValidUser();
        user.setLastName("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the username is shorter than 5 characters.
     */
    @Test
    void shouldFailWhenUsernameTooShort() {
        User user = buildValidUser();
        user.setUsername("abc");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the username is longer than 20 characters.
     */
    @Test
    void shouldFailWhenUsernameTooLong() {
        User user = buildValidUser();
        user.setUsername("abcdefghijklmnopqrstu");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the password is blank.
     */
    @Test
    void shouldFailWhenPasswordIsBlank() {
        User user = buildValidUser();
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the email has an invalid format.
     */
    @Test
    void shouldFailWhenEmailIsInvalid() {
        User user = buildValidUser();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when the email is blank.
     */
    @Test
    void shouldFailWhenEmailIsBlank() {
        User user = buildValidUser();
        user.setEmail("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the role field is correctly assigned.
     */
    @Test
    void shouldSetRoleCorrectly() {
        User user = buildValidUser();

        assertEquals(Role.CLIENT, user.getRole());
    }

    /**
     * Tests that the ID is null before the entity is persisted in the database.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {
        User user = buildValidUser();

        assertNull(user.getId());
    }
}