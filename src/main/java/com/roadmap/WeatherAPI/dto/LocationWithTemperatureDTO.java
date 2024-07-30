package com.roadmap.WeatherAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LocationWithTemperatureDTO {
    private String name;
    private Double temperature;
    private Double latitude;
    private Double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("main")
    private void getTemperatureFromApi(Map<String, Object> main) {
        this.temperature = (Double) main.get("temp");
    }
}
