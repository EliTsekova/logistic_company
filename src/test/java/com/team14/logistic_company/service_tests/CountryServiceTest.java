package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.CountryDto;
import com.team14.logistic_company.entities.Country;
import com.team14.logistic_company.repositories.CountryRepository;
import com.team14.logistic_company.services.CountryService;
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
 * Unit tests for the {@link CountryService} class.
 *
 * These tests verify the business logic,
 * DTO conversion and repository interactions
 * of the CountryService.
 */
@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country;
    private CountryDto countryDto;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        country = new Country();
        country.setName("Bulgaria");

        countryDto = new CountryDto();
        countryDto.setName("Bulgaria");
    }

    /**
     * Tests that all countries are returned successfully.
     */
    @Test
    void shouldFindAllCountries() {

        when(countryRepository.findAll())
                .thenReturn(List.of(country));

        List<CountryDto> result =
                countryService.findAll();

        assertEquals(1, result.size());
        assertEquals("Bulgaria", result.get(0).getName());

        verify(countryRepository)
                .findAll();
    }

    /**
     * Tests that a country is found successfully by ID.
     */
    @Test
    void shouldFindCountryById() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.of(country));

        CountryDto result =
                countryService.findById(1);

        assertNotNull(result);
        assertEquals("Bulgaria", result.getName());

        verify(countryRepository)
                .findById(1);
    }

    /**
     * Tests that CountryNotFound exception
     * is thrown when the country does not exist.
     */
    @Test
    void shouldThrowWhenCountryNotFound() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFound.class,
                () -> countryService.findById(1)
        );
    }

    /**
     * Tests that a new country is created successfully.
     */
    @Test
    void shouldCreateCountry() {

        when(countryRepository.save(any(Country.class)))
                .thenReturn(country);

        CountryDto result =
                countryService.create(countryDto);

        assertNotNull(result);
        assertEquals("Bulgaria", result.getName());

        verify(countryRepository)
                .save(any(Country.class));
    }

    /**
     * Tests that an existing country is returned
     * when findOrCreateByName finds a match.
     */
    @Test
    void shouldFindExistingCountryByName() {

        when(countryRepository.findAll())
                .thenReturn(List.of(country));

        CountryDto result =
                countryService.findOrCreateByName(
                        " Bulgaria "
                );

        assertNotNull(result);
        assertEquals("Bulgaria", result.getName());

        verify(countryRepository)
                .findAll();

        verify(countryRepository, never())
                .save(any(Country.class));
    }

    /**
     * Tests that a new country is created
     * when findOrCreateByName does not find a match.
     */
    @Test
    void shouldCreateCountryWhenNameDoesNotExist() {

        when(countryRepository.findAll())
                .thenReturn(List.of());

        when(countryRepository.save(any(Country.class)))
                .thenReturn(country);

        CountryDto result =
                countryService.findOrCreateByName(
                        " Bulgaria "
                );

        assertNotNull(result);
        assertEquals("Bulgaria", result.getName());

        verify(countryRepository)
                .findAll();

        verify(countryRepository)
                .save(any(Country.class));
    }

    /**
     * Tests that an existing country
     * is updated successfully.
     */
    @Test
    void shouldUpdateCountry() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.of(country));

        when(countryRepository.save(any(Country.class)))
                .thenReturn(country);

        countryDto.setName("Germany");

        CountryDto result =
                countryService.update(1, countryDto);

        assertNotNull(result);
        assertEquals("Germany", result.getName());

        verify(countryRepository)
                .save(any(Country.class));
    }

    /**
     * Tests that CountryNotFound exception
     * is thrown when updating non-existing country.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidCountry() {

        when(countryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CountryNotFound.class,
                () -> countryService.update(1, countryDto)
        );

        verify(countryRepository, never())
                .save(any(Country.class));
    }

    /**
     * Tests that a country is deleted successfully.
     */
    @Test
    void shouldDeleteCountry() {

        when(countryRepository.existsById(1))
                .thenReturn(true);

        countryService.delete(1);

        verify(countryRepository)
                .deleteById(1);
    }

    /**
     * Tests that CountryNotFound exception
     * is thrown when deleting non-existing country.
     */
    @Test
    void shouldThrowWhenDeletingInvalidCountry() {

        when(countryRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                CountryNotFound.class,
                () -> countryService.delete(1)
        );

        verify(countryRepository, never())
                .deleteById(1);
    }
}