package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.EmployeePageController;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link EmployeePageController} class.
 *
 * These tests verify employee dashboard routing,
 * dashboard model attributes and service interactions.
 */
@ExtendWith(MockitoExtension.class)
class EmployeePageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new EmployeePageController(
                                shipmentService,
                                employeeService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    private UsernamePasswordAuthenticationToken authentication() {
        return new UsernamePasswordAuthenticationToken(
                "ivan123",
                "password"
        );
    }

    private EmployeeDto employeeDto(PositionType positionType) {
        EmployeeDto dto = new EmployeeDto();

        dto.setId(1);
        dto.setUserId(10);
        dto.setPositionType(positionType);
        dto.setUserUsername("ivan123");
        dto.setUserFullName("Ivan Ivanov");

        return dto;
    }

    @Test
    void shouldRedirectCoordinatorToCoordinatorDashboard() {
        EmployeePageController controller =
                new EmployeePageController(
                        shipmentService,
                        employeeService
                );

        UsernamePasswordAuthenticationToken authentication =
                authentication();

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto(PositionType.COORDINATOR));

        String viewName =
                controller.employeeRedirect(authentication);

        assertEquals(
                "redirect:/Coordinator",
                viewName
        );

        verify(employeeService)
                .findByUsername("ivan123");
    }

    @Test
    void shouldRedirectDeliverymanToDeliverymanDashboard() {
        EmployeePageController controller =
                new EmployeePageController(
                        shipmentService,
                        employeeService
                );

        UsernamePasswordAuthenticationToken authentication =
                authentication();

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto(PositionType.DELIVERYMAN));

        String viewName =
                controller.employeeRedirect(authentication);

        assertEquals(
                "redirect:/Deliveryman",
                viewName
        );

        verify(employeeService)
                .findByUsername("ivan123");
    }

    @Test
    void shouldShowCoordinatorDashboard() throws Exception {
        var authentication = authentication();

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto(PositionType.COORDINATOR));

        when(shipmentService.findAllForView(authentication))
                .thenReturn(
                        List.of(
                                new ShipmentDto(),
                                new ShipmentDto()
                        )
                );

        when(shipmentService.getUndeliveredShipments())
                .thenReturn(
                        List.of(
                                new Shipment()
                        )
                );

        mockMvc.perform(
                        get("/Coordinator")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("Coordinator"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employeeId", 1))
                .andExpect(model().attributeExists("shipments"))
                .andExpect(model().attribute("totalShipments", 2))
                .andExpect(model().attribute("undeliveredCount", 1));

        verify(employeeService)
                .findByUsername("ivan123");

        verify(shipmentService)
                .findAllForView(authentication);

        verify(shipmentService)
                .getUndeliveredShipments();
    }

    @Test
    void shouldShowDeliverymanDashboard() throws Exception {
        var authentication = authentication();

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto(PositionType.DELIVERYMAN));

        when(shipmentService.findAllForView(authentication))
                .thenReturn(
                        List.of(
                                new ShipmentDto()
                        )
                );

        when(shipmentService.getUndeliveredShipments())
                .thenReturn(
                        List.of(
                                new Shipment(),
                                new Shipment()
                        )
                );

        mockMvc.perform(
                        get("/Deliveryman")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("Deliveryman"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute("employeeId", 1))
                .andExpect(model().attributeExists("shipments"))
                .andExpect(model().attribute("totalShipments", 1))
                .andExpect(model().attribute("undeliveredCount", 2));

        verify(employeeService)
                .findByUsername("ivan123");

        verify(shipmentService)
                .findAllForView(authentication);

        verify(shipmentService)
                .getUndeliveredShipments();
    }
}