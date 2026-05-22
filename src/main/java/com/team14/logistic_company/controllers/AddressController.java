package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.AddressDto;
import com.team14.logistic_company.services.AddressService;
import com.team14.logistic_company.services.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Controller responsible for address management operations.
 * Handles listing, viewing, creating, editing, deleting,
 * and filtering addresses by city.
 */
@Controller
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final CityService cityService;

    /**
     * Displays a list of all addresses.
     *
     * @param model model used to pass address data to the view
     * @return addresses list view
     */
    @GetMapping
    public String getAllAddresses(Model model) {
        model.addAttribute("addresses", addressService.findAll());
        return "addresses/list";
    }

    /**
     * Displays detailed information about a specific address.
     *
     * @param id address identifier
     * @param model model used to pass address details to the view
     * @return address details view
     */
    @GetMapping("/{id}")
    public String getAddressById(@PathVariable Integer id, Model model) {
        model.addAttribute("address", addressService.findById(id));
        return "addresses/details";
    }

    /**
     * Displays all addresses belonging to a specific city.
     *
     * @param cityId city identifier
     * @param model model used to pass address and city data to the view
     * @return addresses list view
     */
    @GetMapping("/city/{cityId}")
    public String getAddressesByCity(@PathVariable Integer cityId, Model model) {
        model.addAttribute("addresses", addressService.findByCityId(cityId));
        model.addAttribute("city", cityService.findById(cityId));
        return "addresses/list";
    }

    /**
     * Displays the address creation form.
     *
     * @param model model used to pass form data and available cities to the view
     * @return address form view
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("address", new AddressDto());
        model.addAttribute("cities", cityService.findAll());
        return "addresses/form";
    }

    /**
     * Creates a new address.
     *
     * @param addressDto address data submitted from the form
     * @param result validation result object
     * @param model model used to reload form data in case of validation errors
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to addresses list after successful creation, or form view on validation error
     */
    @PostMapping
    public String createAddress(@Valid @ModelAttribute("address") AddressDto addressDto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.findAll());
            return "addresses/form";
        }

        addressService.create(addressDto);
        redirectAttributes.addFlashAttribute("successMessage", "Address created successfully!");
        return "redirect:/addresses";
    }

    /**
     * Displays the address edit form.
     *
     * @param id address identifier
     * @param model model used to pass address data and available cities to the view
     * @return address form view
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("address", addressService.findById(id));
        model.addAttribute("cities", cityService.findAll());
        return "addresses/form";
    }

    /**
     * Updates an existing address.
     *
     * @param id address identifier
     * @param addressDto updated address data
     * @param result validation result object
     * @param model model used to reload form data in case of validation errors
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to addresses list after successful update, or form view on validation error
     */
    @PostMapping("/update/{id}")
    public String updateAddress(@PathVariable Integer id,
                                @Valid @ModelAttribute("address") AddressDto addressDto,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.findAll());
            return "addresses/form";
        }

        addressService.update(id, addressDto);
        redirectAttributes.addFlashAttribute("successMessage", "Address updated successfully!");
        return "redirect:/addresses";
    }

    /**
     * Deletes an address by its identifier.
     *
     * @param id address identifier
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to addresses list
     */
    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        addressService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        return "redirect:/addresses";
    }
}