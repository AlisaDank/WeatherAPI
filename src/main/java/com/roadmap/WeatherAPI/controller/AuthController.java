package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.service.UserService;
import com.roadmap.WeatherAPI.util.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserValidation userValidation;

    @Autowired
    public AuthController(UserService userService, UserValidation userValidation) {
        this.userService = userService;
        this.userValidation = userValidation;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidation.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        userService.addUser(user);
        return "redirect:auth/login";
    }
}
