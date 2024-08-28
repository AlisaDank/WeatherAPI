package com.roadmap.WeatherAPI.client;

import com.roadmap.WeatherAPI.exception.WeatherClientException;
import com.roadmap.WeatherAPI.exception.WeatherServerException;
import com.roadmap.WeatherAPI.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@PropertySource("classpath:private.properties")
public class OpenWeatherClient {
    @Value("${apikey}")
    private String APIKey;
    private final HttpClient client;

    public OpenWeatherClient() {
        this.client = HttpClient.newHttpClient();
    }

    public OpenWeatherClient(HttpClient client) {
        this.client = client;
    }

    public String searchLocationByName(String name) throws URISyntaxException, IOException, InterruptedException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s"
                .formatted(encodedName, APIKey);
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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (HttpStatusCode.valueOf(response.statusCode()).is4xxClientError()) {
            throw new WeatherClientException("Something wrong with request");
        } else if (HttpStatusCode.valueOf(response.statusCode()).is5xxServerError()) {
            throw new WeatherServerException("Something goes wrong in API");
        }
        return response.body();
    }
}
