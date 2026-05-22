package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.*;
import com.team14.logistic_company.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
/**
 * Controller responsible for office management operations.
 * Handles listing, viewing, creating, editing, and deleting company offices.
 */
@Controller
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;
    private final AddressService addressService;
    private final CityService cityService;
    private final CountryService countryService;
    private final EmployeeService employeeService;
    private final ShipmentService shipmentService;

    /**
     * Displays a list of all company offices with their addresses.
     *
     * @param model model used to pass office data to the view
     * @return offices list view
     */
    @GetMapping
    public String getAllOffices(Model model) {
        var offices = officeService.findAll();

        Map<Integer, String> officeAddresses = new HashMap<>();

        for (OfficeDto office : offices) {
            officeAddresses.put(office.getId(), getAddressText(office.getAddressId()));
        }

        model.addAttribute("offices", offices);
        model.addAttribute("officeAddresses", officeAddresses);

        return "offices/list";
    }

    /**
     * Displays detailed information about a specific office.
     * Includes office address, employees working in the office,
     * and shipments related to the office.
     *
     * @param id office identifier
     * @param model model used to pass office details to the view
     * @return office details view
     */
    @GetMapping("/{id}")
    public String getOfficeById(@PathVariable Integer id, Model model) {
        OfficeDto office = officeService.findById(id);

        model.addAttribute("office", office);
        model.addAttribute("addressText", getAddressText(office.getAddressId()));
        model.addAttribute("employees", employeeService.findByOfficeId(id));
        model.addAttribute("shipments", shipmentService.getShipmentsByOfficeId(id));

        return "offices/details";
    }

    /**
     * Displays the office creation form.
     *
     * @param model model used to pass an empty office object to the form
     * @return office form view
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("office", new OfficeDto());
        return "offices/form";
    }

    /**
     * Creates a new office together with its address.
     * If the provided country or city does not exist, it is created automatically.
     *
     * @param officeDto office data submitted from the form
     * @param country country name
     * @param city city name
     * @param street street name and number
     * @param postalCode postal code
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to offices list
     */
    @PostMapping
    public String createOffice(@ModelAttribute("office") OfficeDto officeDto,
                               @RequestParam String country,
                               @RequestParam String city,
                               @RequestParam String street,
                               @RequestParam String postalCode,
                               RedirectAttributes redirectAttributes) {

        CountryDto savedCountry = countryService.findOrCreateByName(country);
        CityDto savedCity = cityService.findOrCreateByNameAndCountry(city, savedCountry.getId());

        AddressDto addressDto = new AddressDto();
        addressDto.setCityId(savedCity.getId());
        addressDto.setStreet(street.trim());
        addressDto.setPostalCode(postalCode.trim());

        AddressDto savedAddress = addressService.create(addressDto);

        officeDto.setAddressId(savedAddress.getId());
        officeService.create(officeDto);

        redirectAttributes.addFlashAttribute("successMessage", "Офисът е създаден успешно!");
        return "redirect:/offices";
    }

    /**
     * Displays the office edit form with the current office and address data.
     *
     * @param id office identifier
     * @param model model used to pass office and address data to the form
     * @return office form view
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        OfficeDto office = officeService.findById(id);
        AddressDto address = addressService.findById(office.getAddressId());
        CityDto city = cityService.findById(address.getCityId());
        CountryDto country = countryService.findById(city.getCountryId());

        model.addAttribute("office", office);
        model.addAttribute("countryValue", country.getName());
        model.addAttribute("cityValue", city.getName());
        model.addAttribute("streetValue", address.getStreet());
        model.addAttribute("postalCodeValue", address.getPostalCode());
        model.addAttribute("isEdit", true);

        return "offices/form";
    }

    /**
     * Updates an existing office and assigns a newly created or found address to it.
     *
     * @param id office identifier
     * @param officeDto updated office data
     * @param country country name
     * @param city city name
     * @param street street name and number
     * @param postalCode postal code
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to offices list
     */
    @PostMapping("/update/{id}")
    public String updateOffice(@PathVariable Integer id,
                               @ModelAttribute("office") OfficeDto officeDto,
                               @RequestParam String country,
                               @RequestParam String city,
                               @RequestParam String street,
                               @RequestParam String postalCode,
                               RedirectAttributes redirectAttributes) {

        CountryDto savedCountry = countryService.findOrCreateByName(country);
        CityDto savedCity = cityService.findOrCreateByNameAndCountry(city, savedCountry.getId());

        AddressDto addressDto = new AddressDto();
        addressDto.setCityId(savedCity.getId());
        addressDto.setStreet(street.trim());
        addressDto.setPostalCode(postalCode.trim());

        AddressDto savedAddress = addressService.create(addressDto);

        officeDto.setAddressId(savedAddress.getId());
        officeService.update(id, officeDto);

        redirectAttributes.addFlashAttribute("successMessage", "Офисът е редактиран успешно!");
        return "redirect:/offices";
    }

    /**
     * Deletes an office by its identifier.
     *
     * @param id office identifier
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to offices list
     */
    @GetMapping("/delete/{id}")
    public String deleteOffice(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        officeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Офисът е изтрит успешно!");
        return "redirect:/offices";
    }

    /**
     * Builds a readable address text by address identifier.
     *
     * @param addressId address identifier
     * @return formatted address text or message when address is missing
     */
    private String getAddressText(Integer addressId) {
        if (addressId == null) {
            return "Няма адрес";
        }

        AddressDto address = addressService.findById(addressId);
        CityDto city = cityService.findById(address.getCityId());
        CountryDto country = countryService.findById(city.getCountryId());

        return country.getName() + ", " + city.getName() + ", " + address.getStreet() + ", " + address.getPostalCode();
    }
}