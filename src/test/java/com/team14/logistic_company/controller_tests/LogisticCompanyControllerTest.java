package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.LogisticCompanyController;
import com.team14.logistic_company.entities.LogisticCompany;
import com.team14.logistic_company.services.LogisticCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link LogisticCompanyController} class.
 *
 * These tests verify company information pages,
 * edit functionality and service interactions.
 */
@ExtendWith(MockitoExtension.class)
class LogisticCompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LogisticCompanyService companyService;

    @BeforeEach
    void setUp() {

        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new LogisticCompanyController(
                                companyService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    /**
     * Creates valid LogisticCompany instance.
     */
    private LogisticCompany buildCompany() {

        LogisticCompany company =
                new LogisticCompany();

        company.setId(1);
        company.setName("Logistic Express");
        company.setEmail("office@test.com");

        return company;
    }

    /**
     * Tests that company information page loads successfully.
     */
    @Test
    void shouldShowCompanyInfo() throws Exception {

        when(companyService.getSingleton())
                .thenReturn(buildCompany());

        mockMvc.perform(get("/company"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/info"))
                .andExpect(model().attributeExists("company"));

        verify(companyService)
                .getSingleton();
    }

    /**
     * Tests that edit form loads successfully.
     */
    @Test
    void shouldShowEditForm() throws Exception {

        when(companyService.getSingleton())
                .thenReturn(buildCompany());

        mockMvc.perform(get("/company/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/edit"))
                .andExpect(model().attributeExists("company"));

        verify(companyService)
                .getSingleton();
    }

    /**
     * Tests that valid company edit updates company info.
     */
    @Test
    void shouldResetCompanyInfo() {
        LogisticCompanyController controller =
                new LogisticCompanyController(companyService);

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.reset(redirectAttributes);

        assertEquals("redirect:/company", viewName);

        verify(companyService)
                .reset();

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Company info reset!")
                );
    }

    /**
     * Tests that invalid edit form returns edit page again.
     */
    @Test
    void shouldReturnEditFormWhenValidationFails() throws Exception {

        mockMvc.perform(post("/company/edit")
                        .param("id", "1")
                        .param("name", "")
                        .param("email", "invalid-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/edit"));

        verify(companyService, never())
                .update(any(LogisticCompany.class));
    }

    /**
     * Tests that company reset operation works successfully.
     */
    @Test
    void shouldUpdateCompanyInfo() {
        LogisticCompanyController controller =
                new LogisticCompanyController(companyService);

        LogisticCompany company = buildCompany();
        company.setName("Updated Company");
        company.setEmail("updated@test.com");

        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.editSave(
                        company,
                        result,
                        redirectAttributes
                );

        assertEquals("redirect:/company", viewName);

        verify(companyService)
                .update(any(LogisticCompany.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Company info updated!")
                );
    }
}