package com.bobrai.weatherbot.repository;

import com.bobrai.weatherbot.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}
