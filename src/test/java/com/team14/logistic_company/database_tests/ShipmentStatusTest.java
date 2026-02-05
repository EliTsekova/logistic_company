package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.entities.enums.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShipmentStatusTest {

    private List<String> validate(ShipmentStatus shipmentStatus) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(shipmentStatus)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private ShipmentStatus buildShipmentStatus(Status status) {
        ShipmentStatus ss = new ShipmentStatus();
        ss.setShipment(new Shipment());
        ss.setStatus(status);
        ss.setComment("Package accepted in office");
        return ss;
    }

    @Test
    void whenDataIsValid() {
        ShipmentStatus shipmentStatus = buildShipmentStatus(Status.SUBMITTED);

        List<String> messages = validate(shipmentStatus);

        assertEquals(0, messages.size());
    }

    @Test
    void whenStatusIsDelivered() {
        ShipmentStatus shipmentStatus = buildShipmentStatus(Status.DELIVERED);

        List<String> messages = validate(shipmentStatus);

        assertEquals(0, messages.size());
    }

    @Test
    void whenStatusIsNull_validationDoesNotFail() {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setShipment(new Shipment());

        List<String> messages = validate(shipmentStatus);

        assertEquals(0, messages.size());
    }
}
