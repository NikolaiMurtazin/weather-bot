package com.bobrai.weatherbot.controller;

import com.bobrai.weatherbot.model.LogEntry;
import com.bobrai.weatherbot.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public Page<LogEntry> getAllLogs(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    @GetMapping("/{userId}")
    public Page<LogEntry> getLogsByUserId(@PathVariable Long userId, Pageable pageable) {
        return logRepository.findByUserId(userId, pageable);
    }
}
