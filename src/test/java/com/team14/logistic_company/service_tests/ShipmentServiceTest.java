package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.*;
import com.team14.logistic_company.entities.enums.DeliveryType;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.repositories.*;
import com.team14.logistic_company.services.ShipmentService;
import com.team14.logistic_company.services.exceptions.ShipmentNotFound;
import com.team14.logistic_company.services.exceptions.UnauthorizedAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ShipmentService} class.
 *
 * These tests verify shipment business logic,
 * authentication checks and repository interactions.
 */
@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentStatusRepository shipmentStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment shipment;
    private Employee employee;
    private Client client;
    private Office office;
    private Address address;
    private User user;
    private Authentication authentication;

    /**
     * Helper method for creating authorities collection.
     *
     * @param role security role
     * @return collection of authorities
     */
    private Collection<? extends GrantedAuthority> authorities(String role) {

        return List.of(
                new SimpleGrantedAuthority(role)
        );
    }

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        user = new User();
        user.setUsername("employee1");
        user.setRole(Role.EMPLOYEE);

        employee = new Employee();
        employee.setUser(user);
        employee.setPositionType(PositionType.COORDINATOR);

        client = new Client();
        client.setUser(user);

        office = new Office();
        office.setTitle("Office Sofia");

        address = new Address();
        address.setStreet("Vitosha Blvd");
        address.setPostalCode("1000");

        shipment = new Shipment();

        shipment.setEmployee(employee);
        shipment.setDeliveryman(employee);
        shipment.setSender(client);
        shipment.setRecipient(client);
        shipment.setRecipientName("Ivan Ivanov");
        shipment.setRecipientPhone("0888123456");
        shipment.setSenderAddress(address);
        shipment.setRecipientAddress(address);
        shipment.setOffice(office);
        shipment.setWeight(2.5);
        shipment.setUniqueId("SHIPMENT12345");
        shipment.setDeliveryType(DeliveryType.TO_ADDRESS);

        authentication = mock(Authentication.class);

        lenient().when(authentication.getName())
                .thenReturn("employee1");

        lenient().when(authentication.getAuthorities())
                .thenAnswer(invocation ->
                        authorities("EMPLOYEE")
                );

        lenient().when(employeeRepository.findAll())
                .thenReturn(List.of(employee));
    }

    /**
     * Tests that all shipments are returned
     * for employee users.
     */
    @Test
    void shouldGetAllShipmentsForEmployee() {

        when(shipmentRepository.findAllByOrderByCreatedOnDesc())
                .thenReturn(List.of(shipment));

        List<Shipment> result =
                shipmentService.getAllShipmentsForEmployee(authentication);

        assertEquals(1, result.size());

        verify(shipmentRepository)
                .findAllByOrderByCreatedOnDesc();
    }

    /**
     * Tests that UnauthorizedAccess is thrown
     * when non-employee tries to access shipments.
     */
    @Test
    void shouldThrowWhenUnauthorizedAccessToShipments() {

        when(authentication.getAuthorities())
                .thenAnswer(invocation ->
                        authorities("CLIENT")
                );

        assertThrows(
                UnauthorizedAccess.class,
                () -> shipmentService.getAllShipmentsForEmployee(authentication)
        );
    }

    /**
     * Tests that shipment history is returned successfully.
     */
    @Test
    void shouldGetShipmentHistory() {

        ShipmentStatus status =
                new ShipmentStatus();

        status.setStatus(Status.SUBMITTED);

        when(shipmentRepository.findByUniqueId("SHIPMENT12345"))
                .thenReturn(Optional.of(shipment));

        when(shipmentStatusRepository
                .findByShipment_IdOrderByCreatedOnDesc(isNull()))
                .thenReturn(List.of(status));

        List<ShipmentStatus> result =
                shipmentService.getShipmentHistory("SHIPMENT12345");

        assertEquals(1, result.size());

        verify(shipmentRepository)
                .findByUniqueId("SHIPMENT12345");
    }

    /**
     * Tests that ShipmentNotFound is thrown
     * when shipment history does not exist.
     */
    @Test
    void shouldThrowWhenShipmentHistoryNotFound() {

        when(shipmentRepository.findByUniqueId("INVALID"))
                .thenReturn(Optional.empty());

        assertThrows(
                ShipmentNotFound.class,
                () -> shipmentService.getShipmentHistory("INVALID")
        );
    }

    /**
     * Tests that shipment status is updated successfully.
     */
    @Test
    void shouldUpdateShipmentStatus() {

        when(shipmentRepository.findById(1))
                .thenReturn(Optional.of(shipment));

        Shipment result =
                shipmentService.updateShipmentStatus(
                        1,
                        Status.SHIPPED,
                        authentication
                );

        assertNotNull(result);

        verify(shipmentStatusRepository)
                .save(any(ShipmentStatus.class));
    }

    /**
     * Tests that ShipmentNotFound is thrown
     * when updating non-existing shipment.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidShipment() {

        when(shipmentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ShipmentNotFound.class,
                () -> shipmentService.updateShipmentStatus(
                        1,
                        Status.SHIPPED,
                        authentication
                )
        );
    }

    /**
     * Tests that shipment can be deleted successfully.
     */
    @Test
    void shouldDeleteShipment() {

        when(shipmentRepository.existsById(1))
                .thenReturn(true);

        shipmentService.deleteShipment(1, authentication);

        verify(shipmentRepository)
                .deleteById(1);
    }

    /**
     * Tests that ShipmentNotFound is thrown
     * when deleting non-existing shipment.
     */
    @Test
    void shouldThrowWhenDeletingInvalidShipment() {

        when(shipmentRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                ShipmentNotFound.class,
                () -> shipmentService.deleteShipment(
                        1,
                        authentication
                )
        );
    }

    /**
     * Tests that shipment DTO conversion works correctly.
     */
    @Test
    void shouldConvertShipmentToDto() {

        ShipmentStatus status =
                new ShipmentStatus();

        status.setStatus(Status.SUBMITTED);

        when(shipmentStatusRepository
                .findByShipment_IdOrderByCreatedOnDesc(isNull()))
                .thenReturn(List.of(status));

        ShipmentDto dto =
                shipmentService.toDtoWithCurrentStatus(shipment);

        assertNotNull(dto);

        assertEquals(
                "Ivan Ivanov",
                dto.getRecipientName()
        );

        assertEquals(
                "SUBMITTED",
                dto.getCurrentStatus()
        );
    }

    /**
     * Tests that shipment price calculation works correctly
     * for office delivery.
     */
    @Test
    void shouldCalculatePriceForOfficeDelivery() {

        BigDecimal result =
                shipmentService.calculatePrice(
                        2.0,
                        DeliveryType.TO_OFFICE
                );

        assertEquals(
                0,
                BigDecimal.valueOf(9.0).compareTo(result)
        );
    }

    /**
     * Tests that shipment price calculation works correctly
     * for address delivery.
     */
    @Test
    void shouldCalculatePriceForAddressDelivery() {

        BigDecimal result =
                shipmentService.calculatePrice(
                        2.0,
                        DeliveryType.TO_ADDRESS
                );

        assertEquals(
                0,
                new BigDecimal("14.00").compareTo(result)
        );
    }

    /**
     * Tests that invalid weight throws exception
     * during price calculation.
     */
    @Test
    void shouldThrowWhenCalculatingPriceWithInvalidWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> shipmentService.calculatePrice(
                        0,
                        DeliveryType.TO_ADDRESS
                )
        );
    }

    /**
     * Tests that revenue is returned correctly.
     */
    @Test
    void shouldGetRevenueBetweenDates() {

        when(shipmentRepository.sumRevenueBetween(any(), any()))
                .thenReturn(BigDecimal.valueOf(250));

        BigDecimal result =
                shipmentService.getRevenueBetween(
                        Instant.now().minusSeconds(3600),
                        Instant.now(),
                        authentication
                );

        assertEquals(
                BigDecimal.valueOf(250),
                result
        );
    }
}