package com.roadmap.WeatherAPI.controller;

import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import com.roadmap.WeatherAPI.service.LocationService;
import com.roadmap.WeatherAPI.service.OpenWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;

    @Autowired
    public LocationController(LocationService locationService, OpenWeatherService openWeatherService) {
        this.locationService = locationService;
        this.openWeatherService = openWeatherService;
    }

    @PostMapping("/search")
    public String searchLocation(@RequestParam(name = "locationName") String name, Model model,
                                 @AuthenticationPrincipal UserDetailsImpl currentUser) {
        List<LocationDTO> locations = openWeatherService.getLocationsFromAPI(name);
        model.addAttribute("locations", locations);
        model.addAttribute("user", currentUser.getUser());
        return "search";
    }

    @PostMapping("/add")
    public String addLocation(@AuthenticationPrincipal UserDetailsImpl currentUser,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "lat") Double lat,
                              @RequestParam(name = "lon") Double lon) {
        locationService.addLocation(new Location(name, lat, lon), currentUser.getUser());
        return "redirect:/user";
    }

    @DeleteMapping("/delete")
    public String deleteLocationFromUserList(@RequestParam(name = "lat") Double lat,
                                             @RequestParam(name = "lon") Double lon,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        User user = currentUser.getUser();
        locationService.deleteLocation(lat, lon, user);
        return "redirect:/user";
    }
}
