package com.bobrai.weatherbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherBotApplication.class, args);
    }
}
