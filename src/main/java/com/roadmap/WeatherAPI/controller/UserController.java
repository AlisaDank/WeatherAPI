package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import com.roadmap.WeatherAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserPage(Model model, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        User user = currentUser.getUser();
        List<LocationWithTemperatureDTO> locations = userService.showLocations(user);
        model.addAttribute("user", user);
        model.addAttribute("locations", locations);
        return "user";
    }

    @DeleteMapping("/delete_location")
    public String deleteLocationFromUserList(@RequestParam(name = "lat") Double lat,
                                             @RequestParam(name = "lon") Double lon,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        User user = currentUser.getUser();
        userService.deleteLocation(lat, lon, user);
        return "redirect:/user";
    }
}
