package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
