package com.bobrai.weatherbot.service;

import com.bobrai.weatherbot.model.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable("weather")
    public WeatherResponse getWeather(String city) throws Exception {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=ru",
                city, apiKey);

        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);

        WeatherResponse weather = new WeatherResponse();
        weather.setCity(root.path("name").asText());
        weather.setTemperature(root.path("main").path("temp").asDouble());
        weather.setFeelsLike(root.path("main").path("feels_like").asDouble());
        weather.setDescription(root.path("weather").get(0).path("description").asText());
        weather.setHumidity(root.path("main").path("humidity").asInt());
        weather.setWindSpeed(root.path("wind").path("speed").asDouble());

        return weather;
    }
}
