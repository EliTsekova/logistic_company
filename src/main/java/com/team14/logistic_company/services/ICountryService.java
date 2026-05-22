package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CountryDto;

import java.util.List;

/**
 * Service interface for managing Country operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to countries in the system.
 * </p>
 */
public interface ICountryService {

    /**
     * Retrieves all countries from the system.
     *
     * @return list of CountryDto objects
     */
    List<CountryDto> findAll();

    /**
     * Finds a country by its identifier.
     *
     * @param id country identifier
     * @return CountryDto object
     */
    CountryDto findById(Integer id);

    /**
     * Creates a new country in the system.
     *
     * @param countryDto data transfer object containing country data
     * @return created CountryDto object
     */
    CountryDto create(CountryDto countryDto);

    /**
     * Updates an existing country.
     *
     * @param id country identifier
     * @param countryDto updated country data
     * @return updated CountryDto object
     */
    CountryDto update(Integer id, CountryDto countryDto);

    /**
     * Deletes a country by its identifier.
     *
     * @param id country identifier
     */
    void delete(Integer id);
}