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
/**
 * Controller responsible for managing the logistic company information.
 * Provides pages for viewing, editing, and resetting the company details.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class LogisticCompanyController {

    private final LogisticCompanyService companyService;

    /**
     * Displays the current logistic company information.
     *
     * @param model model used to pass company data to the view
     * @return company information view
     */
    @GetMapping
    public String info(Model model) {
        model.addAttribute("company", companyService.getSingleton());
        return "company/info";
    }

    /**
     * Displays the edit form for the logistic company information.
     * Access to this page should be restricted to employees and administrators.
     *
     * @param model model used to pass company data to the view
     * @param authentication current authenticated user
     * @return company edit view
     */
    @GetMapping("/edit")
    public String edit(Model model, Authentication authentication) {
        model.addAttribute("company", companyService.getSingleton());
        return "company/edit";
    }

    /**
     * Saves the edited logistic company information.
     *
     * @param company company data submitted from the edit form
     * @param result validation result object
     * @param ra redirect attributes used for success messages
     * @return company edit view if validation fails, otherwise redirect to company information page
     */
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

    /**
     * Resets the logistic company information to its default values.
     *
     * @param ra redirect attributes used for success messages
     * @return redirect to company information page
     */
    @PostMapping("/reset")
    public String reset(RedirectAttributes ra) {
        companyService.reset();
        ra.addFlashAttribute("successMessage", "Company info reset!");
        return "redirect:/company";
    }
}