package com.example.weather.service;

import com.example.weather.dto.ForecastResponse;
import com.example.weather.model.Weather;

public interface WeatherProvider {
    Weather getWeatherByCity(String city);

    Weather getWeatherByCoordinates(double lat, double lon);

    ForecastResponse getForecastByCity(String city);
}
