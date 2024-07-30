package com.roadmap.WeatherAPI.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class OpenWeatherClient {
    private final ObjectMapper mapper;
    private final String APIKey = "cf75087ea34994e9bad0f533171c01b3"; // TODO: закинуть ключ в ресурсы?
    private final HttpClient client = HttpClient.newHttpClient();

    @Autowired
    public OpenWeatherClient(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<LocationDTO> searchLocationByName(String name) throws URISyntaxException, IOException, InterruptedException {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s"
                .formatted(name, APIKey);
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .build();
        String response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
        return mapper.readValue(response, new TypeReference<>() {});
    }

    public LocationWithTemperatureDTO getLocationWithTemperature(Location location)
            throws URISyntaxException, IOException, InterruptedException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s"
                .formatted(location.getLat(), location.getLon(), APIKey);
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .build();
        String response = client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
        LocationWithTemperatureDTO locFromResponse = mapper.readerFor(LocationWithTemperatureDTO.class)
                .readValue(response);
        locFromResponse.setLatitude(location.getLat());
        locFromResponse.setLongitude(location.getLon());
        return locFromResponse;
    }
}
