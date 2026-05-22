package com.team14.logistic_company.controllers;

import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Controller responsible for client dashboard operations.
 * Provides pages for client home, shipment overview, and office information.
 */
@Controller
@RequiredArgsConstructor
public class ClientPageController {

    private final ShipmentService shipmentService;
    private final OfficeService officeService;
    private final EmployeeService employeeService;

    /**
     * Displays the client home page.
     *
     * @param model model used to pass client information to the view
     * @param authentication current authenticated client
     * @return client home view
     */
    @GetMapping("/Client")
    public String clientHome(Model model, Authentication authentication) {

        model.addAttribute("clientName", authentication.getName());

        return "ClientHome";
    }

    /**
     * Displays the client shipment dashboard.
     * Shows all shipments, sent shipments, received shipments,
     * and expected shipments for the authenticated client.
     *
     * @param model model used to pass shipment data and statistics to the view
     * @param authentication current authenticated client
     * @return client dashboard view
     */
    @GetMapping("/Client/shipments")
    public String clientDashboard(Model model, Authentication authentication) {

        var shipments = shipmentService.getShipmentsForClient(authentication);
        var sentShipments = shipmentService.getSentByClient(authentication);
        var receivedShipments = shipmentService.getReceivedByClient(authentication);
        var expectedShipments = shipmentService.getExpectedByClient(authentication);

        model.addAttribute("shipments", shipments.stream()
                .map(shipment -> shipmentService.findByIdForView(shipment.getId(), authentication))
                .toList());

        model.addAttribute("sentShipments", sentShipments.stream()
                .map(shipment -> shipmentService.findByIdForView(shipment.getId(), authentication))
                .toList());

        model.addAttribute("receivedShipments", receivedShipments.stream()
                .map(shipment -> shipmentService.findByIdForView(shipment.getId(), authentication))
                .toList());

        model.addAttribute("expectedShipments", expectedShipments.stream()
                .map(shipment -> shipmentService.findByIdForView(shipment.getId(), authentication))
                .toList());

        model.addAttribute("totalShipments", shipments.size());
        model.addAttribute("sentCount", sentShipments.size());
        model.addAttribute("receivedCount", receivedShipments.size());
        model.addAttribute("expectedCount", expectedShipments.size());

        model.addAttribute("clientName", authentication.getName());
        model.addAttribute("activeTab", "all");

        return "Client";
    }

    /**
     * Displays company offices and employees available to clients.
     *
     * @param model model used to pass office and employee data to the view
     * @param authentication current authenticated client
     * @return client offices view
     */
    @GetMapping("/Client/offices")
    public String clientOffices(Model model, Authentication authentication) {

        model.addAttribute("clientName", authentication.getName());
        model.addAttribute("offices", officeService.findAll());
        model.addAttribute("employees", employeeService.findAll());

        return "ClientOffices";
    }
}