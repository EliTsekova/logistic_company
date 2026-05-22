package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Client;
import com.team14.logistic_company.entities.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Client} entity.
 *
 * These tests verify the validation rules and
 * behavior of the Client model.
 */
class ClientTest {

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
     * Creates a valid Client object
     * used in the test methods.
     *
     * @return valid Client instance
     */
    private Client buildValidClient() {

        Client client =
                new Client();

        client.setUser(
                new User()
        );

        client.setPhoneNumber(
                "0888123456"
        );

        return client;
    }

    /**
     * Tests that a valid Client object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidClient() {

        Client client =
                buildValidClient();

        Set<ConstraintViolation<Client>>
                violations =
                validator.validate(client);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the phone number is blank.
     */
    @Test
    void shouldFailWhenPhoneNumberIsBlank() {

        Client client =
                buildValidClient();

        client.setPhoneNumber("");

        Set<ConstraintViolation<Client>>
                violations =
                validator.validate(client);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the phone number is shorter than 10 characters.
     */
    @Test
    void shouldFailWhenPhoneNumberTooShort() {

        Client client =
                buildValidClient();

        client.setPhoneNumber(
                "12345"
        );

        Set<ConstraintViolation<Client>>
                violations =
                validator.validate(client);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the phone number is longer than 10 characters.
     */
    @Test
    void shouldFailWhenPhoneNumberTooLong() {

        Client client =
                buildValidClient();

        client.setPhoneNumber(
                "123456789012"
        );

        Set<ConstraintViolation<Client>>
                violations =
                validator.validate(client);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the phone number
     * is assigned correctly.
     */
    @Test
    void shouldSetPhoneNumberCorrectly() {

        Client client =
                buildValidClient();

        client.setPhoneNumber(
                "0899999999"
        );

        assertEquals(
                "0899999999",
                client.getPhoneNumber()
        );
    }

    /**
     * Tests that the user relation
     * is assigned correctly.
     */
    @Test
    void shouldSetUserCorrectly() {

        User user = new User();

        Client client =
                buildValidClient();

        client.setUser(user);

        assertEquals(
                user,
                client.getUser()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Client client =
                buildValidClient();

        assertNull(
                client.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Client client =
                buildValidClient();

        assertNull(
                client.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Client client =
                buildValidClient();

        assertNull(
                client.getUpdatedOn()
        );
    }
}