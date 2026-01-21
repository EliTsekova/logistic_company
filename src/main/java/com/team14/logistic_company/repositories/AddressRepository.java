package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
