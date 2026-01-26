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

@Controller
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final CityService cityService;

    // Показване на всички адреси
    @GetMapping
    public String getAllAddresses(Model model) {
        model.addAttribute("addresses", addressService.findAll());
        return "addresses/list";
    }

    // Показване на детайли за един адрес
    @GetMapping("/{id}")
    public String getAddressById(@PathVariable Integer id, Model model) {
        model.addAttribute("address", addressService.findById(id));
        return "addresses/details";
    }

    // Адреси по град
    @GetMapping("/city/{cityId}")
    public String getAddressesByCity(@PathVariable Integer cityId, Model model) {
        model.addAttribute("addresses", addressService.findByCityId(cityId));
        model.addAttribute("city", cityService.findById(cityId));
        return "addresses/list";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("address", new AddressDto());
        model.addAttribute("cities", cityService.findAll());
        return "addresses/form";
    }

    // Обработка на създаване
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

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("address", addressService.findById(id));
        model.addAttribute("cities", cityService.findAll());
        return "addresses/form";
    }

    // Обработка на редактиране
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

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        addressService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        return "redirect:/addresses";
    }
}