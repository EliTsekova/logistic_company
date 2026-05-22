package com.team14.logistic_company.service_tests;
import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.OfficeRepository;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.OfficeNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link OfficeService} class.
 *
 * These tests verify the office service business logic,
 * DTO conversion and repository interactions.
 */
@ExtendWith(MockitoExtension.class)
class OfficeServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private OfficeService officeService;

    private Office office;
    private OfficeDto officeDto;
    private Address address;
    private City city;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        city = mock(City.class);
        lenient().when(city.getId()).thenReturn(1);

        address = mock(Address.class);
        lenient().when(address.getId()).thenReturn(1);
        lenient().when(address.getCity()).thenReturn(city);

        office = new Office();
        office.setTitle("Office Sofia");
        office.setAddress(address);

        officeDto = new OfficeDto();
        officeDto.setTitle("Office Sofia");
        officeDto.setAddressId(1);
    }

    /**
     * Tests that all offices are returned successfully.
     */
    @Test
    void shouldFindAllOffices() {

        when(officeRepository.findAll())
                .thenReturn(List.of(office));

        List<OfficeDto> result =
                officeService.findAll();

        assertEquals(1, result.size());
        assertEquals("Office Sofia", result.get(0).getTitle());
        assertEquals(1, result.get(0).getAddressId());
        assertEquals(1, result.get(0).getCityId());

        verify(officeRepository)
                .findAll();
    }

    /**
     * Tests that an office is found successfully by ID.
     */
    @Test
    void shouldFindOfficeById() {

        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        OfficeDto result =
                officeService.findById(1);

        assertNotNull(result);
        assertEquals("Office Sofia", result.getTitle());
        assertEquals(1, result.getAddressId());

        verify(officeRepository)
                .findById(1);
    }

    /**
     * Tests that OfficeNotFound is thrown
     * when the office does not exist.
     */
    @Test
    void shouldThrowWhenOfficeNotFoundById() {

        when(officeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                OfficeNotFound.class,
                () -> officeService.findById(1)
        );
    }

    /**
     * Tests that a new office is created successfully
     * when the address exists.
     */
    @Test
    void shouldCreateOffice() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(address));

        when(officeRepository.save(any(Office.class)))
                .thenReturn(office);

        OfficeDto result =
                officeService.create(officeDto);

        assertNotNull(result);
        assertEquals("Office Sofia", result.getTitle());
        assertEquals(1, result.getAddressId());

        verify(addressRepository)
                .findById(1);

        verify(officeRepository)
                .save(any(Office.class));
    }

    /**
     * Tests that AddressNotFound is thrown
     * when creating an office with invalid address ID.
     */
    @Test
    void shouldThrowWhenCreatingOfficeWithInvalidAddress() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFound.class,
                () -> officeService.create(officeDto)
        );

        verify(officeRepository, never())
                .save(any(Office.class));
    }

    /**
     * Tests that an existing office is updated successfully.
     */
    @Test
    void shouldUpdateOffice() {

        officeDto.setTitle("Updated Office");

        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(address));

        when(officeRepository.save(any(Office.class)))
                .thenReturn(office);

        OfficeDto result =
                officeService.update(1, officeDto);

        assertNotNull(result);
        assertEquals("Updated Office", result.getTitle());

        verify(officeRepository)
                .save(office);
    }

    /**
     * Tests that OfficeNotFound is thrown
     * when updating a non-existing office.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidOffice() {

        when(officeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                OfficeNotFound.class,
                () -> officeService.update(1, officeDto)
        );

        verify(officeRepository, never())
                .save(any(Office.class));
    }

    /**
     * Tests that AddressNotFound is thrown
     * when updating with an invalid address ID.
     */
    @Test
    void shouldThrowWhenUpdatingOfficeWithInvalidAddress() {

        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        when(addressRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFound.class,
                () -> officeService.update(1, officeDto)
        );

        verify(officeRepository, never())
                .save(any(Office.class));
    }

    /**
     * Tests that an office is deleted successfully.
     */
    @Test
    void shouldDeleteOffice() {

        when(officeRepository.existsById(1))
                .thenReturn(true);

        officeService.delete(1);

        verify(officeRepository)
                .deleteById(1);
    }

    /**
     * Tests that OfficeNotFound is thrown
     * when deleting a non-existing office.
     */
    @Test
    void shouldThrowWhenDeletingInvalidOffice() {

        when(officeRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                OfficeNotFound.class,
                () -> officeService.delete(1)
        );

        verify(officeRepository, never())
                .deleteById(1);
    }
}