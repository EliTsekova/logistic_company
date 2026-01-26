package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;
    private final AddressService addressService;

    // Показване на всички офиси
    @GetMapping
    public String getAllOffices(Model model) {
        model.addAttribute("offices", officeService.findAll());
        return "offices/list";
    }

    // Показване на детайли за един офис
    @GetMapping("/{id}")
    public String getOfficeById(@PathVariable Integer id, Model model) {
        model.addAttribute("office", officeService.findById(id));
        return "offices/details";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("office", new OfficeDto());
        model.addAttribute("addresses", addressService.findAll());
        return "offices/form";
    }

    // Обработка на създаване
    @PostMapping
    public String createOffice(@Valid @ModelAttribute("office") OfficeDto officeDto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("addresses", addressService.findAll());
            return "offices/form";
        }

        officeService.create(officeDto);
        redirectAttributes.addFlashAttribute("successMessage", "Office created successfully!");
        return "redirect:/offices";
    }

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("office", officeService.findById(id));
        model.addAttribute("addresses", addressService.findAll());
        return "offices/form";
    }

    // Обработка на редактиране
    @PostMapping("/update/{id}")
    public String updateOffice(@PathVariable Integer id,
                               @Valid @ModelAttribute("office") OfficeDto officeDto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("addresses", addressService.findAll());
            return "offices/form";
        }

        officeService.update(id, officeDto);
        redirectAttributes.addFlashAttribute("successMessage", "Office updated successfully!");
        return "redirect:/offices";
    }

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteOffice(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        officeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Office deleted successfully!");
        return "redirect:/offices";
    }
}