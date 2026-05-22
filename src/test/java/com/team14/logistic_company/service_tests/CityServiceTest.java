package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Country;
import com.team14.logistic_company.repositories.CityRepository;
import com.team14.logistic_company.repositories.CountryRepository;
import com.team14.logistic_company.services.CityService;
import com.team14.logistic_company.services.exceptions.CityNotFound;
import com.team14.logistic_company.services.exceptions.CountryNotFound;
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
 * Unit tests for the {@link CityService} class.
 *
 * These tests verify the business logic, DTO conversion
 * and repository interactions of the CityService.
 */
@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    private City city;
    private CityDto cityDto;
    private Country country;

    /**
     * Initializes the common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        country = mock(Country.class);

        lenient().when(country.getId())
                .thenReturn(1);

        lenient().when(country.getName())
                .thenReturn("Bulgaria");

        city = new City();
        city.setName("Sofia");
        city.setCountry(country);

        cityDto = new CityDto();
        cityDto.setName("Sofia");
        cityDto.setCountryId(1);
    }
    /**
     * Tests that all cities are returned successfully.
     */
    @Test
    void shouldFindAllCities() {

        when(cityRepository.findAll())
                .thenReturn(List.of(city));

        List<CityDto> result =
                cityService.findAll();

        assertEquals(1, result.size());
        assertEquals("Sofia", result.get(0).getName());

        verify(cityRepository)
                .findAll();
    }

    /**
     * Tests that a city is found successfully by ID.
     */
    @Test
    void shouldFindCityById() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        CityDto result =
                cityService.findById(1);

        assertNotNull(result);
        assertEquals("Sofia", result.getName());

        verify(cityRepository)
                .findById(1);
    }

    /**
     * Tests that CityNotFound is thrown
     * when the city does not exist.
     */
    @Test
    void shouldThrowWhenCityNotFound() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CityNotFound.class,
                () -> cityService.findById(1)
        );
    }

    /**
     * Tests that cities are found successfully
     * by country ID.
     */
    @Test
    void shouldFindCitiesByCountryId() {

        when(cityRepository.findByCountryId(1))
                .thenReturn(List.of(city));

        List<CityDto> result =
                cityService.findByCountryId(1);

        assertEquals(1, result.size());
        assertEquals("Sofia", result.get(0).getName());

        verify(cityRepository)
                .findByCountryId(1);
    }

    /**
     * Tests that a new city is created successfully.
     */
    @Test
    void shouldCreateCity() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.of(country));

        when(cityRepository.save(any(City.class)))
                .thenReturn(city);

        CityDto result =
                cityService.create(cityDto);

        assertNotNull(result);
        assertEquals("Sofia", result.getName());

        verify(countryRepository)
                .findById(1);

        verify(cityRepository)
                .save(any(City.class));
    }

    /**
     * Tests that CountryNotFound is thrown
     * when creating a city with invalid country ID.
     */
    @Test
    void shouldThrowWhenCreatingCityWithInvalidCountry() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFound.class,
                () -> cityService.create(cityDto)
        );
    }

    /**
     * Tests that an existing city is returned
     * when findOrCreateByNameAndCountry finds a match.
     */
    @Test
    void shouldFindExistingCityByNameAndCountry() {

        when(cityRepository.findAll())
                .thenReturn(List.of(city));

        CityDto result =
                cityService.findOrCreateByNameAndCountry(
                        " Sofia ",
                        1
                );

        assertNotNull(result);
        assertEquals("Sofia", result.getName());

        verify(cityRepository)
                .findAll();

        verify(countryRepository, never())
                .findById(anyInt());

        verify(cityRepository, never())
                .save(any(City.class));
    }
    /**
     * Tests that a new city is created
     * when findOrCreateByNameAndCountry does not find a match.
     */
    @Test
    void shouldCreateCityWhenNameAndCountryDoNotExist() {

        when(cityRepository.findAll())
                .thenReturn(List.of());

        when(countryRepository.findById(1))
                .thenReturn(Optional.of(country));

        when(cityRepository.save(any(City.class)))
                .thenReturn(city);

        CityDto result =
                cityService.findOrCreateByNameAndCountry(
                        " Sofia ",
                        1
                );

        assertNotNull(result);
        assertEquals("Sofia", result.getName());

        verify(cityRepository)
                .findAll();

        verify(cityRepository)
                .save(any(City.class));
    }

    /**
     * Tests that an existing city is updated successfully.
     */
    @Test
    void shouldUpdateCity() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        when(countryRepository.findById(1))
                .thenReturn(Optional.of(country));

        when(cityRepository.save(any(City.class)))
                .thenReturn(city);

        CityDto result =
                cityService.update(1, cityDto);

        assertNotNull(result);
        assertEquals("Sofia", result.getName());

        verify(cityRepository)
                .save(any(City.class));
    }

    /**
     * Tests that CityNotFound is thrown
     * when updating a non-existing city.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidCity() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CityNotFound.class,
                () -> cityService.update(1, cityDto)
        );
    }

    /**
     * Tests that CountryNotFound is thrown
     * when updating a city with invalid country ID.
     */
    @Test
    void shouldThrowWhenUpdatingCityWithInvalidCountry() {

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        when(countryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFound.class,
                () -> cityService.update(1, cityDto)
        );
    }

    /**
     * Tests that a city is deleted successfully.
     */
    @Test
    void shouldDeleteCity() {

        when(cityRepository.existsById(1))
                .thenReturn(true);

        cityService.delete(1);

        verify(cityRepository)
                .deleteById(1);
    }

    /**
     * Tests that CityNotFound is thrown
     * when deleting a non-existing city.
     */
    @Test
    void shouldThrowWhenDeletingInvalidCity() {

        when(cityRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                CityNotFound.class,
                () -> cityService.delete(1)
        );
    }
}