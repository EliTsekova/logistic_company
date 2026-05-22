package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.LogisticCompany;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface used for logistic company database operations.
 */
public interface LogisticCompanyRepository extends JpaRepository<LogisticCompany, Integer> {
}