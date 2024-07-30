package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.client.OpenWeatherClient;
import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LocationService locationService;
    private final OpenWeatherClient client;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LocationService locationService, OpenWeatherClient client) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
        this.client = client;
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public List<LocationWithTemperatureDTO> showLocations(User currentUser) {
        User user = findByLogin(currentUser.getLogin()).get();
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
        User user = findByLogin(currentUser.getLogin()).get();
        List<Location> locations = user.getLocations();
        Hibernate.initialize(locations);
        Optional<Location> location = locations.stream()
                .filter(loc -> Objects.equals(loc.getLon(), lon) && Objects.equals(loc.getLat(), lat))
                .findFirst();
        locations.remove(location.get());
        userRepository.save(user);
    }

    @Transactional
    public void addLocation(Location location, User user) {
        Optional<Location> presentLocation = locationService
                .findByNameLatAndLon(location.getName(), location.getLat(), location.getLon());
        if (presentLocation.isPresent() && user.getLocations().contains(presentLocation.get()))
            return;
        location.setUser(user);   // TODO исключить добавление уже существующей локации
        user.addLocation(location);
        userRepository.save(user);
    }
}
