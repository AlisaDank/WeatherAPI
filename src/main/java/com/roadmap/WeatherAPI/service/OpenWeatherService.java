package com.roadmap.WeatherAPI.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.WeatherAPI.client.OpenWeatherClient;
import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@Service
public class OpenWeatherService {
    private final OpenWeatherClient client;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public OpenWeatherService(OpenWeatherClient client, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public List<LocationDTO> getLocationsFromAPI(String name) {
        try {
            String responseFromAPI = client.searchLocationByName(name);
            if (responseFromAPI.isEmpty()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(responseFromAPI, new TypeReference<>() {});
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LocationWithTemperatureDTO getLocationWithTemperature(Location location) {
        try {
            String responseFromAPI = client.getLocationWithTemperature(location);
            return parseResponseFromWeatherAPIToDTO(responseFromAPI, location);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private LocationWithTemperatureDTO parseResponseFromWeatherAPIToDTO(String response, Location location) {
        JSONObject responseJSON = new JSONObject(response);
        BigDecimal temperature = (BigDecimal) responseJSON.getJSONObject("main").get("temp");
        LocationWithTemperatureDTO locationDTO = modelMapper.map(location, LocationWithTemperatureDTO.class);
        locationDTO.setTemperature(temperature.doubleValue());
        return locationDTO;
    }
}
