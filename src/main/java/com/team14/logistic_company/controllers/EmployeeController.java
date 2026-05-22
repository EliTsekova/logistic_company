package com.team14.logistic_company.controllers;

import com.team14.logistic_company.controllers.forms.CreateEmployeeForm;
import com.team14.logistic_company.controllers.forms.UpdateEmployeeForm;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.ShipmentService;
import com.team14.logistic_company.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final OfficeService officeService;
    private final UserService userService;
    private final ShipmentService shipmentService;

    @GetMapping
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Integer id, Model model) {

        model.addAttribute("employee", employeeService.findById(id));

        model.addAttribute("shipments",
                shipmentService.getShipmentsByEmployeeId(id)
                        .stream()
                        .map(shipmentService::toDtoWithCurrentStatus)
                        .toList()
        );

        return "employees/details";
    }

    @GetMapping("/position/{positionType}")
    public String getEmployeesByPosition(@PathVariable PositionType positionType,
                                         Model model) {

        model.addAttribute("employees",
                employeeService.findByPositionType(positionType));

        model.addAttribute("positionType", positionType);

        return "employees/list";
    }

    @GetMapping("/office/{officeId}")
    public String getEmployeesByOffice(@PathVariable Integer officeId,
                                       Model model) {

        model.addAttribute("employees",
                employeeService.findByOfficeId(officeId));

        model.addAttribute("office",
                officeService.findById(officeId));

        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("employeeForm",
                new CreateEmployeeForm());

        model.addAttribute("offices",
                officeService.findAll());

        model.addAttribute("positionTypes",
                PositionType.values());

        return "employees/form";
    }

    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employeeForm")
            CreateEmployeeForm form,

            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("offices",
                    officeService.findAll());

            model.addAttribute("positionTypes",
                    PositionType.values());

            return "employees/form";
        }

        UserDto userDto = form.toUserDto();

        User savedUser = userService.create(userDto);

        EmployeeDto employeeDto = form.toEmployeeDto();

        employeeDto.setUserId(savedUser.getId());

        employeeService.create(employeeDto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Служителят е създаден успешно!"
        );

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id,
                               Model model) {

        EmployeeDto employeeDto = employeeService.findById(id);

        UpdateEmployeeForm form = new UpdateEmployeeForm();

        form.setId(employeeDto.getId());
        form.setUserId(employeeDto.getUserId());

        form.setFirstName(employeeDto.getUserFirstName());
        form.setLastName(employeeDto.getUserLastName());

        form.setUsername(employeeDto.getUserUsername());
        form.setEmail(employeeDto.getUserEmail());

        form.setPositionType(employeeDto.getPositionType());
        form.setOfficeId(employeeDto.getOfficeId());

        model.addAttribute("employeeForm", form);

        model.addAttribute("offices",
                officeService.findAll());

        model.addAttribute("positionTypes",
                PositionType.values());

        return "employees/edit-form";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Integer id,

            @Valid @ModelAttribute("employeeForm")
            UpdateEmployeeForm form,

            BindingResult result,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            model.addAttribute("offices",
                    officeService.findAll());

            model.addAttribute("positionTypes",
                    PositionType.values());

            return "employees/edit-form";
        }

        UserDto userDto = form.toUserDto();

        userService.update(userDto);

        EmployeeDto employeeDto = form.toEmployeeDto();

        employeeService.update(id, employeeDto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Служителят е редактиран успешно!"
        );

        return redirectAfterEmployeeUpdate(authentication);
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {

        EmployeeDto employeeDto = employeeService.findById(id);

        Integer userId = employeeDto.getUserId();

        employeeService.delete(id);

        userService.delete(userId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Служителят е изтрит успешно!"
        );

        return "redirect:/employees";
    }

    private String redirectAfterEmployeeUpdate(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return "redirect:/employees";
        }

        EmployeeDto employee =
                employeeService.findByUsername(authentication.getName());

        if (employee.getPositionType() == PositionType.COORDINATOR) {
            return "redirect:/Coordinator";
        }

        if (employee.getPositionType() == PositionType.DELIVERYMAN) {
            return "redirect:/Deliveryman";
        }

        return "redirect:/Employee";
    }
}