package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.OfficeNotFound;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfficeService implements IOfficeService {

    private final OfficeRepository officeRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<OfficeDto> findAll() {
        return officeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OfficeDto findById(Integer id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFound(id));
        return convertToDto(office);
    }

    @Override
    public OfficeDto create(OfficeDto officeDto) {
        Address address = addressRepository.findById(officeDto.getAddressId())
                .orElseThrow(() -> new AddressNotFound(officeDto.getAddressId()));

        Office office = convertToEntity(officeDto);
        office.setAddress(address);

        Office saved = officeRepository.save(office);
        return convertToDto(saved);
    }

    @Override
    public OfficeDto update(Integer id, OfficeDto officeDto) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFound(id));

        Address address = addressRepository.findById(officeDto.getAddressId())
                .orElseThrow(() -> new AddressNotFound(officeDto.getAddressId()));

        office.setTitle(officeDto.getTitle());
        office.setAddress(address);

        Office updated = officeRepository.save(office);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!officeRepository.existsById(id)) {
            throw new OfficeNotFound(id);
        }
        officeRepository.deleteById(id);
    }

    // Converter methods
    private OfficeDto convertToDto(Office office) {
        OfficeDto dto = new OfficeDto();
        dto.setId(office.getId());
        dto.setTitle(office.getTitle());
        dto.setAddressId(office.getAddress().getId());
        dto.setCreatedOn(office.getCreatedOn());
        dto.setUpdatedOn(office.getUpdatedOn());
        return dto;
    }

    private Office convertToEntity(OfficeDto dto) {
        Office office = new Office();
        office.setTitle(dto.getTitle());
        return office;
    }
}