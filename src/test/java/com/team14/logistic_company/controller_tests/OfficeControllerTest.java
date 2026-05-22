package com.team14.logistic_company.controller_tests;

import com.team14.logistic_company.controllers.OfficeController;
import com.team14.logistic_company.dtos.*;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the {@link OfficeController} class.
 *
 * These tests verify office controller routes,
 * view names, model attributes and service calls.
 */
@ExtendWith(MockitoExtension.class)
class OfficeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OfficeService officeService;

    @Mock
    private AddressService addressService;

    @Mock
    private CityService cityService;

    @Mock
    private CountryService countryService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        View mockView = (model, request, response) -> {
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new OfficeController(
                                officeService,
                                addressService,
                                cityService,
                                countryService,
                                employeeService,
                                shipmentService
                        )
                )
                .setSingleView(mockView)
                .build();
    }

    private OfficeDto buildOfficeDto() {
        OfficeDto dto = new OfficeDto();

        dto.setId(1);
        dto.setTitle("Office Sofia");
        dto.setAddressId(1);
        dto.setCityId(1);

        return dto;
    }

    private AddressDto buildAddressDto() {
        AddressDto dto = new AddressDto();

        dto.setId(1);
        dto.setCityId(1);
        dto.setStreet("Vitosha Blvd");
        dto.setPostalCode("1000");

        return dto;
    }

    private CityDto buildCityDto() {
        CityDto dto = new CityDto();

        dto.setId(1);
        dto.setName("Sofia");
        dto.setCountryId(1);

        return dto;
    }

    private CountryDto buildCountryDto() {
        CountryDto dto = new CountryDto();

        dto.setId(1);
        dto.setName("Bulgaria");

        return dto;
    }

    private void mockAddressTextData() {
        when(addressService.findById(1))
                .thenReturn(buildAddressDto());

        when(cityService.findById(1))
                .thenReturn(buildCityDto());

        when(countryService.findById(1))
                .thenReturn(buildCountryDto());
    }

    @Test
    void shouldShowAllOffices() throws Exception {
        when(officeService.findAll())
                .thenReturn(List.of(buildOfficeDto()));

        mockAddressTextData();

        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(view().name("offices/list"))
                .andExpect(model().attributeExists("offices"))
                .andExpect(model().attributeExists("officeAddresses"))
                .andExpect(model().attribute("offices", hasSize(1)));

        verify(officeService)
                .findAll();

        verify(addressService)
                .findById(1);

        verify(cityService)
                .findById(1);

        verify(countryService)
                .findById(1);
    }

    @Test
    void shouldShowOfficeDetails() throws Exception {
        when(officeService.findById(1))
                .thenReturn(buildOfficeDto());

        mockAddressTextData();

        when(employeeService.findByOfficeId(1))
                .thenReturn(List.of(new EmployeeDto()));

        when(shipmentService.getShipmentsByOfficeId(1))
                .thenReturn(List.of(new Shipment()));

        mockMvc.perform(get("/offices/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("offices/details"))
                .andExpect(model().attributeExists("office"))
                .andExpect(model().attributeExists("addressText"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("shipments"));

        verify(officeService)
                .findById(1);

        verify(employeeService)
                .findByOfficeId(1);

        verify(shipmentService)
                .getShipmentsByOfficeId(1);
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/offices/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("offices/form"))
                .andExpect(model().attributeExists("office"));
    }

    @Test
    void shouldCreateOffice() {
        OfficeController controller =
                new OfficeController(
                        officeService,
                        addressService,
                        cityService,
                        countryService,
                        employeeService,
                        shipmentService
                );

        OfficeDto officeDto = buildOfficeDto();

        when(countryService.findOrCreateByName("Bulgaria"))
                .thenReturn(buildCountryDto());

        when(cityService.findOrCreateByNameAndCountry("Sofia", 1))
                .thenReturn(buildCityDto());

        when(addressService.create(any(AddressDto.class)))
                .thenReturn(buildAddressDto());

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.createOffice(
                        officeDto,
                        "Bulgaria",
                        "Sofia",
                        "Vitosha Blvd",
                        "1000",
                        redirectAttributes
                );

        assertEquals(
                "redirect:/offices",
                viewName
        );

        verify(countryService)
                .findOrCreateByName("Bulgaria");

        verify(cityService)
                .findOrCreateByNameAndCountry("Sofia", 1);

        verify(addressService)
                .create(any(AddressDto.class));

        verify(officeService)
                .create(any(OfficeDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Офисът е създаден успешно!")
                );
    }

    @Test
    void shouldShowEditForm() throws Exception {
        when(officeService.findById(1))
                .thenReturn(buildOfficeDto());

        when(addressService.findById(1))
                .thenReturn(buildAddressDto());

        when(cityService.findById(1))
                .thenReturn(buildCityDto());

        when(countryService.findById(1))
                .thenReturn(buildCountryDto());

        mockMvc.perform(get("/offices/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("offices/form"))
                .andExpect(model().attributeExists("office"))
                .andExpect(model().attribute("countryValue", "Bulgaria"))
                .andExpect(model().attribute("cityValue", "Sofia"))
                .andExpect(model().attribute("streetValue", "Vitosha Blvd"))
                .andExpect(model().attribute("postalCodeValue", "1000"))
                .andExpect(model().attribute("isEdit", true));

        verify(officeService)
                .findById(1);
    }

    @Test
    void shouldUpdateOffice() {
        OfficeController controller =
                new OfficeController(
                        officeService,
                        addressService,
                        cityService,
                        countryService,
                        employeeService,
                        shipmentService
                );

        OfficeDto officeDto = buildOfficeDto();

        when(countryService.findOrCreateByName("Bulgaria"))
                .thenReturn(buildCountryDto());

        when(cityService.findOrCreateByNameAndCountry("Sofia", 1))
                .thenReturn(buildCityDto());

        when(addressService.create(any(AddressDto.class)))
                .thenReturn(buildAddressDto());

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.updateOffice(
                        1,
                        officeDto,
                        "Bulgaria",
                        "Sofia",
                        "Vitosha Blvd",
                        "1000",
                        redirectAttributes
                );

        assertEquals(
                "redirect:/offices",
                viewName
        );

        verify(countryService)
                .findOrCreateByName("Bulgaria");

        verify(cityService)
                .findOrCreateByNameAndCountry("Sofia", 1);

        verify(addressService)
                .create(any(AddressDto.class));

        verify(officeService)
                .update(eq(1), any(OfficeDto.class));

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Офисът е редактиран успешно!")
                );
    }

    @Test
    void shouldDeleteOffice() {
        OfficeController controller =
                new OfficeController(
                        officeService,
                        addressService,
                        cityService,
                        countryService,
                        employeeService,
                        shipmentService
                );

        RedirectAttributes redirectAttributes =
                mock(RedirectAttributes.class);

        String viewName =
                controller.deleteOffice(
                        1,
                        redirectAttributes
                );

        assertEquals(
                "redirect:/offices",
                viewName
        );

        verify(officeService)
                .delete(1);

        verify(redirectAttributes)
                .addFlashAttribute(
                        eq("successMessage"),
                        eq("Офисът е изтрит успешно!")
                );
    }
}