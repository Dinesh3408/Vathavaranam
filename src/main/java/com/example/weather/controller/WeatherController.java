package com.example.weather.controller;

import com.example.weather.model.Weather;
import com.example.weather.service.AnalyticsService;
import com.example.weather.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/{city}")
    public ResponseEntity<Weather> getWeatherByCity(@PathVariable String city) {
        try {
            Weather weather = weatherService.getWeatherByCity(city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/coordinates")
    public ResponseEntity<Weather> getWeatherByCoordinates(@RequestParam double lat, @RequestParam double lon) {
        try {
            Weather weather = weatherService.getWeatherByCoordinates(lat, lon);
            return ResponseEntity.ok(weather);
        }catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if(ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

        }
        if(ip !=null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

}
