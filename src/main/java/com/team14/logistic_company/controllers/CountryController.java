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

@Controller
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // Показване на всички държави
    @GetMapping
    public String getAllCountries(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "countries/list";
    }

    // Показване на детайли за една държава
    @GetMapping("/{id}")
    public String getCountryById(@PathVariable Integer id, Model model) {
        model.addAttribute("country", countryService.findById(id));
        return "countries/details";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("country", new CountryDto());
        return "countries/form";
    }

    // Обработка на създаване
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

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("country", countryService.findById(id));
        return "countries/form";
    }

    // Обработка на редактиране
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

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteCountry(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        countryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Country deleted successfully!");
        return "redirect:/countries";
    }
}