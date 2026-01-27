package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    // Допълнителни query методи (optional)
    Optional<Country> findByName(String name);

    boolean existsByName(String name);
}