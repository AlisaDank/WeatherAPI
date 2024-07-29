package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import com.roadmap.WeatherAPI.service.LocationService;
import com.roadmap.WeatherAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final UserService userService;

    @Autowired
    public LocationController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @PostMapping("/search")
    public String searchLocation(@RequestParam(name = "locationName") String name, Model model,
                                 @AuthenticationPrincipal UserDetailsImpl currentUser) {
        List<LocationDTO> locations = locationService.getLocationsFromAPI(name);
        model.addAttribute("locations", locations);
        model.addAttribute("user", currentUser.getUser());
        return "search";
    }

    @PostMapping("/add")
    public String addLocation(@AuthenticationPrincipal UserDetailsImpl currentUser,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "lat") Double lat,
                              @RequestParam(name = "lon") Double lon) {
        userService.addLocation(new Location(name, lat, lon), currentUser.getUser());
        return "redirect:/user";
    }
}
