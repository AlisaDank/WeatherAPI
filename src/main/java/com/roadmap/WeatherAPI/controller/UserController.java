package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import com.roadmap.WeatherAPI.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    public final LocationService locationService;

    @Autowired
    public UserController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public String showUserPage(Model model, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        User user = currentUser.getUser();
        List<LocationWithTemperatureDTO> locations = locationService.showUserLocations(user);
        model.addAttribute("user", user);
        model.addAttribute("locations", locations);
        return "user";
    }
}
