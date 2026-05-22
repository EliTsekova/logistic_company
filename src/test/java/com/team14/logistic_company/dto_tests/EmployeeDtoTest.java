package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.enums.PositionType;
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
 * Unit tests for the {@link EmployeeDto} class.
 *
 * These tests verify validation rules,
 * getters, setters and DTO behavior.
 */
class EmployeeDtoTest {

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
     * Creates a valid EmployeeDto object.
     *
     * @return valid EmployeeDto instance
     */
    private EmployeeDto buildValidDto() {

        EmployeeDto dto =
                new EmployeeDto();

        dto.setId(1);
        dto.setPositionType(PositionType.COORDINATOR);

        dto.setUserId(1);
        dto.setOfficeId(2);

        dto.setUserFirstName("Ivan");
        dto.setUserLastName("Ivanov");
        dto.setUserFullName("Ivan Ivanov");
        dto.setUserEmail("ivan@test.com");
        dto.setUserUsername("ivan123");

        dto.setOfficeTitle("Office Sofia");

        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that a valid DTO
     * passes validation successfully.
     */
    @Test
    void shouldCreateValidEmployeeDto() {

        EmployeeDto dto =
                buildValidDto();

        Set<ConstraintViolation<EmployeeDto>>
                violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when position type is null.
     */
    @Test
    void shouldFailWhenPositionTypeIsNull() {

        EmployeeDto dto =
                buildValidDto();

        dto.setPositionType(null);

        Set<ConstraintViolation<EmployeeDto>>
                violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails
     * when user ID is null.
     */
    @Test
    void shouldFailWhenUserIdIsNull() {

        EmployeeDto dto =
                buildValidDto();

        dto.setUserId(null);

        Set<ConstraintViolation<EmployeeDto>>
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

        EmployeeDto dto =
                new EmployeeDto();

        dto.setId(5);
        dto.setPositionType(PositionType.DELIVERYMAN);

        dto.setUserId(3);
        dto.setOfficeId(4);

        dto.setUserFirstName("Maria");
        dto.setUserLastName("Petrova");
        dto.setUserFullName("Maria Petrova");
        dto.setUserEmail("maria@test.com");
        dto.setUserUsername("maria123");

        dto.setOfficeTitle("Office Plovdiv");

        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());
        assertEquals(PositionType.DELIVERYMAN, dto.getPositionType());

        assertEquals(3, dto.getUserId());
        assertEquals(4, dto.getOfficeId());

        assertEquals("Maria", dto.getUserFirstName());
        assertEquals("Petrova", dto.getUserLastName());
        assertEquals("Maria Petrova", dto.getUserFullName());
        assertEquals("maria@test.com", dto.getUserEmail());
        assertEquals("maria123", dto.getUserUsername());

        assertEquals("Office Plovdiv", dto.getOfficeTitle());

        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

    /**
     * Tests equals and hashCode behavior.
     */
    @Test
    void shouldSupportEqualsAndHashCode() {

        EmployeeDto dto1 =
                buildValidDto();

        EmployeeDto dto2 =
                buildValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    /**
     * Tests toString method behavior.
     */
    @Test
    void shouldGenerateToString() {

        EmployeeDto dto =
                buildValidDto();

        String result =
                dto.toString();

        assertNotNull(result);

        assertTrue(
                result.contains("Ivan Ivanov")
        );
    }
}