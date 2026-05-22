package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.AddressDto;
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
 * Unit tests for the {@link AddressDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class AddressDtoTest {

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
     * Creates a valid AddressDto object.
     *
     * @return valid AddressDto instance
     */
    private AddressDto buildValidDto() {

        AddressDto dto =
                new AddressDto();

        dto.setId(1);
        dto.setCityId(1);
        dto.setStreet("Vitosha Boulevard");
        dto.setPostalCode("1000");
        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidAddressDto() {

        AddressDto dto =
                buildValidDto();

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when city ID is null.
     */
    @Test
    void shouldFailWhenCityIdIsNull() {

        AddressDto dto =
                buildValidDto();

        dto.setCityId(null);

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when street is blank.
     */
    @Test
    void shouldFailWhenStreetIsBlank() {

        AddressDto dto =
                buildValidDto();

        dto.setStreet("");

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when street is too short.
     */
    @Test
    void shouldFailWhenStreetTooShort() {

        AddressDto dto =
                buildValidDto();

        dto.setStreet("abc");

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when street is too long.
     */
    @Test
    void shouldFailWhenStreetTooLong() {

        AddressDto dto =
                buildValidDto();

        dto.setStreet(
                "ThisStreetNameIsWayTooLongForValidationAndShouldFailBecauseItHasMoreThanOneHundredCharactersTotal1234567890"
        );

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when postal code is blank.
     */
    @Test
    void shouldFailWhenPostalCodeIsBlank() {

        AddressDto dto =
                buildValidDto();

        dto.setPostalCode("");

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when postal code is too short.
     */
    @Test
    void shouldFailWhenPostalCodeTooShort() {

        AddressDto dto =
                buildValidDto();

        dto.setPostalCode("12");

        Set<ConstraintViolation<AddressDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when postal code is too long.
     */
    @Test
    void shouldFailWhenPostalCodeTooLong() {

        AddressDto dto =
                buildValidDto();

        dto.setPostalCode("123456789012345");

        Set<ConstraintViolation<AddressDto>>
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

        AddressDto dto =
                new AddressDto();

        dto.setId(5);
        dto.setCityId(2);
        dto.setStreet("Main Street");
        dto.setPostalCode("4000");
        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals(2, dto.getCityId());
        assertEquals("Main Street", dto.getStreet());
        assertEquals("4000", dto.getPostalCode());
        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        AddressDto dto1 =
                buildValidDto();

        AddressDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        AddressDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Vitosha Boulevard"));
    }
}