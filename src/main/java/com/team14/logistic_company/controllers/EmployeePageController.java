package com.team14.logistic_company.controllers;

import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controller responsible for employee dashboard navigation.
 * Redirects employees based on their position type and displays
 * coordinator and deliveryman dashboard pages.
 */
@Controller
@RequiredArgsConstructor
public class EmployeePageController {

    private final ShipmentService shipmentService;
    private final EmployeeService employeeService;

    /**
     * Redirects authenticated employees to the appropriate dashboard
     * according to their position type.
     *
     * @param authentication current authenticated employee
     * @return redirect to coordinator dashboard, deliveryman dashboard, or home page
     */
    @GetMapping("/Employee")
    public String employeeRedirect(Authentication authentication) {
        var employee = employeeService.findByUsername(authentication.getName());

        if (employee.getPositionType() == PositionType.COORDINATOR) {
            return "redirect:/Coordinator";
        }

        if (employee.getPositionType() == PositionType.DELIVERYMAN) {
            return "redirect:/Deliveryman";
        }

        return "redirect:/";
    }

    /**
     * Displays the coordinator dashboard with shipment statistics
     * and shipment information.
     *
     * @param model model used to pass dashboard data to the view
     * @param authentication current authenticated coordinator
     * @return coordinator dashboard view
     */
    @GetMapping("/Coordinator")
    public String coordinatorDashboard(Model model, Authentication authentication) {
        var employee = employeeService.findByUsername(authentication.getName());
        var shipments = shipmentService.findAllForView(authentication);
        var undeliveredShipments = shipmentService.getUndeliveredShipments();

        model.addAttribute("employee", employee);
        model.addAttribute("employeeId", employee.getId());

        model.addAttribute("shipments", shipments);
        model.addAttribute("totalShipments", shipments.size());
        model.addAttribute("undeliveredCount", undeliveredShipments.size());

        return "Coordinator";
    }

    /**
     * Displays the deliveryman dashboard with shipment statistics
     * and shipment information.
     *
     * @param model model used to pass dashboard data to the view
     * @param authentication current authenticated deliveryman
     * @return deliveryman dashboard view
     */
    @GetMapping("/Deliveryman")
    public String deliverymanDashboard(Model model, Authentication authentication) {
        var employee = employeeService.findByUsername(authentication.getName());
        var shipments = shipmentService.findAllForView(authentication);
        var undeliveredShipments = shipmentService.getUndeliveredShipments();

        model.addAttribute("employee", employee);
        model.addAttribute("employeeId", employee.getId());

        model.addAttribute("shipments", shipments);
        model.addAttribute("totalShipments", shipments.size());
        model.addAttribute("undeliveredCount", undeliveredShipments.size());

        return "Deliveryman";
    }
}