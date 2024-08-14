package com.roadmap.WeatherAPI.exception;

public class WeatherClientException extends RuntimeException {
    public WeatherClientException(String message) {
        super(message);
    }
}
