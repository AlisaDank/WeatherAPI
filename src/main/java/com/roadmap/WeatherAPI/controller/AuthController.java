package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.UserDTO;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.service.UserService;
import com.roadmap.WeatherAPI.util.UserValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;

    @Autowired
    public AuthController(UserService userService, UserValidation userValidation, ModelMapper mapper) {
        this.userService = userService;
        this.userValidation = userValidation;
        this.mapper = mapper;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDTO user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = mapper.map(userDTO, User.class);
        userValidation.validate(user,bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        userService.registerUser(user);
        return "redirect:auth/login";
    }
}
