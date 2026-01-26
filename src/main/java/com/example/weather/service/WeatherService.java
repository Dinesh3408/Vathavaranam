package com.example.weather.service;

import com.example.weather.dto.ForecastResponse;
import com.example.weather.model.Weather;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherProvider weatherProvider;
    private final IpLocationService ipLocationService;

    // Constructor Injection
    public WeatherService(WeatherProvider weatherProvider, IpLocationService ipLocationService) {
        this.weatherProvider = weatherProvider;
        this.ipLocationService = ipLocationService;
    }

    @Cacheable(value = "weather", key = "#city")
    public Weather getWeatherByCity(String city) {
        return weatherProvider.getWeatherByCity(city);
    }

    @Cacheable(value = "weather_coord", key = "#lat + '-' + #lon")
    public Weather getWeatherByCoordinates(double lat, double lon) {
        return weatherProvider.getWeatherByCoordinates(lat, lon);
    }

    @Cacheable(value = "forecast", key = "#city")
    public ForecastResponse getForecastByCity(String city) {
        return weatherProvider.getForecastByCity(city);
    }

    // New feature: Auto-detect location by IP
    public Weather getWeatherByIp(String ip) {
        // 1. Get location from IP
        com.example.weather.dto.IpApiResponse location = ipLocationService.getLocation(ip);

        if (location != null && location.getCity() != null) {
            // 2. Get weather for that city (this will be cached by getWeatherByCity if
            // called directly, but here we call internal method)
            // Note: Spring Cache 'self-invocation' doesn't trigger cache unless we use
            // AopContext or inject self.
            // For simplicity, we just call the provider directly or call the public method
            // (hoping for cache if external, but internal call skips proxy).
            // Better to call the method to allow future caching improvements or aspect
            // usage.
            // To ensure caching works on self-invocation, we would need @Autowired
            // WeatherService self;
            // but let's just call provider for now or simple method call.
            // Actually, since getForecastByCity is cached, let's try to reuse
            // getWeatherByCity.
            return getWeatherByCity(location.getCity());
        }
        return null;
    }
}
