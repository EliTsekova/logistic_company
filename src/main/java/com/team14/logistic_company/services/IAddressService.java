package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.AddressDto;

import java.util.List;

/**
 * Service interface for managing Address operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to addresses in the system.
 * </p>
 */
public interface IAddressService {

    /**
     * Retrieves all addresses from the system.
     *
     * @return list of all AddressDto objects
     */
    List<AddressDto> findAll();

    /**
     * Finds an address by its identifier.
     *
     * @param id address identifier
     * @return AddressDto object
     */
    AddressDto findById(Integer id);

    /**
     * Retrieves all addresses belonging to a specific city.
     *
     * @param cityId city identifier
     * @return list of AddressDto objects
     */
    List<AddressDto> findByCityId(Integer cityId);

    /**
     * Creates a new address in the system.
     *
     * @param addressDto data transfer object containing address data
     * @return created AddressDto object
     */
    AddressDto create(AddressDto addressDto);

    /**
     * Updates an existing address.
     *
     * @param id address identifier
     * @param addressDto updated address data
     * @return updated AddressDto object
     */
    AddressDto update(Integer id, AddressDto addressDto);

    /**
     * Deletes an address by its identifier.
     *
     * @param id address identifier
     */
    void delete(Integer id);
}