package com.team14.logistic_company.services;
import com.team14.logistic_company.dtos.ClientDto;

import java.util.List;

public interface IClientService {
    List<ClientDto> findAll();
    ClientDto findById(Integer id);
    ClientDto findByUserId(Integer userId);
    ClientDto create(ClientDto clientDto);
    ClientDto update(Integer id, ClientDto clientDto);
    void delete(Integer id);
}