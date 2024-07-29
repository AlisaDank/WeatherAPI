package com.roadmap.WeatherAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LocationWithTemperatureDTO {
    private String name;
    private Double temperature;

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

    @JsonProperty("main")
    private void getTemperatureFromApi(Map<String, Object> main) {
        this.temperature = (Double) main.get("temp");
    }

}
