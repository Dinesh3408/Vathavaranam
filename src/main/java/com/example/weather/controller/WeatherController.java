package com.example.weather.controller;

import com.example.weather.dto.ForecastResponse;
import com.example.weather.model.Weather;
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

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String city, HttpServletRequest request) {
        try {
            Weather weather = weatherService.getWeatherByCity(city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/coordinates")
    public ResponseEntity<Weather> getWeatherByCoordinates(@RequestParam double lat, @RequestParam double lon,
            HttpServletRequest request) {
        try {
            Weather weather = weatherService.getWeatherByCoordinates(lat, lon);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<ForecastResponse> getForecastByCity(@PathVariable String city) {
        try {
            ForecastResponse forecast = weatherService.getForecastByCity(city);
            return ResponseEntity.ok(forecast);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/auto")
    public ResponseEntity<?> getWeatherByAutoIp(HttpServletRequest request) {
        try {
            String ip = getClientIP(request);
            Weather weather = weatherService.getWeatherByIp(ip);
            if (weather == null) {
                return ResponseEntity.status(404).body("Could not determine location or weather from IP: " + ip);
            }
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

}
