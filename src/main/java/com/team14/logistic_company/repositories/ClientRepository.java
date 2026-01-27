package com.team14.logistic_company.repositories;
import com.team14.logistic_company.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // Намиране по User ID
    Optional<Client> findByUserId(Integer userId);

    // Намиране по телефонен номер
    Optional<Client> findByPhoneNumber(String phoneNumber);

    // Проверка дали съществува по User ID
    boolean existsByUserId(Integer userId);

    // Проверка дали съществува по телефонен номер
    boolean existsByPhoneNumber(String phoneNumber);
}