package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.AddressDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.CityRepository;
import com.team14.logistic_company.services.AddressService;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.CityNotFound;
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
 * Unit tests for the {@link AddressService} class.
 *
 * These tests verify the business logic and
 * repository interactions of the AddressService.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDto addressDto;
    private City city;

    /**
     * Initializes test objects before each test.
     */
    @BeforeEach
    void setUp() {

        city = new City();

        address = new Address();
        address.setStreet("Vitosha Blvd");
        address.setPostalCode("1000");
        address.setCity(city);

        addressDto = new AddressDto();
        addressDto.setStreet("Vitosha Blvd");
        addressDto.setPostalCode("1000");
        addressDto.setCityId(1);
    }

    /**
     * Tests that all addresses
     * are returned successfully.
     */
    @Test
    void shouldFindAllAddresses() {

        when(addressRepository.findAll())
                .thenReturn(List.of(address));

        List<AddressDto> result =
                addressService.findAll();

        assertEquals(1, result.size());

        verify(addressRepository)
                .findAll();
    }

    /**
     * Tests that an address is found
     * successfully by ID.
     */
    @Test
    void shouldFindAddressById() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(address));

        AddressDto result =
                addressService.findById(1);

        assertNotNull(result);

        verify(addressRepository)
                .findById(1);
    }

    /**
     * Tests that AddressNotFound exception
     * is thrown when the address does not exist.
     */
    @Test
    void shouldThrowWhenAddressNotFound() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFound.class,
                () -> addressService.findById(1)
        );
    }

    /**
     * Tests that addresses are found
     * successfully by city ID.
     */
    @Test
    void shouldFindAddressesByCityId() {

        when(addressRepository.findByCityId(1))
                .thenReturn(List.of(address));

        List<AddressDto> result =
                addressService.findByCityId(1);

        assertEquals(1, result.size());

        verify(addressRepository)
                .findByCityId(1);
    }

    /**
     * Tests that a new address
     * is created successfully.
     */
    @Test
    void shouldCreateAddress() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        when(addressRepository.save(any(Address.class)))
                .thenReturn(address);

        AddressDto result =
                addressService.create(addressDto);

        assertNotNull(result);

        verify(cityRepository)
                .findById(1);

        verify(addressRepository)
                .save(any(Address.class));
    }

    /**
     * Tests that CityNotFound exception
     * is thrown when creating address
     * with invalid city ID.
     */
    @Test
    void shouldThrowWhenCreatingAddressWithInvalidCity() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CityNotFound.class,
                () -> addressService.create(addressDto)
        );
    }

    /**
     * Tests that an existing address
     * is updated successfully.
     */
    @Test
    void shouldUpdateAddress() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(address));

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        when(addressRepository.save(any(Address.class)))
                .thenReturn(address);

        AddressDto result =
                addressService.update(1, addressDto);

        assertNotNull(result);

        verify(addressRepository)
                .save(any(Address.class));
    }

    /**
     * Tests that AddressNotFound exception
     * is thrown when updating
     * non-existing address.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidAddress() {

        when(addressRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFound.class,
                () -> addressService.update(1, addressDto)
        );
    }

    /**
     * Tests that an address
     * is deleted successfully.
     */
    @Test
    void shouldDeleteAddress() {

        when(addressRepository.existsById(1))
                .thenReturn(true);

        addressService.delete(1);

        verify(addressRepository)
                .deleteById(1);
    }

    /**
     * Tests that AddressNotFound exception
     * is thrown when deleting
     * non-existing address.
     */
    @Test
    void shouldThrowWhenDeletingInvalidAddress() {

        when(addressRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                AddressNotFound.class,
                () -> addressService.delete(1)
        );
    }
}