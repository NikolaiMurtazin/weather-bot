package com.bobrai.weatherbot;

import com.bobrai.weatherbot.controller.LogController;
import com.bobrai.weatherbot.model.LogEntry;
import com.bobrai.weatherbot.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LogController.class)
public class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogRepository logRepository;

    @Test
    public void testGetAllLogs() throws Exception {
        LogEntry logEntry = new LogEntry();
        logEntry.setId(1L);
        logEntry.setUserId(123456789L);
        logEntry.setCommand("/start");
        logEntry.setResponse("Welcome");
        logEntry.setTimestamp(LocalDateTime.now());

        Page<LogEntry> page = new PageImpl<>(Arrays.asList(logEntry));

        when(logRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].userId").value(123456789L))
                .andExpect(jsonPath("$.content[0].command").value("/start"));
    }

    @Test
    public void testGetLogsByUserId() throws Exception {
        Long userId = 123456789L;
        LogEntry logEntry = new LogEntry();
        logEntry.setId(1L);
        logEntry.setUserId(userId);
        logEntry.setCommand("/start");
        logEntry.setResponse("Welcome");
        logEntry.setTimestamp(LocalDateTime.now());

        Page<LogEntry> page = new PageImpl<>(Arrays.asList(logEntry));

        when(logRepository.findByUserId(eq(userId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/logs/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(userId))
                .andExpect(jsonPath("$.content[0].command").value("/start"));
    }
}
