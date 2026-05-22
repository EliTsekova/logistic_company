package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.CountryDto;
import com.team14.logistic_company.services.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Controller responsible for country management operations.
 * Handles listing, viewing, creating, editing, and deleting countries.
 */
@Controller
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    /**
     * Displays a list of all countries.
     *
     * @param model model used to pass country data to the view
     * @return countries list view
     */
    @GetMapping
    public String getAllCountries(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "countries/list";
    }

    /**
     * Displays detailed information about a specific country.
     *
     * @param id country identifier
     * @param model model used to pass country details to the view
     * @return country details view
     */
    @GetMapping("/{id}")
    public String getCountryById(@PathVariable Integer id, Model model) {
        model.addAttribute("country", countryService.findById(id));
        return "countries/details";
    }

    /**
     * Displays the country creation form.
     *
     * @param model model used to pass an empty country object to the form
     * @return country form view
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("country", new CountryDto());
        return "countries/form";
    }

    /**
     * Creates a new country.
     *
     * @param countryDto country data submitted from the form
     * @param result validation result object
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to countries list after successful creation, or form view on validation error
     */
    @PostMapping
    public String createCountry(@Valid @ModelAttribute("country") CountryDto countryDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "countries/form";
        }

        countryService.create(countryDto);
        redirectAttributes.addFlashAttribute("successMessage", "Country created successfully!");
        return "redirect:/countries";
    }

    /**
     * Displays the country edit form.
     *
     * @param id country identifier
     * @param model model used to pass country data to the form
     * @return country form view
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("country", countryService.findById(id));
        return "countries/form";
    }

    /**
     * Updates an existing country.
     *
     * @param id country identifier
     * @param countryDto updated country data
     * @param result validation result object
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to countries list after successful update, or form view on validation error
     */
    @PostMapping("/update/{id}")
    public String updateCountry(@PathVariable Integer id,
                                @Valid @ModelAttribute("country") CountryDto countryDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "countries/form";
        }

        countryService.update(id, countryDto);
        redirectAttributes.addFlashAttribute("successMessage", "Country updated successfully!");
        return "redirect:/countries";
    }

    /**
     * Deletes a country by its identifier.
     *
     * @param id country identifier
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to countries list
     */
    @GetMapping("/delete/{id}")
    public String deleteCountry(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        countryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Country deleted successfully!");
        return "redirect:/countries";
    }
}