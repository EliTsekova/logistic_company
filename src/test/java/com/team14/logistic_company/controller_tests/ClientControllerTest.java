package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.ClientController;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.ShipmentService;
import com.team14.logistic_company.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link ClientController} class.
 *
 * These tests verify client controller routes,
 * view names, model attributes and service calls.
 */
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private UserService userService;

    @Mock
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new ClientController(
                                clientService,
                                userService,
                                shipmentService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    private ClientDto buildClientDto() {
        ClientDto dto = new ClientDto();

        dto.setId(1);
        dto.setUserId(10);
        dto.setPhoneNumber("0888123456");

        dto.setUserFirstName("Ivan");
        dto.setUserLastName("Ivanov");
        dto.setUserFullName("Ivan Ivanov");
        dto.setUserUsername("ivan123");
        dto.setUserEmail("ivan@test.com");

        return dto;
    }

    @Test
    void shouldShowAllClients() throws Exception {
        when(clientService.findAll())
                .thenReturn(List.of(buildClientDto()));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/list"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attribute("clients", hasSize(1)));

        verify(clientService)
                .findAll();
    }

    @Test
    void shouldShowClientDetails() throws Exception {
        Shipment shipment = new Shipment();
        ShipmentDto shipmentDto = new ShipmentDto();

        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        when(shipmentService.getSentShipmentsByClientId(1))
                .thenReturn(List.of(shipment));

        when(shipmentService.getReceivedShipmentsByClientId(1))
                .thenReturn(List.of());

        when(shipmentService.getExpectedShipmentsByClientId(1))
                .thenReturn(List.of());

        when(shipmentService.getShipmentsByClientId(1))
                .thenReturn(List.of(shipment));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(shipmentDto);

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/details"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attributeExists("sentShipments"))
                .andExpect(model().attributeExists("receivedShipments"))
                .andExpect(model().attributeExists("expectedShipments"))
                .andExpect(model().attributeExists("allShipments"))
                .andExpect(model().attribute("totalShipments", 1))
                .andExpect(model().attribute("sentCount", 1))
                .andExpect(model().attribute("receivedCount", 0))
                .andExpect(model().attribute("expectedCount", 0))
                .andExpect(model().attribute("activeTab", "all"));

        verify(clientService)
                .findById(1);
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/clients/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"))
                .andExpect(model().attributeExists("clientForm"));
    }

    @Test
    void shouldCreateClient() {
        ClientController controller =
                new ClientController(
                        clientService,
                        userService,
                        shipmentService
                );

        User savedUser = mock(User.class);

        when(savedUser.getId())
                .thenReturn(10);

        when(userService.create(any()))
                .thenReturn(savedUser);

        com.team14.logistic_company.controllers.forms.CreateClientForm form =
                new com.team14.logistic_company.controllers.forms.CreateClientForm();

        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPassword("password123");
        form.setConfirmPassword("password123");
        form.setPhoneNumber("0888123456");

        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.createClient(
                        form,
                        result,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/clients",
                viewName
        );

        verify(userService)
                .create(any());

        verify(clientService)
                .create(any(ClientDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Клиентът е създаден успешно!")
                );
    }

    @Test
    void shouldReturnCreateFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("username", "")
                        .param("email", "invalid-email")
                        .param("password", "")
                        .param("confirmPassword", "")
                        .param("phoneNumber", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"));

        verify(userService, never())
                .create(any());

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnCreateFormWhenPasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("firstName", "Ivan")
                        .param("lastName", "Ivanov")
                        .param("username", "ivan123")
                        .param("email", "ivan@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "different123")
                        .param("phoneNumber", "0888123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"));

        verify(userService, never())
                .create(any());

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        mockMvc.perform(get("/clients/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/edit-form"))
                .andExpect(model().attributeExists("clientForm"));

        verify(clientService)
                .findById(1);
    }

    @Test
    void shouldUpdateClient() {
        ClientController controller =
                new ClientController(
                        clientService,
                        userService,
                        shipmentService
                );

        com.team14.logistic_company.controllers.forms.UpdateClientForm form =
                new com.team14.logistic_company.controllers.forms.UpdateClientForm();

        form.setId(1);
        form.setUserId(10);
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPhoneNumber("0888123456");

        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateClient(
                        1,
                        form,
                        result,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/clients",
                viewName
        );

        verify(userService)
                .update(any());

        verify(clientService)
                .update(eq(1), any(ClientDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Клиентът е редактиран успешно!")
                );
    }

    @Test
    void shouldReturnEditFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/clients/update/1")
                        .param("id", "1")
                        .param("userId", "10")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("username", "")
                        .param("email", "invalid-email")
                        .param("phoneNumber", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/edit-form"));

        verify(userService, never())
                .update(any());

        verify(clientService, never())
                .update(eq(1), any(ClientDto.class));
    }

    @Test
    void shouldDeleteClient() {
        ClientController controller =
                new ClientController(
                        clientService,
                        userService,
                        shipmentService
                );

        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String viewName =
                controller.deleteClient(
                        1,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/clients",
                viewName
        );

        verify(clientService)
                .delete(1);

        verify(userService)
                .delete(10);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Клиентът е изтрит успешно!")
                );
    }

    @Test
    void shouldShowAllClientShipments() throws Exception {
        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        when(shipmentService.getShipmentsByClientId(1))
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.getSentShipmentsByClientId(1))
                .thenReturn(List.of());

        when(shipmentService.getReceivedShipmentsByClientId(1))
                .thenReturn(List.of());

        when(shipmentService.getExpectedShipmentsByClientId(1))
                .thenReturn(List.of());

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(new ShipmentDto());

        mockMvc.perform(get("/clients/1/shipments/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/details"))
                .andExpect(model().attribute("activeTab", "all"));
    }

    @Test
    void shouldShowSentClientShipments() throws Exception {
        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        when(shipmentService.getSentShipmentsByClientId(1))
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(new ShipmentDto());

        mockMvc.perform(get("/clients/1/shipments/sent"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/details"))
                .andExpect(model().attribute("activeTab", "sent"));
    }

    @Test
    void shouldShowReceivedClientShipments() throws Exception {
        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        when(shipmentService.getReceivedShipmentsByClientId(1))
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(new ShipmentDto());

        mockMvc.perform(get("/clients/1/shipments/received"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/details"))
                .andExpect(model().attribute("activeTab", "received"));
    }

    @Test
    void shouldShowExpectedClientShipments() throws Exception {
        when(clientService.findById(1))
                .thenReturn(buildClientDto());

        when(shipmentService.getExpectedShipmentsByClientId(1))
                .thenReturn(List.of(new Shipment()));

        when(shipmentService.toDtoWithCurrentStatus(any(Shipment.class)))
                .thenReturn(new ShipmentDto());

        mockMvc.perform(get("/clients/1/shipments/expected"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/details"))
                .andExpect(model().attribute("activeTab", "expected"));
    }
}