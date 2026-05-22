package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.ShipmentController;
import com.team14.logistic_company.dtos.*;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.entities.enums.DeliveryType;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link ShipmentController} class.
 */
@ExtendWith(MockitoExtension.class)
class ShipmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ClientService clientService;

    @Mock
    private OfficeService officeService;

    @Mock
    private CityService cityService;

    @BeforeEach
    void setUp() {
        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(new ShipmentController(
                        shipmentService,
                        employeeService,
                        clientService,
                        officeService,
                        cityService
                ))
                .setSingleView(mockView)
                .build();
    }

    private ShipmentController controller() {
        return new ShipmentController(
                shipmentService,
                employeeService,
                clientService,
                officeService,
                cityService
        );
    }

    private UsernamePasswordAuthenticationToken adminAuth() {
        return new UsernamePasswordAuthenticationToken(
                "admin",
                "password",
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }

    private UsernamePasswordAuthenticationToken employeeAuth() {
        return new UsernamePasswordAuthenticationToken(
                "ivan123",
                "password",
                List.of(new SimpleGrantedAuthority("EMPLOYEE"))
        );
    }

    private UsernamePasswordAuthenticationToken clientAuth() {
        return new UsernamePasswordAuthenticationToken(
                "client123",
                "password",
                List.of(new SimpleGrantedAuthority("CLIENT"))
        );
    }

    private EmployeeDto employeeDto(PositionType positionType) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1);
        dto.setUserId(10);
        dto.setPositionType(positionType);
        dto.setUserUsername("ivan123");
        return dto;
    }

    private ShipmentDto shipmentDto() {
        ShipmentDto dto = new ShipmentDto();
        dto.setId(1);
        dto.setEmployeeId(1);
        dto.setDeliverymanId(2);
        dto.setSenderId(1);
        dto.setRecipientName("Ivan Ivanov");
        dto.setRecipientPhone("0888123456");
        dto.setSenderCityId(1);
        dto.setSenderStreet("Vitosha Blvd");
        dto.setSenderPostalCode("1000");
        dto.setOfficeId(1);
        dto.setWeight(2.0);
        dto.setDeliveryType(DeliveryType.TO_OFFICE);
        dto.setUniqueId("SHIPMENT12345");
        return dto;
    }

    private void mockFormAttributes() {
        when(employeeService.findByPositionType(PositionType.COORDINATOR))
                .thenReturn(List.of(new EmployeeDto()));

        when(employeeService.findByPositionType(PositionType.DELIVERYMAN))
                .thenReturn(List.of(new EmployeeDto()));

        when(clientService.findAll())
                .thenReturn(List.of(new ClientDto()));

        when(officeService.findAll())
                .thenReturn(List.of(new OfficeDto()));

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));
    }

    @Test
    void shouldShowShipmentList() throws Exception {
        var auth = adminAuth();

        when(shipmentService.findAllForView(auth))
                .thenReturn(List.of(shipmentDto()));

        mockMvc.perform(get("/shipments").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/list"))
                .andExpect(model().attributeExists("shipments"))
                .andExpect(model().attribute("activePage", "shipments"));

        verify(shipmentService)
                .findAllForView(auth);
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        var auth = adminAuth();

        mockFormAttributes();

        mockMvc.perform(get("/shipments/new").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/form"))
                .andExpect(model().attributeExists("shipment"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("deliverymen"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("cities"))
                .andExpect(model().attributeExists("deliveryTypes"));
    }

    @Test
    void shouldRedirectDeliverymanFromCreateForm() {
        var auth = employeeAuth();

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto(PositionType.DELIVERYMAN));

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller().createForm(
                        mock(Model.class),
                        auth,
                        redirectAttributes
                );

        assertEquals("redirect:/Deliveryman", viewName);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("errorMessage"),
                        eq("Куриерът няма право да създава пратки.")
                );
    }

    @Test
    void shouldCreateShipment() {
        var auth = adminAuth();

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller().create(
                        shipmentDto(),
                        result,
                        model,
                        auth,
                        redirectAttributes
                );

        assertEquals("redirect:/shipments", viewName);

        verify(shipmentService)
                .createFromDto(any(ShipmentDto.class), eq(auth));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Пратката е регистрирана успешно!")
                );
    }

    @Test
    void shouldShowEditForm() throws Exception {
        var auth = adminAuth();

        when(shipmentService.findByIdForEdit(1, auth))
                .thenReturn(shipmentDto());

        mockFormAttributes();

        mockMvc.perform(get("/shipments/1/edit").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/form"))
                .andExpect(model().attributeExists("shipment"))
                .andExpect(model().attributeExists("deliveryTypes"));

        verify(shipmentService)
                .findByIdForEdit(1, auth);
    }

    @Test
    void shouldUpdateShipment() {
        var auth = adminAuth();

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller().update(
                        1,
                        shipmentDto(),
                        result,
                        model,
                        auth,
                        redirectAttributes
                );

        assertEquals("redirect:/shipments", viewName);

        verify(shipmentService)
                .updateFromDto(eq(1), any(ShipmentDto.class), eq(auth));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Пратката е редактирана успешно!")
                );
    }

    @Test
    void shouldDeleteShipment() {
        var auth = adminAuth();

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller().delete(
                        1,
                        auth,
                        redirectAttributes
                );

        assertEquals("redirect:/shipments", viewName);

        verify(shipmentService)
                .deleteShipment(1, auth);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Пратката е изтрита успешно!")
                );
    }

    @Test
    void shouldUpdateShipmentStatus() {
        var auth = adminAuth();

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller().updateStatus(
                        1,
                        Status.DELIVERED,
                        auth,
                        redirectAttributes
                );

        assertEquals("redirect:/shipments", viewName);

        verify(shipmentService)
                .updateShipmentStatus(1, Status.DELIVERED, auth);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Статусът е обновен успешно!")
                );
    }

    @Test
    void shouldShowUndeliveredShipments() throws Exception {
        var auth = adminAuth();

        when(shipmentService.getUndeliveredShipments())
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/undelivered").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/list"))
                .andExpect(model().attributeExists("shipments"))
                .andExpect(model().attribute("activePage", "undelivered"));
    }

    @Test
    void shouldShowShipmentsByEmployee() throws Exception {
        var auth = adminAuth();

        when(shipmentService.getShipmentsByEmployeeId(1))
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/employee/1").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/list"))
                .andExpect(model().attributeExists("shipments"));
    }

    @Test
    void shouldShowShipmentDetails() throws Exception {
        var auth = adminAuth();

        when(shipmentService.findByIdForView(1, auth))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/1").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/details"))
                .andExpect(model().attributeExists("shipment"));

        verify(shipmentService)
                .findByIdForView(1, auth);
    }

    @Test
    void shouldShowShipmentHistory() throws Exception {
        when(shipmentService.getShipmentHistory("SHIPMENT12345"))
                .thenReturn(List.of(new ShipmentStatus()));

        mockMvc.perform(get("/shipments/SHIPMENT12345/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/history"))
                .andExpect(model().attributeExists("history"))
                .andExpect(model().attribute("uniqueId", "SHIPMENT12345"));
    }

    @Test
    void shouldShowRevenueReport() throws Exception {
        var auth = adminAuth();

        when(shipmentService.getRevenueBetween(any(), any(), eq(auth)))
                .thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(get("/shipments/revenue")
                        .principal(auth)
                        .param("from", "2026-01-01")
                        .param("to", "2026-01-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("shipments/revenue"))
                .andExpect(model().attribute("revenue", new BigDecimal("100.00")))
                .andExpect(model().attribute("from", "2026-01-01"))
                .andExpect(model().attribute("to", "2026-01-31"));
    }

    @Test
    void shouldCalculatePrice() throws Exception {
        when(shipmentService.calculatePrice(2.0, DeliveryType.TO_OFFICE))
                .thenReturn(new BigDecimal("9.00"));

        mockMvc.perform(get("/shipments/calculate-price")
                        .param("weight", "2.0")
                        .param("deliveryType", "TO_OFFICE"))
                .andExpect(status().isOk())
                .andExpect(content().string("9.00"));

        verify(shipmentService)
                .calculatePrice(2.0, DeliveryType.TO_OFFICE);
    }

    @Test
    void shouldShowMySentShipments() throws Exception {
        var auth = clientAuth();

        Shipment shipment = mock(Shipment.class);

        when(shipment.getId())
                .thenReturn(1);

        when(shipmentService.getSentByClient(auth))
                .thenReturn(List.of(shipment));

        when(shipmentService.findByIdForView(1, auth))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/my/sent").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("Client"))
                .andExpect(model().attributeExists("shipments"))
                .andExpect(model().attribute("clientName", "client123"));
    }

    @Test
    void shouldShowMyReceivedShipments() throws Exception {
        var auth = clientAuth();

        Shipment shipment = mock(Shipment.class);

        when(shipment.getId())
                .thenReturn(1);

        when(shipmentService.getReceivedByClient(auth))
                .thenReturn(List.of(shipment));

        when(shipmentService.findByIdForView(1, auth))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/my/received").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("Client"))
                .andExpect(model().attributeExists("shipments"));
    }

    @Test
    void shouldShowMyExpectedShipments() throws Exception {
        var auth = clientAuth();

        Shipment shipment = mock(Shipment.class);

        when(shipment.getId())
                .thenReturn(1);

        when(shipmentService.getExpectedByClient(auth))
                .thenReturn(List.of(shipment));

        when(shipmentService.findByIdForView(1, auth))
                .thenReturn(shipmentDto());

        mockMvc.perform(get("/shipments/my/expected").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("Client"))
                .andExpect(model().attributeExists("shipments"));
    }
}