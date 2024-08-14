package com.roadmap.WeatherAPI.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.WeatherAPI.client.OpenWeatherClient;
import com.roadmap.WeatherAPI.dto.LocationDTO;
import com.roadmap.WeatherAPI.dto.LocationWithTemperatureDTO;
import com.roadmap.WeatherAPI.exception.WeatherClientException;
import com.roadmap.WeatherAPI.exception.WeatherServerException;
import com.roadmap.WeatherAPI.model.Location;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OpenWeatherServiceIT {
    static ObjectMapper objectMapper;
    static ModelMapper modelMapper;
    @Spy
    HttpClient mockClient;
    @Mock
    HttpResponse<String> mockResponse;

    @InjectMocks
    OpenWeatherClient openWeatherClient;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
    }

    @Test
    void getLocationsFromAPI_returnsCorrectListOfLocations() throws URISyntaxException, IOException, InterruptedException {
        String locationName = "Moscow";
        sendMockRequest();
        Mockito.when(mockResponse.body()).thenReturn(responseFromGeoSearch());
        String responseFromClient = openWeatherClient.searchLocationByName(locationName);
        List<LocationDTO> locations = objectMapper.readValue(responseFromClient, new TypeReference<>() {});

        assertThat(locations).isNotNull();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0)).isInstanceOf(LocationDTO.class);
        assertThat(locations.get(0).getName()).isEqualTo(locationName);
    }

    @Test
    void getLocationWithTemperature_returnsCorrectLocationDTO()
            throws IOException, InterruptedException, URISyntaxException, JSONException {
        Location location = new Location("Moscow", 55.7504461, 37.6174943);
        sendMockRequest();
        Mockito.when(mockResponse.body()).thenReturn(responseFromWeatherSearch());
        String responseFromClient = openWeatherClient.getLocationWithTemperature(location);
        JSONObject jsonObject = new JSONObject(responseFromClient);
        Double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        LocationWithTemperatureDTO locationDTO = modelMapper.map(location, LocationWithTemperatureDTO.class);
        locationDTO.setTemperature(temperature);


        assertThat(locationDTO.getName()).isEqualTo(location.getName());
        assertThat(locationDTO.getLat()).isEqualTo(location.getLat());
        assertThat(locationDTO.getLon()).isEqualTo(location.getLon());
        assertThat(locationDTO.getTemperature()).isNotNull();
    }

    @Test
    void getLocationWithTemperature_throwsException_whenStatusCodeIs4xx() throws IOException, InterruptedException {
        sendMockRequest();
        Mockito.when(mockResponse.statusCode()).thenReturn(401);

        assertThatThrownBy(() -> openWeatherClient.getLocationWithTemperature(new Location()))
                .isInstanceOf(WeatherClientException.class).hasMessage("Something wrong with request");
    }

    @Test
    void getLocationWithTemperature_throwsException_whenStatusCodeIs5xx() throws IOException, InterruptedException {
        sendMockRequest();
        Mockito.when(mockResponse.statusCode()).thenReturn(503);

        assertThatThrownBy(() -> openWeatherClient.getLocationWithTemperature(new Location()))
                .isInstanceOf(WeatherServerException.class).hasMessage("Something goes wrong in API");
    }

    private void sendMockRequest() throws IOException, InterruptedException {
        Mockito.when(mockClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);
    }

    private String responseFromGeoSearch() {
        return """
                [
                    {
                        "name": "Moscow",
                        "lat": 55.7504461,
                        "lon": 37.6174943,
                        "country": "RU"
                    },
                    {
                        "name": "Moscow",
                        "lat": 46.7323875,
                        "lon": -117.0001651,
                        "country": "US"
                    },
                    {
                        "name": "Moscow",
                        "lat": 45.071096,
                        "lon": -69.891586,
                        "country": "US"
                    }
                ]
                """;
    }

    private String responseFromWeatherSearch() {
        return """
                {
                    "coord": {
                        "lon": 37.62,
                        "lat": 55.75
                    },
                    "base": "stations",
                    "main": {
                        "temp": 17.58,
                        "feels_like": 17.38,
                        "temp_min": 16.81,
                        "temp_max": 18.38,
                        "pressure": 1007,
                        "humidity": 76,
                        "sea_level": 1007,
                        "grnd_level": 988
                    },
                    "sys": {
                        "type": 1,
                        "id": 9027,
                        "country": "RU",
                        "sunrise": 1723514284,
                        "sunset": 1723569050
                    },
                    "timezone": 10800,
                    "id": 524901,
                    "name": "Moscow",
                    "cod": 200
                }
                """;
    }
}