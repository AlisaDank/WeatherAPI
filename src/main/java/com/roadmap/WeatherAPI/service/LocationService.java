package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.model.User;
import com.roadmap.WeatherAPI.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherService openWeatherService;

    @Autowired
    public LocationService(LocationRepository repository, OpenWeatherService openWeatherService) {
        this.locationRepository = repository;
        this.openWeatherService = openWeatherService;
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

    public Page<LocationWithTemperatureDTO> showUserLocations(User currentUser,
                                                              Integer pageNumber, Integer itemsPerPage) {
        Page<Location> locationPage = locationRepository
                .findAllByUserId(currentUser.getId(), PageRequest.of(pageNumber, itemsPerPage));
        List<LocationWithTemperatureDTO> locationDTOs = locationPage.getContent().stream()
                .map(openWeatherService::getLocationWithTemperature)
                .toList();
        return new PageImpl<>(locationDTOs, locationPage.getPageable(), locationPage.getTotalElements());
    }
}
