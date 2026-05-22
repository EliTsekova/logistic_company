package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.CountryDto;
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
 * Unit tests for the {@link CountryDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class CountryDtoTest {

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
     * Creates a valid CountryDto object.
     *
     * @return valid CountryDto instance
     */
    private CountryDto buildValidDto() {

        CountryDto dto =
                new CountryDto();

        dto.setId(1);
        dto.setName("Bulgaria");
        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidCountryDto() {

        CountryDto dto =
                buildValidDto();

        Set<ConstraintViolation<CountryDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when country name is blank.
     */
    @Test
    void shouldFailWhenNameIsBlank() {

        CountryDto dto =
                buildValidDto();

        dto.setName("");

        Set<ConstraintViolation<CountryDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when country name is too short.
     */
    @Test
    void shouldFailWhenNameTooShort() {

        CountryDto dto =
                buildValidDto();

        dto.setName("AB");

        Set<ConstraintViolation<CountryDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when country name is too long.
     */
    @Test
    void shouldFailWhenNameTooLong() {

        CountryDto dto =
                buildValidDto();

        dto.setName(
                "ThisCountryNameIsWayTooLongForValidationAndShouldFailImmediately"
        );

        Set<ConstraintViolation<CountryDto>>
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

        CountryDto dto =
                new CountryDto();

        dto.setId(5);
        dto.setName("Germany");
        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals("Germany", dto.getName());
        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        CountryDto dto1 =
                buildValidDto();

        CountryDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        CountryDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);

        assertTrue(
                result.contains("Bulgaria")
        );
    }
}