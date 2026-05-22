/**
 * Service implementation for managing the single LogisticCompany entity.
 *
 * <p>
 * This service follows a singleton pattern, meaning the system maintains
 * only one LogisticCompany record at any given time.
 * </p>
 *
 * <p>
 * It provides functionality for:
 * </p>
 * <ul>
 *     <li>Retrieving the company (auto-creating if missing)</li>
 *     <li>Updating company information</li>
 *     <li>Resetting (deleting) the company</li>
 * </ul>
 */
package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.LogisticCompany;
import com.team14.logistic_company.repositories.LogisticCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogisticCompanyService implements ILogisticCompanyService {

    private final LogisticCompanyRepository repository;

    /**
     * Retrieves the single LogisticCompany instance.
     * If none exists, a default one is created automatically.
     *
     * @return existing or newly created LogisticCompany entity
     */
    public LogisticCompany getSingleton() {
        return repository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    LogisticCompany c = new LogisticCompany();
                    c.setName("Logistic Company");
                    c.setUic("0000000000");
                    c.setPhone("");
                    c.setEmail("");
                    c.setAddress("");
                    return repository.save(c);
                });
    }

    /**
     * Updates the LogisticCompany entity with new values.
     *
     * @param input updated company data
     * @return updated LogisticCompany entity
     */
    public LogisticCompany update(LogisticCompany input) {
        LogisticCompany existing = getSingleton();

        existing.setName(input.getName());
        existing.setUic(input.getUic());
        existing.setPhone(input.getPhone());
        existing.setEmail(input.getEmail());
        existing.setAddress(input.getAddress());

        return repository.save(existing);
    }

    /**
     * Deletes the existing LogisticCompany record.
     * A new one will be created on next access.
     */
    public void reset() {
        LogisticCompany existing = getSingleton();
        repository.deleteById(existing.getId());
    }
}