package com.team14.logistic_company.controllers;


import com.team14.logistic_company.entities.LogisticCompany;
import com.team14.logistic_company.services.LogisticCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class LogisticCompanyController {

    private final LogisticCompanyService companyService;

    @GetMapping
    public String info(Model model) {
        model.addAttribute("company", companyService.getSingleton());
        return "company/info";
    }

    @GetMapping("/edit")
    public String edit(Model model, Authentication authentication) {
        // SecurityConfig трябва да ограничи този URL до EMPLOYEE/ADMIN
        model.addAttribute("company", companyService.getSingleton());
        return "company/edit";
    }

    @PostMapping("/edit")
    public String editSave(@Valid @ModelAttribute("company") LogisticCompany company,
                           BindingResult result,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "company/edit";
        }

        companyService.update(company);
        ra.addFlashAttribute("successMessage", "Company info updated!");
        return "redirect:/company";
    }

    @PostMapping("/reset")
    public String reset(RedirectAttributes ra) {
        companyService.reset();
        ra.addFlashAttribute("successMessage", "Company info reset!");
        return "redirect:/company";
    }
}
