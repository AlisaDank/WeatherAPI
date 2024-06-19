package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.UserRepository;
import com.roadmap.WeatherAPI.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByLogin(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User with this login not found");
        return new UserDetailsImpl(user.get());
    }
}
