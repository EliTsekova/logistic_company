package com.team14.logistic_company.controllers;

import com.team14.logistic_company.controllers.forms.CreateClientForm;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.services.ClientService;
import com.team14.logistic_company.services.IUserService;
import com.team14.logistic_company.services.exceptions.EmailNotAvailable;
import com.team14.logistic_company.services.exceptions.UsernameNotAvailable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
/**
 * Controller responsible for handling user-related web requests.
 * Provides pages for home, login, registration, and test access.
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final ClientService clientService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Displays the home page of the application.
     *
     * @return the home page view name
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "HomePage";
    }

    /**
     * Displays the login page.
     * If the user is already authenticated, redirects to the home page.
     *
     * @param model the model used to pass user data to the view
     * @return the login page view name or redirect to home
     */
    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        model.addAttribute("user", new UserDto());
        return "login";
    }

    /**
     * Displays the registration page for new clients.
     * If the user is already authenticated, redirects to the home page.
     *
     * @param model the model used to pass the registration form to the view
     * @return the registration page view name or redirect to home
     */
    @GetMapping("/register")
    public String register(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        model.addAttribute("clientForm", new CreateClientForm());
        return "register";
    }

    /**
     * Processes the client registration form.
     * Creates a new user with client role and then creates the related client profile.
     *
     * @param form the submitted client registration form
     * @param result contains validation errors, if any
     * @param model the model used to pass error messages to the view
     * @return redirect to login page after successful registration, or registration page on error
     * @throws UsernameNotAvailable if the chosen username is already taken
     * @throws EmailNotAvailable if the chosen email is already taken
     */
    @PostMapping("/register")
    public String registerSave(
            @Valid @ModelAttribute("clientForm") CreateClientForm form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue(
                    "confirmPassword",
                    "error.confirmPassword",
                    "Паролите не съвпадат!"
            );

            return "register";
        }

        try {
            UserDto userDto = form.toUserDto();
            userDto.setRole(Role.CLIENT);

            User savedUser = userService.create(userDto);

            ClientDto clientDto = form.toClientDto();
            clientDto.setUserId(savedUser.getId());

            clientService.create(clientDto);

        } catch (UsernameNotAvailable | EmailNotAvailable ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }

    /**
     * Authenticates a newly registered user and stores the authentication in the security context.
     *
     * @param userDto the user data used for authentication
     * @param request the current HTTP request used to build authentication details
     */
    private void authenticateUserAndSetSession(UserDto userDto, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDto.getPassword());

        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Test endpoint used to verify that the controller is working.
     *
     * @return a simple confirmation message
     */
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "OK";
    }
}