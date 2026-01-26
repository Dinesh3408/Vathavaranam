package com.example.weather.controller;

import com.example.weather.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AnalyticsController.class);
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("AnalyticsController: Received request for /stats");
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalHits", analyticsService.getTotalHits());
        stats.put("uniqueVisitors", analyticsService.getUniqueVisitors());
        stats.put("topCities", analyticsService.getTopCities());
        stats.put("uptime", calculateUptime());
        return ResponseEntity.ok(stats);
    }

    private String calculateUptime() {
        Duration uptime = Duration.between(analyticsService.getStartTime(), LocalDateTime.now());
        long hours = uptime.toHours();
        long minutes = uptime.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
