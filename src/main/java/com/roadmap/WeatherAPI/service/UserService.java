package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
