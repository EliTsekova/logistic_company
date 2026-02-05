package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private List<String> validate(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(user)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private User buildValidUser() {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setUsername("ivanov123");
        user.setPassword("password");
        user.setEmail("ivan@test.com");
        user.setRole(Role.CLIENT);
        return user;
    }

    @Test
    void whenDataIsValid() {
        User user = buildValidUser();

        List<String> messages = validate(user);

        assertEquals(0, messages.size());
    }

    // -------- First Name --------

    @Test
    void whenFirstNameIsBlank() {
        User user = buildValidUser();
        user.setFirstName("");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name cannot be blank!"));
    }

    @Test
    void whenFirstNameIsTooLong() {
        User user = buildValidUser();
        user.setFirstName("ThisFirstNameIsWayTooLong");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("First name has to be up to 20 characters!"));
    }

    // -------- Last Name --------

    @Test
    void whenLastNameIsBlank() {
        User user = buildValidUser();
        user.setLastName("");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name cannot be blank!"));
    }

    @Test
    void whenLastNameIsTooLong() {
        User user = buildValidUser();
        user.setLastName("ThisLastNameIsWayTooLong");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Last name has to be up to 20 characters!"));
    }

    // -------- Username --------

    @Test
    void whenUsernameIsBlank() {
        User user = buildValidUser();
        user.setUsername("");

        List<String> messages = validate(user);

        assertEquals(2, messages.size());
        assertTrue(messages.contains("Username cannot be blank!"));
        assertTrue(messages.contains("Username has to be between 5 and 20 characters!"));
    }

    @Test
    void whenUsernameIsTooShort() {
        User user = buildValidUser();
        user.setUsername("usr");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Username has to be between 5 and 20 characters!"));
    }

    // -------- Password --------

    @Test
    void whenPasswordIsBlank() {
        User user = buildValidUser();
        user.setPassword("");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Password cannot be blank!"));
    }

    // -------- Email --------

    @Test
    void whenEmailIsBlank() {
        User user = buildValidUser();
        user.setEmail("");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The email address cannot be blank!"));
    }

    @Test
    void whenEmailIsInvalid() {
        User user = buildValidUser();
        user.setEmail("invalid-email");

        List<String> messages = validate(user);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Invalid email address. Please enter a proper email address!"));
    }

    // -------- Role --------

    @Test
    void whenRoleIsNull_validationDoesNotFail() {
        User user = buildValidUser();
        user.setRole(null);

        List<String> messages = validate(user);

        assertEquals(0, messages.size());
    }
}
