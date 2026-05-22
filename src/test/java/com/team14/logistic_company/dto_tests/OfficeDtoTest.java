package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.OfficeDto;
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
 * Unit tests for the {@link OfficeDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class OfficeDtoTest {

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
     * Creates a valid OfficeDto object.
     *
     * @return valid OfficeDto instance
     */
    private OfficeDto buildValidDto() {

        OfficeDto dto =
                new OfficeDto();

        dto.setId(1);
        dto.setTitle("Office Sofia");
        dto.setAddressId(1);
        dto.setCityId(1);

        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidOfficeDto() {

        OfficeDto dto =
                buildValidDto();

        Set<ConstraintViolation<OfficeDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when title is blank.
     */
    @Test
    void shouldFailWhenTitleIsBlank() {

        OfficeDto dto =
                buildValidDto();

        dto.setTitle("");

        Set<ConstraintViolation<OfficeDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when title is too short.
     */
    @Test
    void shouldFailWhenTitleTooShort() {

        OfficeDto dto =
                buildValidDto();

        dto.setTitle("Abc");

        Set<ConstraintViolation<OfficeDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when title is too long.
     */
    @Test
    void shouldFailWhenTitleTooLong() {

        OfficeDto dto =
                buildValidDto();

        dto.setTitle(
                "ThisOfficeTitleIsWayTooLongForValidationAndShouldFailImmediately"
        );

        Set<ConstraintViolation<OfficeDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when address ID is null.
     */
    @Test
    void shouldFailWhenAddressIdIsNull() {

        OfficeDto dto =
                buildValidDto();

        dto.setAddressId(null);

        Set<ConstraintViolation<OfficeDto>>
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

        OfficeDto dto =
                new OfficeDto();

        dto.setId(5);
        dto.setTitle("Office Plovdiv");
        dto.setAddressId(2);
        dto.setCityId(3);

        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals("Office Plovdiv", dto.getTitle());
        assertEquals(2, dto.getAddressId());
        assertEquals(3, dto.getCityId());

        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        OfficeDto dto1 =
                buildValidDto();

        OfficeDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        OfficeDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);

        assertTrue(
                result.contains("Office Sofia")
        );
    }
}