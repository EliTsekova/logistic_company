package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.entities.Client;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.services.exceptions.ClientNotFound;
import com.team14.logistic_company.repositories.ClientRepository;
import com.team14.logistic_company.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation responsible for client management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Retrieving all clients</li>
 *     <li>Finding clients by id or user id</li>
 *     <li>Creating new client profiles</li>
 *     <li>Updating client information</li>
 *     <li>Deleting clients</li>
 *     <li>Converting Client entities to DTO objects</li>
 * </ul>
 *
 * <p>
 * The service communicates with client and user repositories
 * and validates required user information before creating a client.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all clients from the system.
     *
     * @return list of all clients as DTO objects
     */
    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds a client by its identifier.
     *
     * @param id client identifier
     * @return client DTO object
     * @throws ClientNotFound if client does not exist
     */
    @Override
    public ClientDto findById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFound(id));
        return convertToDto(client);
    }

    /**
     * Finds a client profile by related user identifier.
     *
     * @param userId user identifier
     * @return client DTO object connected to the given user
     * @throws ClientNotFound if client for the given user does not exist
     */
    @Override
    public ClientDto findByUserId(Integer userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ClientNotFound("Client not found for user id: " + userId));
        return convertToDto(client);
    }

    /**
     * Creates a new client profile for an existing user.
     *
     * @param clientDto DTO containing client information
     * @return created client as DTO object
     * @throws RuntimeException if user does not exist
     */
    @Override
    public ClientDto create(ClientDto clientDto) {
        User user = userRepository.findById(clientDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + clientDto.getUserId()));

        Client client = new Client();
        client.setUser(user);
        client.setPhoneNumber(clientDto.getPhoneNumber());

        Client saved = clientRepository.save(client);
        return convertToDto(saved);
    }

    /**
     * Updates existing client information.
     *
     * @param id client identifier
     * @param clientDto DTO containing updated client data
     * @return updated client as DTO object
     * @throws ClientNotFound if client does not exist
     */
    @Override
    public ClientDto update(Integer id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFound(id));

        client.setPhoneNumber(clientDto.getPhoneNumber());

        Client updated = clientRepository.save(client);
        return convertToDto(updated);
    }

    /**
     * Deletes a client from the system.
     *
     * @param id client identifier
     * @throws ClientNotFound if client does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFound(id);
        }
        clientRepository.deleteById(id);
    }

    /**
     * Converts Client entity to ClientDto object.
     *
     * <p>
     * The method also copies related user information,
     * such as name, email and username.
     * </p>
     *
     * @param client client entity
     * @return converted DTO object containing client and user data
     */
    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setUserId(client.getUser().getId());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setCreatedOn(client.getCreatedOn());
        dto.setUpdatedOn(client.getUpdatedOn());

        User user = client.getUser();
        dto.setUserFirstName(user.getFirstName());
        dto.setUserLastName(user.getLastName());
        dto.setUserFullName(user.getFirstName() + " " + user.getLastName());
        dto.setUserEmail(user.getEmail());
        dto.setUserUsername(user.getUsername());

        return dto;
    }
}