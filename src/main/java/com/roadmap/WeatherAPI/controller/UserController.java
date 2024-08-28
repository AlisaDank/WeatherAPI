package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import com.roadmap.WeatherAPI.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {
    private final LocationService locationService;

    @Autowired
    public UserController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public String showUserPage(Model model, @AuthenticationPrincipal UserDetailsImpl currentUser,
                               @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        User user = currentUser.getUser();
        Integer itemsPerPage = 6;
        Page<LocationWithTemperatureDTO> locations = locationService.showUserLocations(user, pageNumber, itemsPerPage);
        if (pageNumber > 0 && locations.isEmpty()) {
            return "redirect:/user";
        }
        model.addAttribute("user", user);
        model.addAttribute("locations", locations);
        model.addAttribute("currentPage", pageNumber);
        return "user";
    }
}
