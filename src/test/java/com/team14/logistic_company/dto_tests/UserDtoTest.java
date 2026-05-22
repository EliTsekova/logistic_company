package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.UserDto;
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
 * Unit tests for the {@link UserDto} class.
 *
 * These tests verify validation rules,
 * constructors, getters, setters and DTO behavior.
 */
class UserDtoTest {

    private Validator validator;

    /**
     * Initializes validator before each test.
     */
    @BeforeEach
    void setUp() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    /**
     * Creates a valid UserDto object.
     *
     * @return valid UserDto instance
     */
    private UserDto buildValidDto() {

        UserDto dto =
                new UserDto();

        dto.setId(1);

        dto.setUsername("ivan123");
        dto.setPassword("password123");

        dto.setEmail("ivan@test.com");

        dto.setRole(Role.CLIENT);

        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidUserDto() {

        UserDto dto =
                buildValidDto();

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when username is blank.
     */
    @Test
    void shouldFailWhenUsernameIsBlank() {

        UserDto dto =
                buildValidDto();

        dto.setUsername("");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when username is too short.
     */
    @Test
    void shouldFailWhenUsernameTooShort() {

        UserDto dto =
                buildValidDto();

        dto.setUsername("abc");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when username is too long.
     */
    @Test
    void shouldFailWhenUsernameTooLong() {

        UserDto dto =
                buildValidDto();

        dto.setUsername(
                "ThisUsernameIsWayTooLongForValidation"
        );

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when password is blank.
     */
    @Test
    void shouldFailWhenPasswordIsBlank() {

        UserDto dto =
                buildValidDto();

        dto.setPassword("");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when email is blank.
     */
    @Test
    void shouldFailWhenEmailIsBlank() {

        UserDto dto =
                buildValidDto();

        dto.setEmail("");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when email is invalid.
     */
    @Test
    void shouldFailWhenEmailIsInvalid() {

        UserDto dto =
                buildValidDto();

        dto.setEmail("invalid-email");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when first name is blank.
     */
    @Test
    void shouldFailWhenFirstNameIsBlank() {

        UserDto dto =
                buildValidDto();

        dto.setFirstName("");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when first name is too long.
     */
    @Test
    void shouldFailWhenFirstNameTooLong() {

        UserDto dto =
                buildValidDto();

        dto.setFirstName(
                "ThisFirstNameIsWayTooLongForValidation"
        );

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when last name is blank.
     */
    @Test
    void shouldFailWhenLastNameIsBlank() {

        UserDto dto =
                buildValidDto();

        dto.setLastName("");

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when last name is too long.
     */
    @Test
    void shouldFailWhenLastNameTooLong() {

        UserDto dto =
                buildValidDto();

        dto.setLastName(
                "ThisLastNameIsWayTooLongForValidation"
        );

        Set<ConstraintViolation<UserDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests default constructor behavior.
     */
    @Test
    void shouldCreateUsingDefaultConstructor() {

        UserDto dto =
                new UserDto();

        assertNotNull(dto);
    }

    /**
     * Tests parameterized constructor behavior.
     */
    @Test
    void shouldCreateUsingParameterizedConstructor() {

        UserDto dto =
                new UserDto(
                        "ivan123",
                        "password123",
                        "ivan@test.com",
                        Role.CLIENT
                );

        assertEquals(
                "ivan123",
                dto.getUsername()
        );

        assertEquals(
                "password123",
                dto.getPassword()
        );

        assertEquals(
                "ivan@test.com",
                dto.getEmail()
        );

        assertEquals(
                Role.CLIENT,
                dto.getRole()
        );
    }

    /**
     * Tests getters and setters
     * work correctly.
     */
    @Test
    void shouldSetAndGetFieldsCorrectly() {

        UserDto dto =
                new UserDto();

        dto.setId(5);

        dto.setUsername("maria123");
        dto.setPassword("securePass");

        dto.setEmail("maria@test.com");

        dto.setRole(Role.EMPLOYEE);

        dto.setFirstName("Maria");
        dto.setLastName("Petrova");

        assertEquals(5, dto.getId());

        assertEquals(
                "maria123",
                dto.getUsername()
        );

        assertEquals(
                "securePass",
                dto.getPassword()
        );

        assertEquals(
                "maria@test.com",
                dto.getEmail()
        );

        assertEquals(
                Role.EMPLOYEE,
                dto.getRole()
        );

        assertEquals(
                "Maria",
                dto.getFirstName()
        );

        assertEquals(
                "Petrova",
                dto.getLastName()
        );
    }

}