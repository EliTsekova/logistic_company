package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.CityDto;
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
 * Unit tests for the {@link CityDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class CityDtoTest {

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
     * Creates a valid CityDto object.
     *
     * @return valid CityDto instance
     */
    private CityDto buildValidDto() {

        CityDto dto =
                new CityDto();

        dto.setId(1);
        dto.setName("Sofia");
        dto.setCountryId(1);
        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidCityDto() {

        CityDto dto =
                buildValidDto();

        Set<ConstraintViolation<CityDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when city name is blank.
     */
    @Test
    void shouldFailWhenNameIsBlank() {

        CityDto dto =
                buildValidDto();

        dto.setName("");

        Set<ConstraintViolation<CityDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when city name is too short.
     */
    @Test
    void shouldFailWhenNameTooShort() {

        CityDto dto =
                buildValidDto();

        dto.setName("AB");

        Set<ConstraintViolation<CityDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when city name is too long.
     */
    @Test
    void shouldFailWhenNameTooLong() {

        CityDto dto =
                buildValidDto();

        dto.setName(
                "ThisCityNameIsWayTooLongForValidation"
        );

        Set<ConstraintViolation<CityDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when country ID is null.
     */
    @Test
    void shouldFailWhenCountryIdIsNull() {

        CityDto dto =
                buildValidDto();

        dto.setCountryId(null);

        Set<ConstraintViolation<CityDto>>
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

        CityDto dto =
                new CityDto();

        dto.setId(5);
        dto.setName("Plovdiv");
        dto.setCountryId(2);
        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals("Plovdiv", dto.getName());
        assertEquals(2, dto.getCountryId());
        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        CityDto dto1 =
                buildValidDto();

        CityDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        CityDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Sofia"));
    }
}