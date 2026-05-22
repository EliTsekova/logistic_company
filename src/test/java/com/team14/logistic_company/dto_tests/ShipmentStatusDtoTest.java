package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.ShipmentStatusDto;
import com.team14.logistic_company.entities.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShipmentStatusDto} class.
 *
 * These tests verify getters, setters,
 * equals, hashCode and DTO behavior.
 */
class ShipmentStatusDtoTest {

    /**
     * Creates a valid ShipmentStatusDto object.
     *
     * @return valid ShipmentStatusDto instance
     */
    private ShipmentStatusDto buildValidDto() {

        ShipmentStatusDto dto =
                new ShipmentStatusDto();

        dto.setId(1);
        dto.setShipmentId(10);

        dto.setStatus(Status.IN_TRANSIT);

        dto.setComment(
                "Shipment is currently moving between offices."
        );

        dto.setCreatedOn(Instant.now());
        dto.setUpdatedOn(Instant.now());

        return dto;
    }

    /**
     * Tests that getters and setters
     * work correctly.
     */
    @Test
    void shouldSetAndGetFieldsCorrectly() {

        Instant now =
                Instant.now();

        ShipmentStatusDto dto =
                new ShipmentStatusDto();

        dto.setId(5);
        dto.setShipmentId(20);

        dto.setStatus(Status.DELIVERED);

        dto.setComment(
                "Shipment delivered successfully."
        );

        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(5, dto.getId());

        assertEquals(20, dto.getShipmentId());

        assertEquals(
                Status.DELIVERED,
                dto.getStatus()
        );

        assertEquals(
                "Shipment delivered successfully.",
                dto.getComment()
        );

        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }




}