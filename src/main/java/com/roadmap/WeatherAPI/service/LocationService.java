package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.client.OpenWeatherClient;
import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final OpenWeatherClient client;
    private final UserService userService;

    @Autowired
    public LocationService(OpenWeatherClient client, UserService userService) {
        this.client = client;
        this.userService = userService;
    }

    public List<LocationDTO> getLocationsFromAPI(String name) {
        try {
            return client.searchLocationByName(name);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocationWithTemperatureDTO> showLocations(User currentUser) {
        User user = userService.findByLogin(currentUser.getLogin()).get();
        List<Location> locations = user.getLocations();
        Hibernate.initialize(locations);
        return locations.stream()
                .map(location -> {
                    try {
                        return client.getLocationWithTemperature(location);
                    } catch (URISyntaxException | IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    @Transactional
    public void deleteLocation(Double lat, Double lon, User currentUser) {
        User user = userService.findByLogin(currentUser.getLogin()).get();
        List<Location> locations = user.getLocations();
        Hibernate.initialize(locations);
        Optional<Location> location = findLocationByCoordinatesFromList(locations, lat, lon);
        locations.remove(location.get());
        userService.saveUser(user);
    }

    @Transactional
    public void addLocation(Location location, User currentUser) {
        User user = userService.findByLogin(currentUser.getLogin()).get();
        List<Location> locations = user.getLocations();
        Hibernate.initialize(locations);
        Optional<Location> optionalLocation =
                findLocationByCoordinatesFromList(locations, location.getLat(), location.getLon());
        if (optionalLocation.isEmpty()) {
        locations.add(location);
        location.setUser(user);
        userService.saveUser(user);
        }
    }

    private Optional<Location> findLocationByCoordinatesFromList(List<Location> locations, Double lat, Double lon) {
        return locations.stream()
                .filter(location -> Objects.equals(location.getLat(), lat) && Objects.equals(location.getLon(), lon))
                .findAny();
    }
}
