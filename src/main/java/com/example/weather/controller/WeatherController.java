package com.example.weather.controller;

import com.example.weather.model.Weather;
import com.example.weather.service.WeatherService;
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

}
