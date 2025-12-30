package com.example.weather.controller;

import com.example.weather.model.Weather;
import com.example.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests

    @MockBean
    private WeatherService weatherService;  // Mock the service

    @Test
    void getWeather_Success() throws Exception {
        // Arrange: Create mock weather data
        Weather mockWeather = new Weather();
        mockWeather.setCity("London");
        mockWeather.setTemperature(15.5);
        mockWeather.setFeelsLike(13.2);
        mockWeather.setHumidity(65);
        mockWeather.setDescription("cloudy");
        mockWeather.setWindSpeed(12.5);
        mockWeather.setPressure(1013);
        mockWeather.setCountry("GB");
        mockWeather.setTimestamp(1702646400);

        // Mock the service method
        when(weatherService.getWeatherByCity(anyString())).thenReturn(mockWeather);

        // Act & Assert: Make request and verify response
        mockMvc.perform(get("/api/weather/London"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.temperature").value(15.5))
                .andExpect(jsonPath("$.humidity").value(65))
                .andExpect(jsonPath("$.country").value("GB"));
    }

    @Test
    void getWeatherByCoordinates_Success() throws Exception {
        // Arrange
        Weather mockWeather = new Weather();
        mockWeather.setCity("Mumbai");
        mockWeather.setTemperature(28.5);
        mockWeather.setCountry("IN");

        when(weatherService.getWeatherByCoordinates(anyDouble(), anyDouble()))
                .thenReturn(mockWeather);

        // Act & Assert
        mockMvc.perform(get("/api/weather/coordinates")
                        .param("lat", "19.0760")
                        .param("lon", "72.8777"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Mumbai"))
                .andExpect(jsonPath("$.temperature").value(28.5))
                .andExpect(jsonPath("$.country").value("IN"));
    }

    @Test
    void getWeather_ServiceThrowsException_Returns500() throws Exception {
        // Arrange: Mock service to throw exception
        when(weatherService.getWeatherByCity(anyString()))
                .thenThrow(new RuntimeException("API error"));

        // Act & Assert: Verify error handling
        mockMvc.perform(get("/api/weather/InvalidCity"))
                .andExpect(status().isInternalServerError());
    }
}