package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.AdminController;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.servlet.View;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link AdminController} class.
 *
 * These tests verify admin dashboard loading,
 * model attributes and service interactions.
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ClientService clientService;

    @Mock
    private OfficeService officeService;

    @Mock
    private ShipmentService shipmentService;

    /**
     * Initializes MockMvc in standalone mode.
     *
     * This avoids loading the full Spring context,
     * Spring Security filters and Thymeleaf template rendering.
     */
    @BeforeEach
    void setUp() {

        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new AdminController(
                                employeeService,
                                clientService,
                                officeService,
                                shipmentService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    /**
     * Tests that the admin page loads successfully
     * with all required dashboard statistics.
     */
    @Test
    void shouldLoadAdminPage() throws Exception {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "adminUser",
                        "password"
                );

        when(employeeService.findAll())
                .thenReturn(
                        List.of(
                                new EmployeeDto(),
                                new EmployeeDto()
                        )
                );

        when(clientService.findAll())
                .thenReturn(
                        List.of(
                                new ClientDto(),
                                new ClientDto(),
                                new ClientDto()
                        )
                );

        when(officeService.findAll())
                .thenReturn(
                        List.of(
                                new OfficeDto()
                        )
                );

        when(shipmentService.getAllShipmentsForEmployee(authentication))
                .thenReturn(
                        List.of(
                                new Shipment(),
                                new Shipment(),
                                new Shipment(),
                                new Shipment()
                        )
                );

        mockMvc.perform(
                        get("/Admin")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("Admin"))

                .andExpect(
                        model().attribute(
                                "adminUsername",
                                "adminUser"
                        )
                )

                .andExpect(
                        model().attribute(
                                "employeesCount",
                                2
                        )
                )

                .andExpect(
                        model().attribute(
                                "clientsCount",
                                3
                        )
                )

                .andExpect(
                        model().attribute(
                                "officesCount",
                                1
                        )
                )

                .andExpect(
                        model().attribute(
                                "shipmentsCount",
                                4
                        )
                );

        verify(employeeService)
                .findAll();

        verify(clientService)
                .findAll();

        verify(officeService)
                .findAll();

        verify(shipmentService)
                .getAllShipmentsForEmployee(authentication);
    }

    /**
     * Tests that the admin page loads correctly
     * when there is no data in the system.
     */
    @Test
    void shouldLoadAdminPageWithEmptyStatistics() throws Exception {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "adminUser",
                        "password"
                );

        when(employeeService.findAll())
                .thenReturn(List.of());

        when(clientService.findAll())
                .thenReturn(List.of());

        when(officeService.findAll())
                .thenReturn(List.of());

        when(shipmentService.getAllShipmentsForEmployee(authentication))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/Admin")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("Admin"))

                .andExpect(
                        model().attribute(
                                "employeesCount",
                                0
                        )
                )

                .andExpect(
                        model().attribute(
                                "clientsCount",
                                0
                        )
                )

                .andExpect(
                        model().attribute(
                                "officesCount",
                                0
                        )
                )

                .andExpect(
                        model().attribute(
                                "shipmentsCount",
                                0
                        )
                );
    }
}