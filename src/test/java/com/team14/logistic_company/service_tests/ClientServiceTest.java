package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.entities.Client;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.repositories.ClientRepository;
import com.team14.logistic_company.repositories.UserRepository;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.exceptions.ClientNotFound;
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
 * Unit tests for the {@link ClientService} class.
 *
 * These tests verify the business logic, DTO conversion
 * and repository interactions of the ClientService.
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientDto clientDto;
    private User user;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        user = mock(User.class);

        lenient().when(user.getId()).thenReturn(1);
        lenient().when(user.getFirstName()).thenReturn("Ivan");
        lenient().when(user.getLastName()).thenReturn("Ivanov");
        lenient().when(user.getEmail()).thenReturn("ivan@test.com");
        lenient().when(user.getUsername()).thenReturn("ivan123");

        client = new Client();
        client.setUser(user);
        client.setPhoneNumber("0888123456");

        clientDto = new ClientDto();
        clientDto.setUserId(1);
        clientDto.setPhoneNumber("0888123456");
    }

    /**
     * Tests that all clients are returned successfully.
     */
    @Test
    void shouldFindAllClients() {

        when(clientRepository.findAll())
                .thenReturn(List.of(client));

        List<ClientDto> result =
                clientService.findAll();

        assertEquals(1, result.size());
        assertEquals("0888123456", result.get(0).getPhoneNumber());
        assertEquals("Ivan Ivanov", result.get(0).getUserFullName());

        verify(clientRepository)
                .findAll();
    }

    /**
     * Tests that a client is found successfully by ID.
     */
    @Test
    void shouldFindClientById() {

        when(clientRepository.findById(1))
                .thenReturn(Optional.of(client));

        ClientDto result =
                clientService.findById(1);

        assertNotNull(result);
        assertEquals("0888123456", result.getPhoneNumber());
        assertEquals("Ivan", result.getUserFirstName());

        verify(clientRepository)
                .findById(1);
    }

    /**
     * Tests that ClientNotFound is thrown
     * when the client does not exist.
     */
    @Test
    void shouldThrowWhenClientNotFoundById() {

        when(clientRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFound.class,
                () -> clientService.findById(1)
        );
    }

    /**
     * Tests that a client is found successfully
     * by the related user ID.
     */
    @Test
    void shouldFindClientByUserId() {

        when(clientRepository.findByUserId(1))
                .thenReturn(Optional.of(client));

        ClientDto result =
                clientService.findByUserId(1);

        assertNotNull(result);
        assertEquals("ivan123", result.getUserUsername());

        verify(clientRepository)
                .findByUserId(1);
    }

    /**
     * Tests that ClientNotFound is thrown
     * when no client exists for the given user ID.
     */
    @Test
    void shouldThrowWhenClientNotFoundByUserId() {

        when(clientRepository.findByUserId(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFound.class,
                () -> clientService.findByUserId(1)
        );
    }

    /**
     * Tests that a new client is created successfully
     * when the related user exists.
     */
    @Test
    void shouldCreateClient() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(clientRepository.save(any(Client.class)))
                .thenReturn(client);

        ClientDto result =
                clientService.create(clientDto);

        assertNotNull(result);
        assertEquals("0888123456", result.getPhoneNumber());

        verify(userRepository)
                .findById(1);

        verify(clientRepository)
                .save(any(Client.class));
    }

    /**
     * Tests that RuntimeException is thrown
     * when creating a client with non-existing user.
     */
    @Test
    void shouldThrowWhenCreatingClientWithInvalidUser() {

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> clientService.create(clientDto)
        );

        verify(clientRepository, never())
                .save(any(Client.class));
    }

    /**
     * Tests that an existing client is updated successfully.
     */
    @Test
    void shouldUpdateClient() {

        when(clientRepository.findById(1))
                .thenReturn(Optional.of(client));

        when(clientRepository.save(any(Client.class)))
                .thenReturn(client);

        clientDto.setPhoneNumber("0899999999");

        ClientDto result =
                clientService.update(1, clientDto);

        assertNotNull(result);
        assertEquals("0899999999", result.getPhoneNumber());

        verify(clientRepository)
                .save(client);
    }

    /**
     * Tests that ClientNotFound is thrown
     * when updating a non-existing client.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidClient() {

        when(clientRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFound.class,
                () -> clientService.update(1, clientDto)
        );

        verify(clientRepository, never())
                .save(any(Client.class));
    }

    /**
     * Tests that a client is deleted successfully.
     */
    @Test
    void shouldDeleteClient() {

        when(clientRepository.existsById(1))
                .thenReturn(true);

        clientService.delete(1);

        verify(clientRepository)
                .deleteById(1);
    }

    /**
     * Tests that ClientNotFound is thrown
     * when deleting a non-existing client.
     */
    @Test
    void shouldThrowWhenDeletingInvalidClient() {

        when(clientRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                ClientNotFound.class,
                () -> clientService.delete(1)
        );

        verify(clientRepository, never())
                .deleteById(1);
    }
}
