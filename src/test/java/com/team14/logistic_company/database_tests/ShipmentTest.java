package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.DeliveryType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Shipment} entity.
 *
 * These tests verify the validation rules and
 * business logic of the Shipment model.
 */
class ShipmentTest {

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
     * Creates a valid Shipment object
     * used in the test methods.
     *
     * @return valid Shipment instance
     */
    private Shipment buildValidShipment() {

        Shipment shipment = new Shipment();

        shipment.setEmployee(new Employee());

        shipment.setDeliveryman(new Employee());

        shipment.setRecipient(new Client());

        shipment.setRecipientName("Ivan Ivanov");

        shipment.setRecipientPhone("0888123456");

        shipment.setRecipientAddress(new Address());

        shipment.setDeliveryType(
                DeliveryType.TO_ADDRESS
        );

        shipment.setSender(new Client());

        shipment.setSenderAddress(
                new Address()
        );

        shipment.setOffice(new Office());

        shipment.setWeight(2.5);

        shipment.setPrice(
                new BigDecimal("12.50")
        );

        shipment.setUniqueId(
                "SHIPMENT123"
        );

        return shipment;
    }

    /**
     * Tests that a valid Shipment object
     * passes all validation rules.
     */
    @Test
    void shouldCreateValidShipment() {

        Shipment shipment =
                buildValidShipment();

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the recipient name is blank.
     */
    @Test
    void shouldFailWhenRecipientNameIsBlank() {

        Shipment shipment =
                buildValidShipment();

        shipment.setRecipientName("");

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the recipient phone is blank.
     */
    @Test
    void shouldFailWhenRecipientPhoneIsBlank() {

        Shipment shipment =
                buildValidShipment();

        shipment.setRecipientPhone("");

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the weight is less than or equal to zero.
     */
    @Test
    void shouldFailWhenWeightIsInvalid() {

        Shipment shipment =
                buildValidShipment();

        shipment.setWeight(0);

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the unique ID is blank.
     */
    @Test
    void shouldFailWhenUniqueIdIsBlank() {

        Shipment shipment =
                buildValidShipment();

        shipment.setUniqueId("");

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that validation fails when
     * the unique ID is too short.
     */
    @Test
    void shouldFailWhenUniqueIdTooShort() {

        Shipment shipment =
                buildValidShipment();

        shipment.setUniqueId("123");

        Set<ConstraintViolation<Shipment>>
                violations =
                validator.validate(shipment);

        assertFalse(violations.isEmpty());
    }

    /**
     * Tests that the delivery type
     * is assigned correctly.
     */
    @Test
    void shouldSetDeliveryTypeCorrectly() {

        Shipment shipment =
                buildValidShipment();

        shipment.setDeliveryType(
                DeliveryType.TO_OFFICE
        );

        assertEquals(
                DeliveryType.TO_OFFICE,
                shipment.getDeliveryType()
        );
    }

    /**
     * Tests that the shipment price
     * is assigned correctly.
     */
    @Test
    void shouldSetPriceCorrectly() {

        Shipment shipment =
                buildValidShipment();

        shipment.setPrice(
                new BigDecimal("20.00")
        );

        assertEquals(
                new BigDecimal("20.00"),
                shipment.getPrice()
        );
    }

    /**
     * Tests that the sender
     * is assigned correctly.
     */
    @Test
    void shouldSetSenderCorrectly() {

        Client sender = new Client();

        Shipment shipment =
                buildValidShipment();

        shipment.setSender(sender);

        assertEquals(
                sender,
                shipment.getSender()
        );
    }

    /**
     * Tests that the recipient
     * is assigned correctly.
     */
    @Test
    void shouldSetRecipientCorrectly() {

        Client recipient =
                new Client();

        Shipment shipment =
                buildValidShipment();

        shipment.setRecipient(recipient);

        assertEquals(
                recipient,
                shipment.getRecipient()
        );
    }

    /**
     * Tests that the entity ID is null
     * before persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        Shipment shipment =
                buildValidShipment();

        assertNull(
                shipment.getId()
        );
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        Shipment shipment =
                buildValidShipment();

        assertNull(
                shipment.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        Shipment shipment =
                buildValidShipment();

        assertNull(
                shipment.getUpdatedOn()
        );
    }
}