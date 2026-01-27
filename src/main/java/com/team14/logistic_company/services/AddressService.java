package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.AddressDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.CityNotFound;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFound(id));
        return convertToDto(address);
    }

    @Override
    public List<AddressDto> findByCityId(Integer cityId) {
        return addressRepository.findByCityId(cityId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto create(AddressDto addressDto) {
        City city = cityRepository.findById(addressDto.getCityId())
                .orElseThrow(() -> new CityNotFound(addressDto.getCityId()));

        Address address = convertToEntity(addressDto);
        address.setCity(city);

        Address saved = addressRepository.save(address);
        return convertToDto(saved);
    }

    @Override
    public AddressDto update(Integer id, AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFound(id));

        City city = cityRepository.findById(addressDto.getCityId())
                .orElseThrow(() -> new CityNotFound(addressDto.getCityId()));

        address.setStreet(addressDto.getStreet());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCity(city);

        Address updated = addressRepository.save(address);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFound(id);
        }
        addressRepository.deleteById(id);
    }

    // Converter methods
    private AddressDto convertToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setPostalCode(address.getPostalCode());
        dto.setCityId(address.getCity().getId());
        dto.setCreatedOn(address.getCreatedOn());
        dto.setUpdatedOn(address.getUpdatedOn());
        return dto;
    }

    private Address convertToEntity(AddressDto dto) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        return address;
    }
}