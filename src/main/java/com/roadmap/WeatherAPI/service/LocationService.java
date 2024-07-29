package com.roadmap.WeatherAPI.service;

import com.roadmap.WeatherAPI.client.OpenWeatherClient;
import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.model.Location;
import com.roadmap.WeatherAPI.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    private final OpenWeatherClient client;

    @Autowired
    public LocationService(LocationRepository locationRepository, OpenWeatherClient client) {
        this.locationRepository = locationRepository;
        this.client = client;
    }

    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    public Optional<Location> findByNameLatAndLon(String name, Double lat, Double lon) {
        return locationRepository.findByNameAndLatAndLon(name, lat, lon);
    }

    public List<LocationDTO> getLocationsFromAPI(String name) {
        try {
            return client.searchLocationByName(name);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);  // TODO: возможно обработать исключения
        }
    }
}
