package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    // Намиране на градове по държава
    List<City> findByCountryId(Integer countryId);

    // Допълнителни query методи (optional)
    Optional<City> findByName(String name);

    boolean existsByName(String name);

    List<City> findByNameContainingIgnoreCase(String name);
}