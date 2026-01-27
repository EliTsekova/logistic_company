package com.team14.logistic_company.services;
import com.team14.logistic_company.dtos.AddressDto;

import java.util.List;

public interface IAddressService {
    List<AddressDto> findAll();
    AddressDto findById(Integer id);
    List<AddressDto> findByCityId(Integer cityId);
    AddressDto create(AddressDto addressDto);
    AddressDto update(Integer id, AddressDto addressDto);
    void delete(Integer id);
}
