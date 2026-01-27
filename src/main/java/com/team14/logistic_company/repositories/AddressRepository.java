package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // Намиране на адреси по град
    List<Address> findByCityId(Integer cityId);

    // Допълнителни query методи (optional)
    List<Address> findByStreetContainingIgnoreCase(String street);

    List<Address> findByPostalCode(String postalCode);

    List<Address> findByCityIdAndPostalCode(Integer cityId, String postalCode);
}