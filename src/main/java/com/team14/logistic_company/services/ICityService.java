package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CityDto;

import java.util.List;

/**
 * Service interface for managing City operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to cities in the system.
 * </p>
 */
public interface ICityService {

    /**
     * Retrieves all cities from the system.
     *
     * @return list of CityDto objects
     */
    List<CityDto> findAll();

    /**
     * Finds a city by its identifier.
     *
     * @param id city identifier
     * @return CityDto object
     */
    CityDto findById(Integer id);

    /**
     * Retrieves all cities belonging to a specific country.
     *
     * @param countryId country identifier
     * @return list of CityDto objects
     */
    List<CityDto> findByCountryId(Integer countryId);

    /**
     * Creates a new city in the system.
     *
     * @param cityDto data transfer object containing city data
     * @return created CityDto object
     */
    CityDto create(CityDto cityDto);

    /**
     * Updates an existing city.
     *
     * @param id city identifier
     * @param cityDto updated city data
     * @return updated CityDto object
     */
    CityDto update(Integer id, CityDto cityDto);

    /**
     * Deletes a city by its identifier.
     *
     * @param id city identifier
     */
    void delete(Integer id);
}