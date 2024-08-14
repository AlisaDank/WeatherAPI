package com.roadmap.WeatherAPI.exception;

public class WeatherServerException extends RuntimeException {
    public WeatherServerException(String message) {
        super(message);
    }
}
