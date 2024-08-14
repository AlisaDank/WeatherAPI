package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository repository) {
        this.locationRepository = repository;
    }

    @Transactional
    public void deleteLocation(Double lat, Double lon, User currentUser) {
        locationRepository.deleteLocationByUserIdAndLatAndLon(currentUser.getId(), lat, lon);
    }

    @Transactional
    public void addLocation(Location location, User currentUser) {
        Optional<Location> locationDuplicate = locationRepository
                .findByUserIdAndLatAndLon(currentUser.getId(), location.getLat(), location.getLon());
        if (locationDuplicate.isEmpty()) {
            location.setUser(currentUser);
            locationRepository.save(location);
        }
    }
}
