package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.EmployeeController;
import com.team14.logistic_company.controllers.forms.CreateEmployeeForm;
import com.team14.logistic_company.controllers.forms.UpdateEmployeeForm;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.ShipmentService;
import com.team14.logistic_company.services.UserService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link EmployeeController} class.
 *
 * These tests verify employee controller routes,
 * view names, model attributes and service calls.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private OfficeService officeService;

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
                        new EmployeeController(
                                employeeService,
                                officeService,
                                userService,
                                shipmentService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    private EmployeeDto buildEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();

        dto.setId(1);
        dto.setUserId(10);
        dto.setOfficeId(2);
        dto.setPositionType(PositionType.COORDINATOR);

        dto.setUserFirstName("Ivan");
        dto.setUserLastName("Ivanov");
        dto.setUserFullName("Ivan Ivanov");
        dto.setUserUsername("ivan123");
        dto.setUserEmail("ivan@test.com");

        dto.setOfficeTitle("Office Sofia");

        return dto;
    }

    @Test
    void shouldShowAllEmployees() throws Exception {
        when(employeeService.findAll())
                .thenReturn(List.of(buildEmployeeDto()));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", hasSize(1)));

        verify(employeeService)
                .findAll();
    }

    @Test
    void shouldShowEmployeeDetails() throws Exception {
        when(employeeService.findById(1))
                .thenReturn(buildEmployeeDto());

        when(shipmentService.getShipmentsByEmployeeId(1))
                .thenReturn(List.of());

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/details"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("shipments"));

        verify(employeeService)
                .findById(1);

        verify(shipmentService)
                .getShipmentsByEmployeeId(1);
    }

    @Test
    void shouldShowEmployeesByPosition() throws Exception {
        when(employeeService.findByPositionType(PositionType.COORDINATOR))
                .thenReturn(List.of(buildEmployeeDto()));

        mockMvc.perform(get("/employees/position/COORDINATOR"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("positionType", PositionType.COORDINATOR));

        verify(employeeService)
                .findByPositionType(PositionType.COORDINATOR);
    }

    @Test
    void shouldShowEmployeesByOffice() throws Exception {
        when(employeeService.findByOfficeId(2))
                .thenReturn(List.of(buildEmployeeDto()));

        when(officeService.findById(2))
                .thenReturn(new OfficeDto());

        mockMvc.perform(get("/employees/office/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("office"));

        verify(employeeService)
                .findByOfficeId(2);

        verify(officeService)
                .findById(2);
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        when(officeService.findAll())
                .thenReturn(List.of(new OfficeDto()));

        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/form"))
                .andExpect(model().attributeExists("employeeForm"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("positionTypes"));

        verify(officeService)
                .findAll();
    }

    @Test
    void shouldCreateEmployee() {
        EmployeeController controller =
                new EmployeeController(
                        employeeService,
                        officeService,
                        userService,
                        shipmentService
                );

        User savedUser = mock(User.class);

        when(savedUser.getId())
                .thenReturn(10);

        when(userService.create(any()))
                .thenReturn(savedUser);

        CreateEmployeeForm form = new CreateEmployeeForm();

        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPassword("password123");
        form.setPositionType(PositionType.COORDINATOR);
        form.setOfficeId(2);

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.createEmployee(
                        form,
                        result,
                        model,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/employees",
                viewName
        );

        verify(userService)
                .create(any());

        verify(employeeService)
                .create(any(EmployeeDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Служителят е създаден успешно!")
                );
    }

    @Test
    void shouldReturnCreateFormWhenValidationFails() throws Exception {
        when(officeService.findAll())
                .thenReturn(List.of(new OfficeDto()));

        mockMvc.perform(post("/employees")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("username", "")
                        .param("email", "invalid-email")
                        .param("password", "")
                        .param("positionType", "")
                        .param("officeId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/form"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("positionTypes"));

        verify(userService, never())
                .create(any());

        verify(employeeService, never())
                .create(any(EmployeeDto.class));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        when(employeeService.findById(1))
                .thenReturn(buildEmployeeDto());

        when(officeService.findAll())
                .thenReturn(List.of(new OfficeDto()));

        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/edit-form"))
                .andExpect(model().attributeExists("employeeForm"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("positionTypes"));

        verify(employeeService)
                .findById(1);

        verify(officeService)
                .findAll();
    }

    @Test
    void shouldUpdateEmployeeAsAdmin() {
        EmployeeController controller =
                new EmployeeController(
                        employeeService,
                        officeService,
                        userService,
                        shipmentService
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "admin",
                        "password",
                        List.of(new SimpleGrantedAuthority("ADMIN"))
                );

        UpdateEmployeeForm form = new UpdateEmployeeForm();

        form.setId(1);
        form.setUserId(10);
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPositionType(PositionType.COORDINATOR);
        form.setOfficeId(2);

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateEmployee(
                        1,
                        form,
                        result,
                        model,
                        authentication,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/employees",
                viewName
        );

        verify(userService)
                .update(any());

        verify(employeeService)
                .update(eq(1), any(EmployeeDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Служителят е редактиран успешно!")
                );
    }

    @Test
    void shouldUpdateEmployeeAsCoordinator() {
        EmployeeController controller =
                new EmployeeController(
                        employeeService,
                        officeService,
                        userService,
                        shipmentService
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "ivan123",
                        "password",
                        List.of(new SimpleGrantedAuthority("EMPLOYEE"))
                );

        EmployeeDto employeeDto = buildEmployeeDto();
        employeeDto.setPositionType(PositionType.COORDINATOR);

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto);

        UpdateEmployeeForm form = new UpdateEmployeeForm();

        form.setId(1);
        form.setUserId(10);
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPositionType(PositionType.COORDINATOR);
        form.setOfficeId(2);

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateEmployee(
                        1,
                        form,
                        result,
                        model,
                        authentication,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/Coordinator",
                viewName
        );

        verify(employeeService)
                .findByUsername("ivan123");
    }

    @Test
    void shouldUpdateEmployeeAsDeliveryman() {
        EmployeeController controller =
                new EmployeeController(
                        employeeService,
                        officeService,
                        userService,
                        shipmentService
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "ivan123",
                        "password",
                        List.of(new SimpleGrantedAuthority("EMPLOYEE"))
                );

        EmployeeDto employeeDto = buildEmployeeDto();
        employeeDto.setPositionType(PositionType.DELIVERYMAN);

        when(employeeService.findByUsername("ivan123"))
                .thenReturn(employeeDto);

        UpdateEmployeeForm form = new UpdateEmployeeForm();

        form.setId(1);
        form.setUserId(10);
        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPositionType(PositionType.DELIVERYMAN);
        form.setOfficeId(2);

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateEmployee(
                        1,
                        form,
                        result,
                        model,
                        authentication,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/Deliveryman",
                viewName
        );

        verify(employeeService)
                .findByUsername("ivan123");
    }

    @Test
    void shouldReturnEditFormWhenValidationFails() throws Exception {
        when(officeService.findAll())
                .thenReturn(List.of(new OfficeDto()));

        mockMvc.perform(post("/employees/update/1")
                        .param("id", "1")
                        .param("userId", "10")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("username", "")
                        .param("email", "invalid-email")
                        .param("positionType", "")
                        .param("officeId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/edit-form"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("positionTypes"));

        verify(userService, never())
                .update(any());

        verify(employeeService, never())
                .update(eq(1), any(EmployeeDto.class));
    }

    @Test
    void shouldDeleteEmployee() {
        EmployeeController controller =
                new EmployeeController(
                        employeeService,
                        officeService,
                        userService,
                        shipmentService
                );

        when(employeeService.findById(1))
                .thenReturn(buildEmployeeDto());

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.deleteEmployee(
                        1,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/employees",
                viewName
        );

        verify(employeeService)
                .delete(1);

        verify(userService)
                .delete(10);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Служителят е изтрит успешно!")
                );
    }
}