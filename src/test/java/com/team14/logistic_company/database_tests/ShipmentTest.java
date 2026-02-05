package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.*;
import com.team14.logistic_company.entities.enums.DeliveryType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ShipmentTest {

    private List<String> validate(Shipment shipment) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(shipment)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private Shipment buildValidShipment() {
        Shipment s = new Shipment();

        s.setEmployee(new Employee());
        s.setRecipient(new Client());
        s.setSender(new Client());
        s.setSenderAddress(new Address());
        s.setOffice(new Office());
        s.setDeliveryType(DeliveryType.TO_OFFICE);
        s.setRecipientAddress(null);

        s.setWeight(1.0);
        s.setPrice(new BigDecimal("5.50"));
        s.setUniqueId("UNIQUE12345"); // 10+ chars

        return s;
    }

    @Test
    void whenDataIsValid() {
        Shipment shipment = buildValidShipment();

        List<String> messages = validate(shipment);

        assertEquals(0, messages.size());
    }

    @Test
    void whenWeightIsZero() {
        Shipment shipment = buildValidShipment();
        shipment.setWeight(0);

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The weight must be greater than 0"));
    }

    @Test
    void whenWeightIsNegative() {
        Shipment shipment = buildValidShipment();
        shipment.setWeight(-2);

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The weight must be greater than 0"));
    }

    @Test
    void whenPriceIsZero() {
        Shipment shipment = buildValidShipment();
        shipment.setPrice(BigDecimal.ZERO);

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The price must be greater than 0"));
    }

    @Test
    void whenPriceIsNegative() {
        Shipment shipment = buildValidShipment();
        shipment.setPrice(new BigDecimal("-1"));

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The price must be greater than 0"));
    }

    @Test
    void whenUniqueIdIsBlank() {
        Shipment shipment = buildValidShipment();
        shipment.setUniqueId("");

        List<String> messages = validate(shipment);

        // "" нарушава @NotBlank и @Size(min=10)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("Unique ID cannot be blank!"));
        assertTrue(messages.contains("Unique id has to be between 5 and 30 characters!"));
    }

    @Test
    void whenUniqueIdIsTooShort() {
        Shipment shipment = buildValidShipment();
        shipment.setUniqueId("ABC"); // < 10

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Unique id has to be between 5 and 30 characters!"));
    }

    @Test
    void whenUniqueIdIsTooLong() {
        Shipment shipment = buildValidShipment();
        shipment.setUniqueId("THIS_UNIQUE_ID_IS_WAY_TOO_LONG_123");

        List<String> messages = validate(shipment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Unique id has to be between 5 and 30 characters!"));
    }
}
