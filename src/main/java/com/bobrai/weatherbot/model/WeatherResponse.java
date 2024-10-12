package com.bobrai.weatherbot.model;

import lombok.Data;

@Data
public class WeatherResponse {

    private String city;
    private double temperature;
    private double feelsLike;
    private String description;
    private int humidity;
    private double windSpeed;
}
