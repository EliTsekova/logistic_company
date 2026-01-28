package com.team14.logistic_company.repositories;


import com.team14.logistic_company.entities.LogisticCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticCompanyRepository extends JpaRepository<LogisticCompany, Integer> {
}
