package com.team14.logistic_company.controllers;
import com.team14.logistic_company.controllers.forms.CreateEmployeeForm;
import com.team14.logistic_company.controllers.forms.UpdateEmployeeForm;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.OfficeService;
import com.team14.logistic_company.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // Показване на всички служители
    @GetMapping
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    // Показване на детайли за служител
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/details";
    }

    // Служители по позиция
    @GetMapping("/position/{positionType}")
    public String getEmployeesByPosition(@PathVariable PositionType positionType, Model model) {
        model.addAttribute("employees", employeeService.findByPositionType(positionType));
        model.addAttribute("positionType", positionType);
        return "employees/list";
    }

    // Служители по офис
    @GetMapping("/office/{officeId}")
    public String getEmployeesByOffice(@PathVariable Integer officeId, Model model) {
        model.addAttribute("employees", employeeService.findByOfficeId(officeId));
        model.addAttribute("office", officeService.findById(officeId));
        return "employees/list";
    }

    // Показване на форма за създаване
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employeeForm", new CreateEmployeeForm());
        model.addAttribute("offices", officeService.findAll());
        model.addAttribute("positionTypes", PositionType.values());
        return "employees/form";
    }

    // Обработка на създаване
    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employeeForm") CreateEmployeeForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("offices", officeService.findAll());
            model.addAttribute("positionTypes", PositionType.values());
            return "employees/form";
        }

        // 1. Създай User
        UserDto userDto = form.toUserDto();
        UserDto savedUser = userService.create(userDto);

        // 2. Създай Employee и свържи го с User
        EmployeeDto employeeDto = form.toEmployeeDto();
        employeeDto.setUserId(savedUser.getId());
        employeeService.create(employeeDto);

        redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
        return "redirect:/employees";
    }

    // Показване на форма за редактиране
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        EmployeeDto employeeDto = employeeService.findById(id);

        UpdateEmployeeForm form = new UpdateEmployeeForm();
        form.setId(employeeDto.getId());
        form.setPositionType(employeeDto.getPositionType());
        form.setOfficeId(employeeDto.getOfficeId());

        model.addAttribute("employeeForm", form);
        model.addAttribute("offices", officeService.findAll());
        model.addAttribute("positionTypes", PositionType.values());
        return "employees/edit-form";
    }

    // Обработка на редактиране
    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Integer id,
            @Valid @ModelAttribute("employeeForm") UpdateEmployeeForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("offices", officeService.findAll());
            model.addAttribute("positionTypes", PositionType.values());
            return "employees/edit-form";
        }

        EmployeeDto employeeDto = form.toEmployeeDto();
        employeeService.update(id, employeeDto);

        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        return "redirect:/employees";
    }

    // Изтриване
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/employees";
    }
}