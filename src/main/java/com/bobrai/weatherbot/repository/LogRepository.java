package com.bobrai.weatherbot.repository;

import com.bobrai.weatherbot.model.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntry, Long> {

    Page<LogEntry> findByUserId(Long userId, Pageable pageable);
}
