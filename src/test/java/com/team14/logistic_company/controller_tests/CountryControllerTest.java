package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.CountryController;
import com.team14.logistic_company.dtos.CountryDto;
import com.team14.logistic_company.services.CountryService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link CountryController} class.
 *
 * These tests verify country controller routes,
 * view names, model attributes and service calls.
 */
@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @BeforeEach
    void setUp() {

        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new CountryController(countryService)
                )
                .setSingleView(mockView)
                .build();
    }

    /**
     * Tests that all countries are loaded
     * and the list view is returned.
     */
    @Test
    void shouldShowAllCountries() throws Exception {

        when(countryService.findAll())
                .thenReturn(List.of(new CountryDto()));

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/list"))
                .andExpect(model().attributeExists("countries"))
                .andExpect(model().attribute("countries", hasSize(1)));

        verify(countryService)
                .findAll();
    }

    /**
     * Tests that country details are loaded
     * by country ID.
     */
    @Test
    void shouldShowCountryDetails() throws Exception {

        CountryDto countryDto =
                new CountryDto();

        countryDto.setId(1);
        countryDto.setName("Bulgaria");

        when(countryService.findById(1))
                .thenReturn(countryDto);

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/details"))
                .andExpect(model().attributeExists("country"));

        verify(countryService)
                .findById(1);
    }

    /**
     * Tests that the create country form
     * is shown successfully.
     */
    @Test
    void shouldShowCreateForm() throws Exception {

        mockMvc.perform(get("/countries/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/form"))
                .andExpect(model().attributeExists("country"));
    }

    /**
     * Tests that a valid country form submission
     * creates a country and redirects to list page.
     */
    @Test
    void shouldCreateCountry() {
        CountryController controller =
                new CountryController(countryService);

        CountryDto countryDto = new CountryDto();
        countryDto.setName("Bulgaria");

        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.createCountry(
                        countryDto,
                        result,
                        redirectAttributes
                );

        assertEquals("redirect:/countries", viewName);

        verify(countryService)
                .create(any(CountryDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Country created successfully!")
                );
    }

    /**
     * Tests that invalid country form submission
     * returns the form view.
     */
    @Test
    void shouldReturnFormWhenCreateCountryHasErrors() throws Exception {

        mockMvc.perform(post("/countries")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/form"));

        verify(countryService, never())
                .create(any(CountryDto.class));
    }

    /**
     * Tests that the edit form is shown
     * for an existing country.
     */
    @Test
    void shouldShowEditForm() throws Exception {

        CountryDto countryDto =
                new CountryDto();

        countryDto.setId(1);
        countryDto.setName("Bulgaria");

        when(countryService.findById(1))
                .thenReturn(countryDto);

        mockMvc.perform(get("/countries/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/form"))
                .andExpect(model().attributeExists("country"));

        verify(countryService)
                .findById(1);
    }

    /**
     * Tests that a valid update form submission
     * updates a country and redirects.
     */
    @Test
    void shouldUpdateCountry() {
        CountryController controller =
                new CountryController(countryService);

        CountryDto countryDto = new CountryDto();
        countryDto.setId(1);
        countryDto.setName("Germany");

        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateCountry(
                        1,
                        countryDto,
                        result,
                        redirectAttributes
                );

        assertEquals("redirect:/countries", viewName);

        verify(countryService)
                .update(eq(1), any(CountryDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Country updated successfully!")
                );
    }

    /**
     * Tests that invalid update form submission
     * returns the form view.
     */
    @Test
    void shouldReturnFormWhenUpdateCountryHasErrors() throws Exception {

        mockMvc.perform(post("/countries/update/1")
                        .param("id", "1")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("countries/form"));

        verify(countryService, never())
                .update(eq(1), any(CountryDto.class));
    }

    /**
     * Tests that a country is deleted
     * and the user is redirected.
     */
    @Test
    void shouldDeleteCountry() {
        CountryController controller =
                new CountryController(countryService);

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.deleteCountry(
                        1,
                        redirectAttributes
                );

        assertEquals("redirect:/countries", viewName);

        verify(countryService)
                .delete(1);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Country deleted successfully!")
                );
    }
}