package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.enums.DeliveryType;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
/**
 * Controller responsible for shipment management operations.
 * Handles shipment creation, editing, deletion, status updates,
 * shipment reports, and shipment history visualization.
 */
@Controller
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final OfficeService officeService;
    private final CityService cityService;

    /**
     * Displays all shipments visible to the currently authenticated user.
     *
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated user
     * @return shipments list view
     */
    @GetMapping
    public String list(Model model, Authentication authentication) {
        model.addAttribute("shipments", shipmentService.findAllForView(authentication));
        model.addAttribute("isDeliveryman", isDeliveryman(authentication));
        model.addAttribute("isCoordinator", isCoordinator(authentication));
        model.addAttribute("activePage", "shipments");
        return "shipments/list";
    }

    /**
     * Displays all shipments sent by the currently authenticated client.
     *
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated client
     * @return client shipments view
     */
    @GetMapping("/my/sent")
    public String mySent(Model model, Authentication authentication) {
        model.addAttribute("shipments",
                shipmentService.getSentByClient(authentication)
                        .stream()
                        .map(s -> shipmentService.findByIdForView(s.getId(), authentication))
                        .toList());

        model.addAttribute("clientName", authentication.getName());
        return "Client";
    }

    /**
     * Displays all shipments received by the currently authenticated client.
     *
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated client
     * @return client shipments view
     */
    @GetMapping("/my/received")
    public String myReceived(Model model, Authentication authentication) {
        model.addAttribute("shipments",
                shipmentService.getReceivedByClient(authentication)
                        .stream()
                        .map(s -> shipmentService.findByIdForView(s.getId(), authentication))
                        .toList());

        model.addAttribute("clientName", authentication.getName());
        return "Client";
    }

    /**
     * Displays all expected shipments for the currently authenticated client.
     *
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated client
     * @return client shipments view
     */
    @GetMapping("/my/expected")
    public String myExpected(Model model, Authentication authentication) {
        model.addAttribute("shipments",
                shipmentService.getExpectedByClient(authentication)
                        .stream()
                        .map(s -> shipmentService.findByIdForView(s.getId(), authentication))
                        .toList());

        model.addAttribute("clientName", authentication.getName());
        return "Client";
    }

    /**
     * Displays all undelivered shipments.
     *
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated user
     * @return shipments list view
     */
    @GetMapping("/undelivered")
    public String undelivered(Model model, Authentication authentication) {
        model.addAttribute("shipments",
                shipmentService.getUndeliveredShipments()
                        .stream()
                        .map(shipmentService::toDtoWithCurrentStatus)
                        .toList()
        );

        model.addAttribute("isDeliveryman", isDeliveryman(authentication));
        model.addAttribute("isCoordinator", isCoordinator(authentication));
        model.addAttribute("activePage", "undelivered");
        return "shipments/list";
    }

    /**
     * Displays all shipments registered by a specific employee.
     *
     * @param employeeId employee identifier
     * @param model model used to pass shipment data to the view
     * @param authentication current authenticated user
     * @return shipments list view
     */
    @GetMapping("/employee/{employeeId}")
    public String byEmployee(@PathVariable Integer employeeId,
                             Model model,
                             Authentication authentication) {

        model.addAttribute("shipments",
                shipmentService.getShipmentsByEmployeeId(employeeId)
                        .stream()
                        .map(shipmentService::toDtoWithCurrentStatus)
                        .toList()
        );

        model.addAttribute("isDeliveryman", isDeliveryman(authentication));
        model.addAttribute("isCoordinator", isCoordinator(authentication));
        model.addAttribute("activePage", "shipments");
        return "shipments/list";
    }

    /**
     * Displays the shipment creation form.
     *
     * @param model model used to pass form data
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return shipment form view or redirect if access is denied
     */
    @GetMapping("/new")
    public String createForm(Model model,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма право да създава пратки.");
            return "redirect:/Deliveryman";
        }

        model.addAttribute("shipment", new ShipmentDto());
        addFormAttributes(model);
        return "shipments/form";
    }

    /**
     * Creates a new shipment.
     *
     * @param shipmentDto shipment data submitted from the form
     * @param result validation result object
     * @param model model used to pass form data
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return redirect after successful creation or shipment form on validation error
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                         BindingResult result,
                         Model model,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма право да създава пратки.");
            return "redirect:/Deliveryman";
        }

        if (result.hasErrors()) {
            addFormAttributes(model);
            return "shipments/form";
        }

        shipmentService.createFromDto(shipmentDto, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Пратката е регистрирана успешно!");
        return redirectAfterShipmentAction(authentication);
    }

    /**
     * Displays the shipment edit form.
     *
     * @param id shipment identifier
     * @param model model used to pass shipment data
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return shipment form view or redirect if access is denied
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id,
                           Model model,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма право да редактира пратки.");
            return "redirect:/Deliveryman";
        }

        model.addAttribute("shipment", shipmentService.findByIdForEdit(id, authentication));
        addFormAttributes(model);
        return "shipments/form";
    }

    /**
     * Updates an existing shipment.
     *
     * @param id shipment identifier
     * @param shipmentDto updated shipment data
     * @param result validation result object
     * @param model model used to pass form data
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return redirect after successful update or shipment form on validation error
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                         BindingResult result,
                         Model model,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма право да редактира пратки.");
            return "redirect:/Deliveryman";
        }

        if (result.hasErrors()) {
            addFormAttributes(model);
            return "shipments/form";
        }

        shipmentService.updateFromDto(id, shipmentDto, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Пратката е редактирана успешно!");
        return redirectAfterShipmentAction(authentication);
    }

    /**
     * Deletes a shipment.
     *
     * @param id shipment identifier
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return redirect to shipments list
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма право да изтрива пратки.");
            return "redirect:/Deliveryman";
        }

        shipmentService.deleteShipment(id, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Пратката е изтрита успешно!");
        return "redirect:/shipments";
    }

    /**
     * Updates the status of a shipment.
     *
     * @param id shipment identifier
     * @param status new shipment status
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return redirect after successful update
     */
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id,
                               @RequestParam Status status,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {

        shipmentService.updateShipmentStatus(id, status, authentication);
        redirectAttributes.addFlashAttribute("successMessage", "Статусът е обновен успешно!");
        return redirectAfterShipmentAction(authentication);
    }

    /**
     * Displays revenue report for a given time period.
     *
     * @param from start date
     * @param to end date
     * @param model model used to pass revenue data
     * @param authentication current authenticated user
     * @param redirectAttributes attributes used for redirect messages
     * @return revenue report view
     */
    @GetMapping("/revenue")
    public String revenue(@RequestParam String from,
                          @RequestParam String to,
                          Model model,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {

        if (isDeliveryman(authentication)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Куриерът няма достъп до справка за приходи.");
            return "redirect:/Deliveryman";
        }

        Instant fromInstant = LocalDate.parse(from).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toInstant = LocalDate.parse(to).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        model.addAttribute("revenue", shipmentService.getRevenueBetween(fromInstant, toInstant, authentication));
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "shipments/revenue";
    }

    /**
     * Displays the status history of a shipment.
     *
     * @param uniqueId unique shipment identifier
     * @param model model used to pass shipment history data
     * @return shipment history view
     */
    @GetMapping("/{uniqueId}/history")
    public String history(@PathVariable String uniqueId, Model model) {
        model.addAttribute("history", shipmentService.getShipmentHistory(uniqueId));
        model.addAttribute("uniqueId", uniqueId);
        return "shipments/history";
    }

    /**
     * Displays detailed information about a shipment.
     *
     * @param id shipment identifier
     * @param model model used to pass shipment data
     * @param authentication current authenticated user
     * @return shipment details view
     */
    @GetMapping("/{id}")
    public String details(@PathVariable Integer id,
                          Model model,
                          Authentication authentication) {

        model.addAttribute("shipment", shipmentService.findByIdForView(id, authentication));
        model.addAttribute("isCoordinator", isCoordinator(authentication));
        model.addAttribute("isDeliveryman", isDeliveryman(authentication));

        return "shipments/details";
    }

    /**
     * Adds required form attributes used in shipment creation and editing forms.
     *
     * @param model model used to pass form data
     */
    private void addFormAttributes(Model model) {
        model.addAttribute(
                "employees",
                employeeService.findByPositionType(PositionType.COORDINATOR)
        );

        model.addAttribute(
                "deliverymen",
                employeeService.findByPositionType(PositionType.DELIVERYMAN)
        );

        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("offices", officeService.findAll());
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("deliveryTypes", DeliveryType.values());
    }

    /**
     * Checks whether the authenticated user is a deliveryman.
     *
     * @param authentication current authenticated user
     * @return true if the user is a deliveryman, otherwise false
     */
    private boolean isDeliveryman(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return false;
        }

        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYEE"));

        if (!isEmployee) {
            return false;
        }

        return employeeService.findByUsername(authentication.getName())
                .getPositionType() == PositionType.DELIVERYMAN;
    }

    /**
     * Checks whether the authenticated user is a coordinator.
     *
     * @param authentication current authenticated user
     * @return true if the user is a coordinator or admin, otherwise false
     */
    private boolean isCoordinator(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return true;
        }

        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("EMPLOYEE"));

        if (!isEmployee) {
            return false;
        }

        return employeeService.findByUsername(authentication.getName())
                .getPositionType() == PositionType.COORDINATOR;
    }

    /**
     * Determines the redirect URL after shipment operations.
     *
     * @param authentication current authenticated user
     * @return redirect URL depending on the user role
     */
    private String redirectAfterShipmentAction(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return "redirect:/shipments";
        }

        return "redirect:/Employee";
    }

    /**
     * Calculates shipment price based on shipment weight and delivery type.
     *
     * @param weight shipment weight
     * @param deliveryType selected delivery type
     * @return calculated shipment price
     */
    @GetMapping("/calculate-price")
    @ResponseBody
    public BigDecimal calculatePrice(@RequestParam double weight,
                                     @RequestParam DeliveryType deliveryType) {

        return shipmentService.calculatePrice(weight, deliveryType);
    }
}