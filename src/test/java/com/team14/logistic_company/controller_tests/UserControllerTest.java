package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.UserController;
import com.team14.logistic_company.controllers.forms.CreateClientForm;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.IUserService;
import com.team14.logistic_company.services.exceptions.EmailNotAvailable;
import com.team14.logistic_company.services.exceptions.UsernameNotAvailable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link UserController} class.
 *
 * These tests verify home, login, registration
 * and simple test endpoint behavior.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Mock
    private ClientService clientService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new UserController(
                                userService,
                                clientService,
                                userDetailsService,
                                authenticationManager
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private CreateClientForm buildValidForm() {
        CreateClientForm form = new CreateClientForm();

        form.setFirstName("Ivan");
        form.setLastName("Ivanov");
        form.setUsername("ivan123");
        form.setEmail("ivan@test.com");
        form.setPassword("password123");
        form.setConfirmPassword("password123");
        form.setPhoneNumber("0888123456");

        return form;
    }

    @Test
    void shouldShowHomePageFromRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("HomePage"));
    }

    @Test
    void shouldShowHomePageFromHomeUrl() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("HomePage"));
    }

    @Test
    void shouldShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("clientForm"));
    }

    @Test
    void shouldRegisterClientSuccessfully() {
        UserController controller =
                new UserController(
                        userService,
                        clientService,
                        userDetailsService,
                        authenticationManager
                );

        User savedUser = mock(User.class);

        when(savedUser.getId())
                .thenReturn(10);

        when(userService.create(any()))
                .thenReturn(savedUser);

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.registerSave(
                        buildValidForm(),
                        result,
                        model
                );

        assertEquals(
                "redirect:/login?registered",
                viewName
        );

        verify(userService)
                .create(any());

        verify(clientService)
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnRegisterWhenValidationFails() {
        UserController controller =
                new UserController(
                        userService,
                        clientService,
                        userDetailsService,
                        authenticationManager
                );

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        when(result.hasErrors())
                .thenReturn(true);

        String viewName =
                controller.registerSave(
                        buildValidForm(),
                        result,
                        model
                );

        assertEquals(
                "register",
                viewName
        );

        verify(userService, never())
                .create(any());

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnRegisterWhenPasswordsDoNotMatch() {
        UserController controller =
                new UserController(
                        userService,
                        clientService,
                        userDetailsService,
                        authenticationManager
                );

        CreateClientForm form =
                buildValidForm();

        form.setConfirmPassword("different123");

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.registerSave(
                        form,
                        result,
                        model
                );

        assertEquals(
                "register",
                viewName
        );

        verify(result)
                .rejectValue(
                        eq("confirmPassword"),
                        eq("error.confirmPassword"),
                        eq("Паролите не съвпадат!")
                );

        verify(userService, never())
                .create(any());

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnRegisterWhenUsernameNotAvailable() {
        UserController controller =
                new UserController(
                        userService,
                        clientService,
                        userDetailsService,
                        authenticationManager
                );

        when(userService.create(any()))
                .thenThrow(
                        new UsernameNotAvailable(
                                "Username is not available"
                        )
                );

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.registerSave(
                        buildValidForm(),
                        result,
                        model
                );

        assertEquals(
                "register",
                viewName
        );

        verify(model)
                .addAttribute(
                        "error",
                        "Username is not available"
                );

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnRegisterWhenEmailNotAvailable() {
        UserController controller =
                new UserController(
                        userService,
                        clientService,
                        userDetailsService,
                        authenticationManager
                );

        when(userService.create(any()))
                .thenThrow(
                        new EmailNotAvailable(
                                "Email is not available"
                        )
                );

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.registerSave(
                        buildValidForm(),
                        result,
                        model
                );

        assertEquals(
                "register",
                viewName
        );

        verify(model)
                .addAttribute(
                        "error",
                        "Email is not available"
                );

        verify(clientService, never())
                .create(any(ClientDto.class));
    }

    @Test
    void shouldReturnOkFromTestEndpoint() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}