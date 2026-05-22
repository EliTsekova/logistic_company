package com.team14.logistic_company.controllers;

import com.team14.logistic_company.controllers.forms.CreateClientForm;
import com.team14.logistic_company.controllers.forms.UpdateClientForm;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.ShipmentService;
import com.team14.logistic_company.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Controller responsible for client management operations.
 * Handles listing, viewing, creating, editing, deleting clients,
 * and displaying client shipment reports.
 */
@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;
    private final ShipmentService shipmentService;

    /**
     * Displays a list of all clients.
     *
     * @param model model used to pass client data to the view
     * @return clients list view
     */
    @GetMapping
    public String getAllClients(Model model) {

        model.addAttribute("clients", clientService.findAll());

        return "clients/list";
    }

    /**
     * Displays detailed information about a specific client.
     * Includes all shipments, sent shipments, received shipments,
     * expected shipments, and shipment statistics.
     *
     * @param id client identifier
     * @param model model used to pass client and shipment data to the view
     * @return client details view
     */
    @GetMapping("/{id}")
    public String getClientById(@PathVariable Integer id, Model model) {

        ClientDto client = clientService.findById(id);

        model.addAttribute("client", client);

        model.addAttribute(
                "sentShipments",
                shipmentService.getSentShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "receivedShipments",
                shipmentService.getReceivedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "expectedShipments",
                shipmentService.getExpectedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "allShipments",
                shipmentService.getShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "totalShipments",
                shipmentService.getShipmentsByClientId(id).size()
        );

        model.addAttribute(
                "sentCount",
                shipmentService.getSentShipmentsByClientId(id).size()
        );

        model.addAttribute(
                "receivedCount",
                shipmentService.getReceivedShipmentsByClientId(id).size()
        );

        model.addAttribute(
                "expectedCount",
                shipmentService.getExpectedShipmentsByClientId(id).size()
        );

        model.addAttribute("activeTab", "all");

        return "clients/details";
    }

    /**
     * Displays the client creation form.
     *
     * @param model model used to pass an empty client creation form to the view
     * @return client creation form view
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("clientForm", new CreateClientForm());

        return "clients/form";
    }

    /**
     * Creates a new client and the related user account.
     *
     * @param form submitted client creation form
     * @param result validation result object
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to clients list after successful creation, or form view on validation error
     */
    @PostMapping
    public String createClient(
            @Valid @ModelAttribute("clientForm") CreateClientForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            return "clients/form";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {

            result.rejectValue(
                    "confirmPassword",
                    "error.confirmPassword",
                    "Passwords do not match!"
            );

            return "clients/form";
        }

        UserDto userDto = form.toUserDto();

        User savedUser = userService.create(userDto);

        ClientDto clientDto = form.toClientDto();

        clientDto.setUserId(savedUser.getId());

        clientService.create(clientDto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Клиентът е създаден успешно!"
        );

        return "redirect:/clients";
    }

    /**
     * Displays the client edit form with existing client data.
     *
     * @param id client identifier
     * @param model model used to pass client data to the edit form
     * @return client edit form view
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        ClientDto clientDto = clientService.findById(id);

        UpdateClientForm form = new UpdateClientForm();

        form.setId(clientDto.getId());
        form.setUserId(clientDto.getUserId());
        form.setFirstName(clientDto.getUserFirstName());
        form.setLastName(clientDto.getUserLastName());
        form.setUsername(clientDto.getUserUsername());
        form.setEmail(clientDto.getUserEmail());
        form.setPhoneNumber(clientDto.getPhoneNumber());

        model.addAttribute("clientForm", form);

        return "clients/edit-form";
    }

    /**
     * Updates an existing client and the related user account.
     *
     * @param id client identifier
     * @param form submitted client update form
     * @param result validation result object
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to clients list after successful update, or edit form on validation error
     */
    @PostMapping("/update/{id}")
    public String updateClient(
            @PathVariable Integer id,
            @Valid @ModelAttribute("clientForm") UpdateClientForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            return "clients/edit-form";
        }

        UserDto userDto = form.toUserDto();

        userService.update(userDto);

        ClientDto clientDto = form.toClientDto();

        clientService.update(id, clientDto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Клиентът е редактиран успешно!"
        );

        return "redirect:/clients";
    }

    /**
     * Deletes a client and the related user account.
     *
     * @param id client identifier
     * @param redirectAttributes attributes used for success messages after redirect
     * @return redirect to clients list
     */
    @GetMapping("/delete/{id}")
    public String deleteClient(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {

        ClientDto clientDto = clientService.findById(id);

        Integer userId = clientDto.getUserId();

        clientService.delete(id);

        userService.delete(userId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Клиентът е изтрит успешно!"
        );

        return "redirect:/clients";
    }

    /**
     * Displays all shipments related to a specific client.
     *
     * @param id client identifier
     * @param model model used to pass client and shipment data to the view
     * @return client details view with all shipments tab active
     */
    @GetMapping("/{id}/shipments/all")
    public String clientAllShipments(
            @PathVariable Integer id,
            Model model
    ) {

        ClientDto client = clientService.findById(id);

        model.addAttribute("client", client);

        model.addAttribute(
                "allShipments",
                shipmentService.getShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "sentShipments",
                shipmentService.getSentShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "receivedShipments",
                shipmentService.getReceivedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute(
                "expectedShipments",
                shipmentService.getExpectedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute("activeTab", "all");

        return "clients/details";
    }

    /**
     * Displays only shipments sent by a specific client.
     *
     * @param id client identifier
     * @param model model used to pass client and sent shipment data to the view
     * @return client details view with sent shipments tab active
     */
    @GetMapping("/{id}/shipments/sent")
    public String clientSentShipments(
            @PathVariable Integer id,
            Model model
    ) {

        ClientDto client = clientService.findById(id);

        model.addAttribute("client", client);

        model.addAttribute(
                "sentShipments",
                shipmentService.getSentShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute("activeTab", "sent");

        return "clients/details";
    }

    /**
     * Displays only shipments received by a specific client.
     *
     * @param id client identifier
     * @param model model used to pass client and received shipment data to the view
     * @return client details view with received shipments tab active
     */
    @GetMapping("/{id}/shipments/received")
    public String clientReceivedShipments(
            @PathVariable Integer id,
            Model model
    ) {

        ClientDto client = clientService.findById(id);

        model.addAttribute("client", client);

        model.addAttribute(
                "receivedShipments",
                shipmentService.getReceivedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute("activeTab", "received");

        return "clients/details";
    }

    /**
     * Displays only expected shipments for a specific client.
     *
     * @param id client identifier
     * @param model model used to pass client and expected shipment data to the view
     * @return client details view with expected shipments tab active
     */
    @GetMapping("/{id}/shipments/expected")
    public String clientExpectedShipments(
            @PathVariable Integer id,
            Model model
    ) {

        ClientDto client = clientService.findById(id);

        model.addAttribute("client", client);

        model.addAttribute(
                "expectedShipments",
                shipmentService.getExpectedShipmentsByClientId(id)
                        .stream()
                        .map(shipment -> shipmentService.toDtoWithCurrentStatus(shipment))
                        .toList()
        );

        model.addAttribute("activeTab", "expected");

        return "clients/details";
    }
}