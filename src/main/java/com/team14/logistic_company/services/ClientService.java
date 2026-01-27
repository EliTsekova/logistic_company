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

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto findById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFound(id));
        return convertToDto(client);
    }

    @Override
    public ClientDto findByUserId(Integer userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ClientNotFound("Client not found for user id: " + userId));
        return convertToDto(client);
    }

    @Override
    public ClientDto create(ClientDto clientDto) {
        // Намери User
        User user = userRepository.findById(clientDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + clientDto.getUserId()));

        Client client = new Client();
        client.setUser(user);
        client.setPhoneNumber(clientDto.getPhoneNumber());

        Client saved = clientRepository.save(client);
        return convertToDto(saved);
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFound(id));

        client.setPhoneNumber(clientDto.getPhoneNumber());

        Client updated = clientRepository.save(client);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFound(id);
        }
        clientRepository.deleteById(id);
    }

    // Converter methods
    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setUserId(client.getUser().getId());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setCreatedOn(client.getCreatedOn());
        dto.setUpdatedOn(client.getUpdatedOn());

        // User информация
        User user = client.getUser();
        dto.setUserFirstName(user.getFirstName());
        dto.setUserLastName(user.getLastName());
        dto.setUserFullName(user.getFirstName() + " " + user.getLastName());
        dto.setUserEmail(user.getEmail());
        dto.setUserUsername(user.getUsername());

        return dto;
    }
}