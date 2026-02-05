package com.team14.logistic_company.services;


import com.team14.logistic_company.entities.LogisticCompany;
import com.team14.logistic_company.repositories.LogisticCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogisticCompanyService implements ILogisticCompanyService{

    private final LogisticCompanyRepository repository;
/// TODO: the data about the logistic company to be actualized
    public LogisticCompany getSingleton() {
        return repository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    LogisticCompany c = new LogisticCompany();
                    c.setName("Logistic Company");
                    c.setUic("0000000000");
                    c.setPhone("");
                    c.setEmail("");
                    c.setAddress(null);
                    return repository.save(c);
                });
    }

    public LogisticCompany update(LogisticCompany input) {
        LogisticCompany existing = getSingleton();

        existing.setName(input.getName());
        existing.setUic(input.getUic());
        existing.setPhone(input.getPhone());
        existing.setEmail(input.getEmail());
        existing.setAddress(input.getAddress());

        return repository.save(existing);
    }

    public void reset() {
        LogisticCompany existing = getSingleton();
        repository.deleteById(existing.getId());
    }
}
