package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.OfficeDto;

import java.util.List;

/**
 * Service interface for managing Office operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to company offices in the system.
 * </p>
 */
public interface IOfficeService {

    /**
     * Retrieves all offices from the system.
     *
     * @return list of OfficeDto objects
     */
    List<OfficeDto> findAll();

    /**
     * Finds an office by its identifier.
     *
     * @param id office identifier
     * @return OfficeDto object
     */
    OfficeDto findById(Integer id);

    /**
     * Creates a new office in the system.
     *
     * @param officeDto data transfer object containing office data
     * @return created OfficeDto object
     */
    OfficeDto create(OfficeDto officeDto);

    /**
     * Updates an existing office.
     *
     * @param id office identifier
     * @param officeDto updated office data
     * @return updated OfficeDto object
     */
    OfficeDto update(Integer id, OfficeDto officeDto);

    /**
     * Deletes an office by its identifier.
     *
     * @param id office identifier
     */
    void delete(Integer id);
}