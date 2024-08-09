package com.roadmap.WeatherAPI.client;

import com.roadmap.WeatherAPI.model.Location;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class OpenWeatherClient {
    private final String APIKey = "cf75087ea34994e9bad0f533171c01b3"; // TODO: закинуть ключ в ресурсы
    private final HttpClient client = HttpClient.newHttpClient();

    public String searchLocationByName(String name) throws URISyntaxException, IOException, InterruptedException {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s"
                .formatted(name, APIKey);
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    public String getLocationWithTemperature(Location location)
            throws URISyntaxException, IOException, InterruptedException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s"
                .formatted(location.getLat(), location.getLon(), APIKey);
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }
}
