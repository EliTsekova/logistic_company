package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.ClientDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ClientDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class ClientDtoTest {

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
     * Creates a valid ClientDto object.
     *
     * @return valid ClientDto instance
     */
    private ClientDto buildValidDto() {

        ClientDto dto =
                new ClientDto();

        dto.setId(1);
        dto.setUserId(1);
        dto.setPhoneNumber("0888123456");

        dto.setUserFirstName("Ivan");
        dto.setUserLastName("Ivanov");
        dto.setUserFullName("Ivan Ivanov");
        dto.setUserEmail("ivan@test.com");
        dto.setUserUsername("ivan123");

        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidClientDto() {

        ClientDto dto =
                buildValidDto();

        Set<ConstraintViolation<ClientDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when user ID is null.
     */
    @Test
    void shouldFailWhenUserIdIsNull() {

        ClientDto dto =
                buildValidDto();

        dto.setUserId(null);

        Set<ConstraintViolation<ClientDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when phone number is blank.
     */
    @Test
    void shouldFailWhenPhoneNumberIsBlank() {

        ClientDto dto =
                buildValidDto();

        dto.setPhoneNumber("");

        Set<ConstraintViolation<ClientDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when phone number is too short.
     */
    @Test
    void shouldFailWhenPhoneNumberTooShort() {

        ClientDto dto =
                buildValidDto();

        dto.setPhoneNumber("123");

        Set<ConstraintViolation<ClientDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when phone number is too long.
     */
    @Test
    void shouldFailWhenPhoneNumberTooLong() {

        ClientDto dto =
                buildValidDto();

        dto.setPhoneNumber("1234567890123");

        Set<ConstraintViolation<ClientDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that getters and setters
     * work correctly.
     */
    @Test
    void shouldSetAndGetFieldsCorrectly() {

        Instant now =
                Instant.now();

        ClientDto dto =
                new ClientDto();

        dto.setId(5);
        dto.setUserId(2);
        dto.setPhoneNumber("0899999999");

        dto.setUserFirstName("Maria");
        dto.setUserLastName("Petrova");
        dto.setUserFullName("Maria Petrova");
        dto.setUserEmail("maria@test.com");
        dto.setUserUsername("maria123");

        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals(2, dto.getUserId());
        assertEquals("0899999999", dto.getPhoneNumber());

        assertEquals("Maria", dto.getUserFirstName());
        assertEquals("Petrova", dto.getUserLastName());
        assertEquals("Maria Petrova", dto.getUserFullName());
        assertEquals("maria@test.com", dto.getUserEmail());
        assertEquals("maria123", dto.getUserUsername());

        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        ClientDto dto1 =
                buildValidDto();

        ClientDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        ClientDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);

        assertTrue(
                result.contains("Ivan Ivanov")
        );
    }
}