package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.LogisticCompany;

public interface ILogisticCompanyService {

    /**
     * Returns the single LogisticCompany record (creates one if missing).
     */
    LogisticCompany getSingleton();

    /**
     * Updates the single LogisticCompany record.
     */
    LogisticCompany update(LogisticCompany input);

    /**
     * Resets (deletes) the single LogisticCompany record.
     * Next call to getSingleton() will recreate it.
     */
    void reset();
}
