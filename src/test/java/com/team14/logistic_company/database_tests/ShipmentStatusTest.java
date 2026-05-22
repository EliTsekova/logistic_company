package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.entities.enums.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShipmentStatus} entity.
 *
 * These tests verify the validation rules and behavior
 * of the ShipmentStatus model.
 */
class ShipmentStatusTest {

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
     * Creates a valid ShipmentStatus object
     * used in the test methods.
     *
     * @return valid ShipmentStatus instance
     */
    private ShipmentStatus buildValidShipmentStatus() {

        ShipmentStatus shipmentStatus =
                new ShipmentStatus();

        shipmentStatus.setShipment(new Shipment());
        shipmentStatus.setStatus(Status.SUBMITTED);
        shipmentStatus.setComment(
                "Shipment was successfully submitted."
        );

        return shipmentStatus;
    }

    /**
     * Tests that a valid ShipmentStatus object
     * passes all validation checks.
     */
    @Test
    void shouldCreateValidShipmentStatus() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        Set<ConstraintViolation<ShipmentStatus>>
                violations =
                validator.validate(shipmentStatus);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that the shipment relation
     * is assigned correctly.
     */
    @Test
    void shouldSetShipmentCorrectly() {

        Shipment shipment = new Shipment();

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        shipmentStatus.setShipment(shipment);

        assertEquals(
                shipment,
                shipmentStatus.getShipment()
        );
    }

    /**
     * Tests that the shipment status
     * enum value is assigned correctly.
     */
    @Test
    void shouldSetStatusCorrectly() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        shipmentStatus.setStatus(Status.DELIVERED);

        assertEquals(
                Status.DELIVERED,
                shipmentStatus.getStatus()
        );
    }

    /**
     * Tests that the comment field
     * is assigned correctly.
     */
    @Test
    void shouldSetCommentCorrectly() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        shipmentStatus.setComment(
                "Shipment delivered successfully."
        );

        assertEquals(
                "Shipment delivered successfully.",
                shipmentStatus.getComment()
        );
    }

    /**
     * Tests that the comment field
     * can be null because it is optional.
     */
    @Test
    void shouldAllowNullComment() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        shipmentStatus.setComment(null);

        Set<ConstraintViolation<ShipmentStatus>>
                violations =
                validator.validate(shipmentStatus);

        assertTrue(violations.isEmpty());
    }

    /**
     * Tests that the entity ID is null
     * before database persistence.
     */
    @Test
    void shouldHaveNullIdBeforePersist() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        assertNull(shipmentStatus.getId());
    }

    /**
     * Tests that createdOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullCreatedOnBeforePersist() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        assertNull(
                shipmentStatus.getCreatedOn()
        );
    }

    /**
     * Tests that updatedOn is null
     * before persistence.
     */
    @Test
    void shouldHaveNullUpdatedOnBeforePersist() {

        ShipmentStatus shipmentStatus =
                buildValidShipmentStatus();

        assertNull(
                shipmentStatus.getUpdatedOn()
        );
    }
}