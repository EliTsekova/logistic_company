package com.team14.logistic_company.configurations;

import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component responsible for seeding initial application data
 * when the application starts.
 * <p>
 * This class creates default users such as an administrator
 * and sample client accounts if they do not already exist
 * in the database.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    /**
     * Repository used for user persistence operations.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder used for secure password hashing.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes data seeding logic during application startup.
     * <p>
     * Creates default admin and client users if they are missing
     * from the database.
     * </p>
     *
     * @param args application startup arguments
     */
    @Override
    public void run(String... args) {

        // Create default administrator account
        if (!userRepository.existsByUsername("admin")) {

            User admin = new User();

            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setUsername("admin");
            admin.setEmail("admin@logistic.bg");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }

        // Create sample client account
        if (!userRepository.existsByUsername("client1")) {

            User client = new User();

            client.setFirstName("Ivan");
            client.setLastName("Ivanov");
            client.setUsername("client1");
            client.setEmail("client1@logistic.bg");
            client.setPassword(passwordEncoder.encode("client123"));
            client.setRole(Role.CLIENT);

            userRepository.save(client);
        }
    }
}