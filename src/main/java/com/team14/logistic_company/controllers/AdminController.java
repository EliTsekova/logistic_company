package com.team14.logistic_company.controllers;

import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controller responsible for the administrator dashboard.
 * Displays general statistics about employees, clients,
 * offices, and shipments in the system.
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final OfficeService officeService;
    private final ShipmentService shipmentService;

    /**
     * Displays the administrator dashboard page.
     * Shows system statistics including counts of employees,
     * clients, offices, and shipments.
     *
     * @param model model used to pass dashboard statistics to the view
     * @param authentication current authenticated administrator
     * @return administrator dashboard view
     */
    @GetMapping("/Admin")
    public String adminPage(Model model, Authentication authentication) {

        model.addAttribute("adminUsername", authentication.getName());

        model.addAttribute("employeesCount", employeeService.findAll().size());
        model.addAttribute("clientsCount", clientService.findAll().size());
        model.addAttribute("officesCount", officeService.findAll().size());
        model.addAttribute("shipmentsCount", shipmentService.getAllShipmentsForEmployee(authentication).size());

        return "Admin";
    }
}