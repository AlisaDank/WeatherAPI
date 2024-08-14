package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OpenWeatherService openWeatherService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OpenWeatherService openWeatherService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.openWeatherService = openWeatherService;
    }

    @Transactional
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<LocationWithTemperatureDTO> showUserLocations(User currentUser) {
        User user = findByLogin(currentUser.getLogin()).get();
        List<Location> locations = user.getLocations();
        Hibernate.initialize(locations);
        return locations.stream()
                .map(openWeatherService::getLocationWithTemperature)
                .toList();
    }
}
