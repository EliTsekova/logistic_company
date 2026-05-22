package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.entities.LogisticCompany;
import com.team14.logistic_company.repositories.LogisticCompanyRepository;
import com.team14.logistic_company.services.LogisticCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LogisticCompanyService} class.
 *
 * These tests verify the singleton behavior,
 * update functionality and repository interactions
 * of the LogisticCompanyService.
 */
@ExtendWith(MockitoExtension.class)
class LogisticCompanyServiceTest {

    @Mock
    private LogisticCompanyRepository repository;

    @InjectMocks
    private LogisticCompanyService logisticCompanyService;

    private LogisticCompany company;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        company = new LogisticCompany();

        company.setName("Speed Logistics");
        company.setUic("1234567890");
        company.setPhone("0888123456");
        company.setEmail("office@test.com");
        company.setAddress("Sofia, Bulgaria");
    }

    /**
     * Tests that the existing singleton company
     * is returned successfully.
     */
    @Test
    void shouldReturnExistingSingleton() {

        when(repository.findAll())
                .thenReturn(List.of(company));

        LogisticCompany result =
                logisticCompanyService.getSingleton();

        assertNotNull(result);
        assertEquals(
                "Speed Logistics",
                result.getName()
        );

        verify(repository)
                .findAll();

        verify(repository, never())
                .save(any(LogisticCompany.class));
    }

    /**
     * Tests that a default company
     * is created when no company exists.
     */
    @Test
    void shouldCreateDefaultSingletonWhenMissing() {

        when(repository.findAll())
                .thenReturn(List.of());

        when(repository.save(any(LogisticCompany.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LogisticCompany result =
                logisticCompanyService.getSingleton();

        assertNotNull(result);

        assertEquals(
                "Logistic Company",
                result.getName()
        );

        assertEquals(
                "0000000000",
                result.getUic()
        );

        verify(repository)
                .save(any(LogisticCompany.class));
    }

    /**
     * Tests that the singleton company
     * is updated successfully.
     */
    @Test
    void shouldUpdateSingletonCompany() {

        LogisticCompany updatedInput =
                new LogisticCompany();

        updatedInput.setName("Updated Logistics");
        updatedInput.setUic("9999999999");
        updatedInput.setPhone("0899999999");
        updatedInput.setEmail("updated@test.com");
        updatedInput.setAddress("Plovdiv, Bulgaria");

        when(repository.findAll())
                .thenReturn(List.of(company));

        when(repository.save(any(LogisticCompany.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LogisticCompany result =
                logisticCompanyService.update(updatedInput);

        assertNotNull(result);

        assertEquals(
                "Updated Logistics",
                result.getName()
        );

        assertEquals(
                "9999999999",
                result.getUic()
        );

        assertEquals(
                "0899999999",
                result.getPhone()
        );

        assertEquals(
                "updated@test.com",
                result.getEmail()
        );

        assertEquals(
                "Plovdiv, Bulgaria",
                result.getAddress()
        );

        verify(repository)
                .save(company);
    }

    /**
     * Tests that the singleton company
     * is deleted successfully during reset.
     */
    @Test
    void shouldResetSingletonCompany() {

        when(repository.findAll())
                .thenReturn(List.of(company));

        logisticCompanyService.reset();

        verify(repository)
                .deleteById(company.getId());
    }

    /**
     * Tests that reset creates a default company
     * if no company exists before deletion.
     */
    @Test
    void shouldResetWhenNoCompanyExists() {

        when(repository.findAll())
                .thenReturn(List.of());

        when(repository.save(any(LogisticCompany.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        logisticCompanyService.reset();

        verify(repository)
                .save(any(LogisticCompany.class));

        verify(repository)
                .deleteById(any());
    }
}