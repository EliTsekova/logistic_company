package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.CityController;
import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.dtos.CountryDto;
import com.team14.logistic_company.services.CityService;
import com.team14.logistic_company.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link CityController} class.
 *
 * These tests verify view rendering,
 * model attributes and service interactions.
 */
@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CityService cityService;

    @Mock
    private CountryService countryService;

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
                        new CityController(
                                cityService,
                                countryService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    /**
     * Tests that all cities are loaded
     * and the list view is returned.
     */
    @Test
    void shouldShowAllCities() throws Exception {

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/list"))
                .andExpect(model().attributeExists("cities"))
                .andExpect(model().attribute("cities", hasSize(1)));

        verify(cityService)
                .findAll();
    }

    /**
     * Tests that city details are loaded
     * by city ID.
     */
    @Test
    void shouldShowCityDetails() throws Exception {

        CityDto cityDto =
                new CityDto();

        cityDto.setId(1);
        cityDto.setName("Sofia");
        cityDto.setCountryId(1);

        when(cityService.findById(1))
                .thenReturn(cityDto);

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/details"))
                .andExpect(model().attributeExists("city"));

        verify(cityService)
                .findById(1);
    }

    /**
     * Tests that cities are loaded
     * by country ID.
     */
    @Test
    void shouldShowCitiesByCountry() throws Exception {

        when(cityService.findByCountryId(1))
                .thenReturn(List.of(new CityDto()));

        when(countryService.findById(1))
                .thenReturn(new CountryDto());

        mockMvc.perform(get("/cities/country/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/list"))
                .andExpect(model().attributeExists("cities"))
                .andExpect(model().attributeExists("country"));

        verify(cityService)
                .findByCountryId(1);

        verify(countryService)
                .findById(1);
    }

    /**
     * Tests that the create form is shown
     * with an empty CityDto and countries list.
     */
    @Test
    void shouldShowCreateForm() throws Exception {

        when(countryService.findAll())
                .thenReturn(List.of(new CountryDto()));

        mockMvc.perform(get("/cities/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/form"))
                .andExpect(model().attributeExists("city"))
                .andExpect(model().attributeExists("countries"));

        verify(countryService)
                .findAll();
    }

    /**
     * Tests that a valid city form submission
     * creates a new city and redirects to list page.
     */
    @Test
    void shouldCreateCity() {

        CityController controller =
                new CityController(
                        cityService,
                        countryService
                );

        CityDto cityDto =
                new CityDto();

        cityDto.setName("Sofia");
        cityDto.setCountryId(1);

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.createCity(
                        cityDto,
                        result,
                        model,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/cities",
                viewName
        );

        verify(cityService)
                .create(any(CityDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("City created successfully!")
                );
    }

    /**
     * Tests that invalid city form submission
     * returns the form view again.
     */
    @Test
    void shouldReturnFormWhenCreateCityHasErrors() throws Exception {

        when(countryService.findAll())
                .thenReturn(List.of(new CountryDto()));

        mockMvc.perform(post("/cities")
                        .param("name", "")
                        .param("countryId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/form"))
                .andExpect(model().attributeExists("countries"));

        verify(cityService, never())
                .create(any(CityDto.class));

        verify(countryService)
                .findAll();
    }

    /**
     * Tests that the edit form is shown
     * for an existing city.
     */
    @Test
    void shouldShowEditForm() throws Exception {

        when(cityService.findById(1))
                .thenReturn(new CityDto());

        when(countryService.findAll())
                .thenReturn(List.of(new CountryDto()));

        mockMvc.perform(get("/cities/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/form"))
                .andExpect(model().attributeExists("city"))
                .andExpect(model().attributeExists("countries"));

        verify(cityService)
                .findById(1);

        verify(countryService)
                .findAll();
    }

    /**
     * Tests that valid update form submission
     * updates a city and redirects.
     */
    @Test
    void shouldUpdateCity() {

        CityController controller =
                new CityController(
                        cityService,
                        countryService
                );

        CityDto cityDto =
                new CityDto();

        cityDto.setId(1);
        cityDto.setName("Plovdiv");
        cityDto.setCountryId(1);

        BindingResult result =
                mock(BindingResult.class);

        Model model =
                mock(Model.class);

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        when(result.hasErrors())
                .thenReturn(false);

        String viewName =
                controller.updateCity(
                        1,
                        cityDto,
                        result,
                        model,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/cities",
                viewName
        );

        verify(cityService)
                .update(eq(1), any(CityDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("City updated successfully!")
                );
    }

    /**
     * Tests that invalid update form submission
     * returns the form view again.
     */
    @Test
    void shouldReturnFormWhenUpdateCityHasErrors() throws Exception {

        when(countryService.findAll())
                .thenReturn(List.of(new CountryDto()));

        mockMvc.perform(post("/cities/update/1")
                        .param("name", "")
                        .param("countryId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("cities/form"))
                .andExpect(model().attributeExists("countries"));

        verify(cityService, never())
                .update(eq(1), any(CityDto.class));

        verify(countryService)
                .findAll();
    }

    /**
     * Tests that a city is deleted
     * and the user is redirected.
     */
    @Test
    void shouldDeleteCity() {

        CityController controller =
                new CityController(
                        cityService,
                        countryService
                );

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.deleteCity(
                        1,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/cities",
                viewName
        );

        verify(cityService)
                .delete(1);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("City deleted successfully!")
                );
    }
}