package com.team14.logistic_company.controllers;

import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.services.IUserService;
import com.team14.logistic_company.services.exceptions.EmailNotAvailable;
import com.team14.logistic_company.services.exceptions.UsernameNotAvailable;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    private String withAppLayout(Model model) {
        return "layouts/app";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("content", "index");
        return withAppLayout(model);
    }

    @GetMapping("/login")
    public String login(Model model, UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        model.addAttribute("user", userDto);
        model.addAttribute("title", "Login");
        model.addAttribute("content", "user/login");
        return withAppLayout(model);
    }

    @GetMapping("/register")
    public String register(Model model, UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        model.addAttribute("user", userDto);
        model.addAttribute("title", "Register");
        model.addAttribute("content", "user/register");
        return withAppLayout(model);
    }

    @PostMapping("/register")
    public String registerSave(@ModelAttribute("user") UserDto userDto, Model model, HttpServletRequest request) {

        userDto.setRole(Role.CLIENT);

        try {
            userService.create(userDto);
        } catch (UsernameNotAvailable | EmailNotAvailable ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "user/register");
            return withAppLayout(model);
        }

        authenticateUserAndSetSession(userDto, request);
        return "redirect:/home";
    }

    private void authenticateUserAndSetSession(UserDto user, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
