package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.services.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Controller
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    // c) всички пратки: employee вижда всички, client вижда неговите
    @GetMapping
    public String list(Model model, Authentication authentication) {
        try {
            model.addAttribute("shipments", shipmentService.getAllShipmentsForEmployee(authentication));
        } catch (Exception ignored) {
            model.addAttribute("shipments", shipmentService.getShipmentsForClient(authentication));
        }
        return "shipments/list";
    }

    // f) изпратени от клиента
    @GetMapping("/my/sent")
    public String mySent(Model model, Authentication authentication) {
        model.addAttribute("shipments", shipmentService.getSentByClient(authentication));
        return "shipments/list";
    }

    // g) получени от клиента
    @GetMapping("/my/received")
    public String myReceived(Model model, Authentication authentication) {
        model.addAttribute("shipments", shipmentService.getReceivedByClient(authentication));
        return "shipments/list";
    }

    // очаквани за получаване
    @GetMapping("/my/expected")
    public String myExpected(Model model, Authentication authentication) {
        model.addAttribute("shipments", shipmentService.getExpectedByClient(authentication));
        return "shipments/list";
    }

    // e) недоставени (служител)
    @GetMapping("/undelivered")
    public String undelivered(Model model, Authentication authentication) {
        model.addAttribute("shipments", shipmentService.getUndeliveredShipments());
        return "shipments/list";
    }

    // d) по служител
    @GetMapping("/employee/{employeeId}")
    public String byEmployee(@PathVariable Integer employeeId, Model model) {
        model.addAttribute("shipments", shipmentService.getShipmentsByEmployeeId(employeeId));
        return "shipments/list";
    }

    // 4) служител сменя статус (получена/доставена)
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id,
                               @RequestParam Status status,
                               RedirectAttributes ra) {
        shipmentService.updateShipmentStatus(id, status);
        ra.addFlashAttribute("successMessage", "Shipment status updated!");
        return "redirect:/shipments";
    }

    // h) приходи за период (служител)
    @GetMapping("/revenue")
    public String revenue(@RequestParam String from,
                          @RequestParam String to,
                          Model model,
                          Authentication authentication) {

        Instant fromInstant = LocalDate.parse(from).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toInstant = LocalDate.parse(to).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        model.addAttribute("revenue", shipmentService.getRevenueBetween(fromInstant, toInstant, authentication));
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "shipments/revenue";
    }

    // 4) служител регистрира пратка (минимално – с Entity binding)
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("shipment", new ShipmentDto());
        return "shipments/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                         BindingResult result,
                         Authentication authentication,
                         RedirectAttributes ra) {

        if (result.hasErrors()) return "shipments/form";

        shipmentService.createFromDto(shipmentDto, authentication);
        ra.addFlashAttribute("successMessage", "Shipment registered!");
        return "redirect:/shipments";
    }
    // 1) форма за редакция
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, Authentication authentication) {
        model.addAttribute("shipment", shipmentService.findByIdForEdit(id, authentication));
        return "shipments/form";
    }

    // 2) запис на редакция
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                         BindingResult result,
                         Authentication authentication,
                         RedirectAttributes ra) {

        if (result.hasErrors()) return "shipments/form";

        shipmentService.updateFromDto(id, shipmentDto, authentication);
        ra.addFlashAttribute("successMessage", "Shipment updated!");
        return "redirect:/shipments";
    }

    // 3) delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id,
                         Authentication authentication,
                         RedirectAttributes ra) {

        shipmentService.deleteShipment(id, authentication);
        ra.addFlashAttribute("successMessage", "Shipment deleted!");
        return "redirect:/shipments";
    }


}

