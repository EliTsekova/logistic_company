package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
