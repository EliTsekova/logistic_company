package com.team14.logistic_company.controllers;
import com.team14.logistic_company.controllers.forms.CreateClientForm;
import com.team14.logistic_company.controllers.forms.UpdateClientForm;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    // Показване на всички клиенти
    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients/list";
    }

    // Показване на детайли за клиент
    @GetMapping("/{id}")
    public String getClientById(@PathVariable Integer id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        return "clients/details";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("clientForm", new CreateClientForm());
        return "clients/form";
    }

    // Обработка на създаване
    @PostMapping
    public String createClient(
            @Valid @ModelAttribute("clientForm") CreateClientForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "clients/form";
        }

        // 1. Създай User
        UserDto userDto = form.toUserDto();
        User savedUser = userService.create(userDto);

        // 2. Създай Client и свържи го с User
        ClientDto clientDto = form.toClientDto();
        clientDto.setUserId(savedUser.getId());
        clientService.create(clientDto);


        redirectAttributes.addFlashAttribute("successMessage", "Client created successfully!");
        return "redirect:/clients";
    }

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ClientDto clientDto = clientService.findById(id);

        UpdateClientForm form = new UpdateClientForm();
        form.setId(clientDto.getId());
        form.setPhoneNumber(clientDto.getPhoneNumber());

        model.addAttribute("clientForm", form);
        return "clients/edit-form";
    }

    // Обработка на редактиране
    @PostMapping("/update/{id}")
    public String updateClient(
            @PathVariable Integer id,
            @Valid @ModelAttribute("clientForm") UpdateClientForm form,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "clients/edit-form";
        }

        ClientDto clientDto = form.toClientDto();
        clientService.update(id, clientDto);

        redirectAttributes.addFlashAttribute("successMessage", "Client updated successfully!");
        return "redirect:/clients";
    }

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        clientService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully!");
        return "redirect:/clients";
    }
}