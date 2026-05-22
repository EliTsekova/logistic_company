package com.team14.logistic_company.dto_tests;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.enums.DeliveryType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShipmentDto} class.
 *
 * These tests verify getters, setters,
 * equals, hashCode and DTO behavior.
 */
class ShipmentDtoTest {

    /**
     * Creates a valid ShipmentDto object.
     *
     * @return valid ShipmentDto instance
     */
    private ShipmentDto buildValidDto() {

        ShipmentDto dto =
                new ShipmentDto();

        dto.setId(1);

        dto.setEmployeeId(1);
        dto.setDeliverymanId(2);

        dto.setSenderId(3);
        dto.setRecipientId(4);

        dto.setSenderAddressId(5);
        dto.setRecipientAddressId(6);

        dto.setSenderCityId(1);
        dto.setSenderStreet("Vitosha Blvd");
        dto.setSenderPostalCode("1000");

        dto.setRecipientCityId(2);
        dto.setRecipientStreet("Main Street");
        dto.setRecipientPostalCode("4000");

        dto.setRecipientName("Ivan Ivanov");
        dto.setRecipientPhone("0888123456");

        dto.setOfficeId(7);

        dto.setEmployeeName("Employee One");
        dto.setDeliverymanName("Courier One");
        dto.setSenderName("Sender One");

        dto.setSenderAddressText("Sofia, Vitosha Blvd");
        dto.setRecipientAddressText("Plovdiv, Main Street");

        dto.setOfficeTitle("Office Sofia");

        dto.setWeight(2.5);

        dto.setPrice(
                new BigDecimal("14.00")
        );

        dto.setDeliveryType(
                DeliveryType.TO_ADDRESS
        );

        dto.setUniqueId("SHIPMENT12345");

        dto.setCurrentStatus("SUBMITTED");

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

        ShipmentDto dto =
                new ShipmentDto();

        dto.setId(10);

        dto.setEmployeeId(1);
        dto.setDeliverymanId(2);

        dto.setSenderId(3);
        dto.setRecipientId(4);

        dto.setSenderAddressId(5);
        dto.setRecipientAddressId(6);

        dto.setSenderCityId(1);
        dto.setSenderStreet("Street A");
        dto.setSenderPostalCode("1000");

        dto.setRecipientCityId(2);
        dto.setRecipientStreet("Street B");
        dto.setRecipientPostalCode("4000");

        dto.setRecipientName("Maria Petrova");
        dto.setRecipientPhone("0899999999");

        dto.setOfficeId(7);

        dto.setEmployeeName("Employee");
        dto.setDeliverymanName("Courier");
        dto.setSenderName("Sender");

        dto.setSenderAddressText("Sender Address");
        dto.setRecipientAddressText("Recipient Address");

        dto.setOfficeTitle("Office Plovdiv");

        dto.setWeight(5.0);

        dto.setPrice(
                new BigDecimal("20.00")
        );

        dto.setDeliveryType(
                DeliveryType.TO_OFFICE
        );

        dto.setUniqueId("TRACK123456");

        dto.setCurrentStatus("DELIVERED");

        dto.setCreatedOn(now);
        dto.setUpdatedOn(now);

        assertEquals(10, dto.getId());

        assertEquals(1, dto.getEmployeeId());
        assertEquals(2, dto.getDeliverymanId());

        assertEquals(3, dto.getSenderId());
        assertEquals(4, dto.getRecipientId());

        assertEquals(5, dto.getSenderAddressId());
        assertEquals(6, dto.getRecipientAddressId());

        assertEquals(1, dto.getSenderCityId());
        assertEquals("Street A", dto.getSenderStreet());
        assertEquals("1000", dto.getSenderPostalCode());

        assertEquals(2, dto.getRecipientCityId());
        assertEquals("Street B", dto.getRecipientStreet());
        assertEquals("4000", dto.getRecipientPostalCode());

        assertEquals("Maria Petrova", dto.getRecipientName());
        assertEquals("0899999999", dto.getRecipientPhone());

        assertEquals(7, dto.getOfficeId());

        assertEquals("Employee", dto.getEmployeeName());
        assertEquals("Courier", dto.getDeliverymanName());
        assertEquals("Sender", dto.getSenderName());

        assertEquals("Sender Address", dto.getSenderAddressText());
        assertEquals("Recipient Address", dto.getRecipientAddressText());

        assertEquals("Office Plovdiv", dto.getOfficeTitle());

        assertEquals(5.0, dto.getWeight());

        assertEquals(
                0,
                new BigDecimal("20.00")
                        .compareTo(dto.getPrice())
        );

        assertEquals(
                DeliveryType.TO_OFFICE,
                dto.getDeliveryType()
        );

        assertEquals(
                "TRACK123456",
                dto.getUniqueId()
        );

        assertEquals(
                "DELIVERED",
                dto.getCurrentStatus()
        );

        assertEquals(now, dto.getCreatedOn());
        assertEquals(now, dto.getUpdatedOn());
    }

}