package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
