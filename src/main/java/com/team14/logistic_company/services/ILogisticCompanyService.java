package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.LogisticCompany;

/**
 * Service interface for managing the Logistic Company entity.
 *
 * <p>
 * This service follows a singleton-like pattern because the system
 * is designed to contain only one LogisticCompany record.
 * </p>
 *
 * <p>
 * Provides operations for retrieving, updating, and resetting
 * the company configuration and core data.
 * </p>
 */
public interface ILogisticCompanyService {

    /**
     * Returns the single LogisticCompany record.
     * If no record exists, a new one is created automatically.
     *
     * @return the existing or newly created LogisticCompany entity
     */
    LogisticCompany getSingleton();

    /**
     * Updates the single LogisticCompany record.
     *
     * @param input updated LogisticCompany data
     * @return updated LogisticCompany entity
     */
    LogisticCompany update(LogisticCompany input);

    /**
     * Deletes the existing LogisticCompany record.
     *
     * <p>
     * After reset, calling {@code getSingleton()} will recreate it.
     * </p>
     */
    void reset();
}
