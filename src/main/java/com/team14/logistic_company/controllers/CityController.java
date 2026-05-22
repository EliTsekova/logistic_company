package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.services.CityService;
import com.team14.logistic_company.services.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Controller responsible for city management operations.
 * Handles listing, viewing, creating, editing, deleting,
 * and filtering cities by country.
 */
@Controller
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final CountryService countryService;

    /**
     * Displays a list of all cities.
     *
     * @param model model used to pass city data to the view
     * @return cities list view
     */
    @GetMapping
    public String getAllCities(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "cities/list";
    }

    /**
     * Displays detailed information about a specific city.
     *
     * @param id city identifier
     * @param model model used to pass city details to the view
     * @return city details view
     */
    @GetMapping("/{id}")
    public String getCityById(@PathVariable Integer id, Model model) {
        model.addAttribute("city", cityService.findById(id));
        return "cities/details";
    }

    /**
     * Displays all cities belonging to a specific country.
     *
     * @param countryId country identifier
     * @param model model used to pass city and country data to the view
     * @return cities list view
     */
    @GetMapping("/country/{countryId}")
    public String getCitiesByCountry(@PathVariable Integer countryId, Model model) {
        model.addAttribute("cities", cityService.findByCountryId(countryId));
        model.addAttribute("country", countryService.findById(countryId));
        return "cities/list";
    }

    /**
     * Displays the city creation form.
     *
     * @param model model used to pass form data and countries list to the view
     * @return city form view
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("city", new CityDto());
        model.addAttribute("countries", countryService.findAll());
        return "cities/form";
    }

    /**
     * Creates a new city.
     *
     * @param cityDto city data submitted from the form
     * @param result validation result object
     * @param model model used to reload form data in case of validation errors
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to cities list after successful creation, or form view on validation error
     */
    @PostMapping
    public String createCity(@Valid @ModelAttribute("city") CityDto cityDto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("countries", countryService.findAll());
            return "cities/form";
        }

        cityService.create(cityDto);
        redirectAttributes.addFlashAttribute("successMessage", "City created successfully!");
        return "redirect:/cities";
    }

    /**
     * Displays the city edit form.
     *
     * @param id city identifier
     * @param model model used to pass city data and countries list to the view
     * @return city form view
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("city", cityService.findById(id));
        model.addAttribute("countries", countryService.findAll());
        return "cities/form";
    }

    /**
     * Updates an existing city.
     *
     * @param id city identifier
     * @param cityDto updated city data
     * @param result validation result object
     * @param model model used to reload form data in case of validation errors
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to cities list after successful update, or form view on validation error
     */
    @PostMapping("/update/{id}")
    public String updateCity(@PathVariable Integer id,
                             @Valid @ModelAttribute("city") CityDto cityDto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("countries", countryService.findAll());
            return "cities/form";
        }

        cityService.update(id, cityDto);
        redirectAttributes.addFlashAttribute("successMessage", "City updated successfully!");
        return "redirect:/cities";
    }

    /**
     * Deletes a city by its identifier.
     *
     * @param id city identifier
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to cities list
     */
    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        cityService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "City deleted successfully!");
        return "redirect:/cities";
    }
}