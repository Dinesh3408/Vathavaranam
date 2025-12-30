package com.example.weather.service;

import com.example.weather.dto.OpenWeatherMapResponse;
import com.example.weather.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class WeatherService {
    @Value("${openweathermap.api.key}")
    String apiKey;
    @Value("${openweathermap.api.url}")
    String apiUrl;

    RestTemplate restTemplate;
    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }
    public Weather getWeatherByCity(String city){
        String url = String.format("%s?q=%s&appid=%s&units=metric",
                apiUrl, city, apiKey);
        OpenWeatherMapResponse response = restTemplate.getForObject(url, OpenWeatherMapResponse.class);
        return mapToWeather(response);
    }
    public Weather getWeatherByCoordinates(double lat, double lon){
        String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                apiUrl, lat, lon, apiKey);
        OpenWeatherMapResponse response = restTemplate.getForObject(url,
                OpenWeatherMapResponse.class);
        return mapToWeather(response);
    }
    private Weather mapToWeather(OpenWeatherMapResponse response) {
        Weather weather = new Weather();
        weather.setCity(response.getCityName());
        weather.setTemperature(response.getMain().getTemperature());
        weather.setFeelsLike(response.getMain().getFeelslike());
        weather.setHumidity(response.getMain().getHumidity());
        weather.setPressure(response.getMain().getPressure());
        weather.setWindSpeed(response.getWind().getSpeed());
        weather.setCountry(response.getSys().getCountry());
        weather.setTimestamp(response.getTimestamp());

        if (response.getWeather() != null && !response.getWeather().isEmpty()) {
            weather.setDescription(response.getWeather().get(0).getDescription());
        }

        return weather;
    }
}
