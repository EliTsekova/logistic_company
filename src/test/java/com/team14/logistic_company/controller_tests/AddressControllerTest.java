package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.AddressController;
import com.team14.logistic_company.dtos.AddressDto;
import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.services.AddressService;
import com.team14.logistic_company.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link AddressController} class.
 *
 * These tests verify the web layer behavior,
 * view names, model attributes and service calls.
 */
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @Mock
    private CityService cityService;

    /**
     * Initializes MockMvc in standalone mode.
     *
     * This avoids loading the full Spring context,
     * Spring Security filters and Thymeleaf template rendering.
     */
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new AddressController(
                                addressService,
                                cityService
                        )
                )
                .build();
    }

    /**
     * Tests that all addresses are loaded
     * and the list view is returned.
     */
    @Test
    void shouldShowAllAddresses() throws Exception {

        when(addressService.findAll())
                .thenReturn(List.of(new AddressDto()));

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/list"))
                .andExpect(model().attributeExists("addresses"))
                .andExpect(model().attribute("addresses", hasSize(1)));

        verify(addressService)
                .findAll();
    }

    /**
     * Tests that address details are loaded
     * by address ID.
     */
    @Test
    void shouldShowAddressDetails() throws Exception {

        AddressDto addressDto =
                new AddressDto();

        addressDto.setId(1);
        addressDto.setStreet("Vitosha Blvd");
        addressDto.setPostalCode("1000");
        addressDto.setCityId(1);

        when(addressService.findById(1))
                .thenReturn(addressDto);

        mockMvc.perform(get("/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/details"))
                .andExpect(model().attributeExists("address"));

        verify(addressService)
                .findById(1);
    }

    /**
     * Tests that addresses can be loaded
     * by city ID.
     */
    @Test
    void shouldShowAddressesByCity() throws Exception {

        when(addressService.findByCityId(1))
                .thenReturn(List.of(new AddressDto()));

        when(cityService.findById(1))
                .thenReturn(new CityDto());

        mockMvc.perform(get("/addresses/city/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/list"))
                .andExpect(model().attributeExists("addresses"))
                .andExpect(model().attributeExists("city"));

        verify(addressService)
                .findByCityId(1);

        verify(cityService)
                .findById(1);
    }

    /**
     * Tests that the create form is shown
     * with an empty AddressDto and cities list.
     */
    @Test
    void shouldShowCreateForm() throws Exception {

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));

        mockMvc.perform(get("/addresses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/form"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("cities"));

        verify(cityService)
                .findAll();
    }

    /**
     * Tests that a valid address form submission
     * creates a new address and redirects to list page.
     */
    @Test
    void shouldCreateAddress() throws Exception {

        mockMvc.perform(post("/addresses")
                        .param("cityId", "1")
                        .param("street", "Vitosha Blvd")
                        .param("postalCode", "1000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(addressService)
                .create(any(AddressDto.class));
    }

    /**
     * Tests that invalid address form submission
     * returns the form view again.
     */
    @Test
    void shouldReturnFormWhenCreateAddressHasErrors() throws Exception {

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));

        mockMvc.perform(post("/addresses")
                        .param("cityId", "")
                        .param("street", "")
                        .param("postalCode", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/form"))
                .andExpect(model().attributeExists("cities"));

        verify(addressService, never())
                .create(any(AddressDto.class));

        verify(cityService)
                .findAll();
    }

    /**
     * Tests that the edit form is shown
     * for an existing address.
     */
    @Test
    void shouldShowEditForm() throws Exception {

        when(addressService.findById(1))
                .thenReturn(new AddressDto());

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));

        mockMvc.perform(get("/addresses/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/form"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("cities"));

        verify(addressService)
                .findById(1);

        verify(cityService)
                .findAll();
    }

    /**
     * Tests that valid update form submission
     * updates an address and redirects.
     */
    @Test
    void shouldUpdateAddress() throws Exception {

        mockMvc.perform(post("/addresses/update/1")
                        .param("cityId", "1")
                        .param("street", "Updated Street")
                        .param("postalCode", "4000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(addressService)
                .update(eq(1), any(AddressDto.class));
    }

    /**
     * Tests that invalid update form submission
     * returns the form view again.
     */
    @Test
    void shouldReturnFormWhenUpdateAddressHasErrors() throws Exception {

        when(cityService.findAll())
                .thenReturn(List.of(new CityDto()));

        mockMvc.perform(post("/addresses/update/1")
                        .param("cityId", "")
                        .param("street", "")
                        .param("postalCode", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("addresses/form"))
                .andExpect(model().attributeExists("cities"));

        verify(addressService, never())
                .update(eq(1), any(AddressDto.class));

        verify(cityService)
                .findAll();
    }

    /**
     * Tests that an address is deleted
     * and the user is redirected to the list page.
     */
    @Test
    void shouldDeleteAddress() throws Exception {

        mockMvc.perform(get("/addresses/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/addresses"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(addressService)
                .delete(1);
    }
}