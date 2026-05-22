package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.ClientDto;

import java.util.List;

/**
 * Service interface for managing Client operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to clients in the system.
 * </p>
 */
public interface IClientService {

    /**
     * Retrieves all clients from the system.
     *
     * @return list of ClientDto objects
     */
    List<ClientDto> findAll();

    /**
     * Finds a client by its identifier.
     *
     * @param id client identifier
     * @return ClientDto object
     */
    ClientDto findById(Integer id);

    /**
     * Finds a client by related user identifier.
     *
     * @param userId user identifier
     * @return ClientDto object
     */
    ClientDto findByUserId(Integer userId);

    /**
     * Creates a new client in the system.
     *
     * @param clientDto data transfer object containing client data
     * @return created ClientDto object
     */
    ClientDto create(ClientDto clientDto);

    /**
     * Updates an existing client.
     *
     * @param id client identifier
     * @param clientDto updated client data
     * @return updated ClientDto object
     */
    ClientDto update(Integer id, ClientDto clientDto);

    /**
     * Deletes a client by its identifier.
     *
     * @param id client identifier
     */
    void delete(Integer id);
}