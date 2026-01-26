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

@Controller
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final CountryService countryService;

    // Показване на всички градове
    @GetMapping
    public String getAllCities(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "cities/list";
    }

    // Показване на детайли за един град
    @GetMapping("/{id}")
    public String getCityById(@PathVariable Integer id, Model model) {
        model.addAttribute("city", cityService.findById(id));
        return "cities/details";
    }

    // Градове по държава
    @GetMapping("/country/{countryId}")
    public String getCitiesByCountry(@PathVariable Integer countryId, Model model) {
        model.addAttribute("cities", cityService.findByCountryId(countryId));
        model.addAttribute("country", countryService.findById(countryId));
        return "cities/list";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("city", new CityDto());
        model.addAttribute("countries", countryService.findAll());
        return "cities/form";
    }

    // Обработка на създаване
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

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("city", cityService.findById(id));
        model.addAttribute("countries", countryService.findAll());
        return "cities/form";
    }

    // Обработка на редактиране
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

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        cityService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "City deleted successfully!");
        return "redirect:/cities";
    }
}