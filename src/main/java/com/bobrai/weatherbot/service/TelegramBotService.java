package com.bobrai.weatherbot.service;

import com.bobrai.weatherbot.config.BotConfig;
import com.bobrai.weatherbot.model.LogEntry;
import com.bobrai.weatherbot.model.UserSettings;
import com.bobrai.weatherbot.model.WeatherResponse;
import com.bobrai.weatherbot.repository.LogRepository;
import com.bobrai.weatherbot.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Component
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Long userId = update.getMessage().getFrom().getId();
            String messageText = update.getMessage().getText();
            String responseText;

            try {
                if (messageText.startsWith("/start")) {
                    responseText = "Добро пожаловать! Этот бот создан в качестве тестового задания для компании BobrAi.";
                } else if (messageText.startsWith("/weather")) {
                    String[] parts = messageText.split(" ", 2);
                    String city;

                    if (parts.length > 1) {
                        city = parts[1];
                    } else {
                        // Попытка получить город из настроек пользователя
                        UserSettings settings = userSettingsRepository.findById(userId).orElse(null);
                        if (settings != null && settings.getDefaultCity() != null) {
                            city = settings.getDefaultCity();
                        } else {
                            responseText = "Пожалуйста, укажите город после команды или сохраните его с помощью /setcity";
                            sendResponse(update, responseText);
                            saveLog(userId, messageText, responseText);
                            return;
                        }
                    }

                    WeatherResponse weather = weatherService.getWeather(city);
                    responseText = String.format(
                            "Погода в городе %s:\n" +
                                    "Температура: %.1f°C\n" +
                                    "Ощущается как: %.1f°C\n" +
                                    "Описание: %s\n" +
                                    "Влажность: %d%%\n" +
                                    "Скорость ветра: %.1f м/с",
                            weather.getCity(),
                            weather.getTemperature(),
                            weather.getFeelsLike(),
                            weather.getDescription(),
                            weather.getHumidity(),
                            weather.getWindSpeed()
                    );
                } else if (messageText.startsWith("/setcity")) {
                    String[] parts = messageText.split(" ", 2);
                    if (parts.length > 1) {
                        String city = parts[1];
                        UserSettings settings = userSettingsRepository.findById(userId).orElse(new UserSettings());
                        settings.setUserId(userId);
                        settings.setDefaultCity(city);
                        userSettingsRepository.save(settings);
                        responseText = "Город по умолчанию установлен: " + city;
                    } else {
                        responseText = "Пожалуйста, укажите город после команды /setcity";
                    }
                } else {
                    responseText = "Извините, я не понимаю эту команду.";
                }
            } catch (Exception e) {
                responseText = "Произошла ошибка при обработке вашего запроса.";
            }

            sendResponse(update, responseText);
            saveLog(userId, messageText, responseText);
        }
    }

    private void sendResponse(Update update, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveLog(Long userId, String command, String response) {
        LogEntry logEntry = new LogEntry();
        logEntry.setUserId(userId);
        logEntry.setCommand(command);
        logEntry.setResponse(response);
        logEntry.setTimestamp(LocalDateTime.now());
        logRepository.save(logEntry);
    }
}
