package com.bobrai.weatherbot.config;

import com.bobrai.weatherbot.service.TelegramBotService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotInitializer {

    @Autowired
    TelegramBotService botService;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(botService);
            System.out.println("Telegram bot registered successfully.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
