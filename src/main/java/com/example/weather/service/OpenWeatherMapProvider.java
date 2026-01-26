package com.example.weather.service;

import com.example.weather.dto.ForecastResponse;
import com.example.weather.dto.OpenWeatherMapResponse;
import com.example.weather.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenWeatherMapProvider implements WeatherProvider {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenWeatherMapProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Weather getWeatherByCity(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric",
                apiUrl, city, apiKey);
        OpenWeatherMapResponse response = restTemplate.getForObject(url, OpenWeatherMapResponse.class);
        return mapToWeather(response);
    }

    @Override
    public Weather getWeatherByCoordinates(double lat, double lon) {
        String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                apiUrl, lat, lon, apiKey);
        OpenWeatherMapResponse response = restTemplate.getForObject(url,
                OpenWeatherMapResponse.class);
        return mapToWeather(response);
    }

    @Override
    public ForecastResponse getForecastByCity(String city) {
        String forecastUrl = apiUrl.replace("/weather", "/forecast");
        String url = String.format("%s?q=%s&appid=%s&units=metric",
                forecastUrl, city, apiKey);
        return restTemplate.getForObject(url, ForecastResponse.class);
    }

    private Weather mapToWeather(OpenWeatherMapResponse response) {
        if (response == null)
            return null;

        Weather weather = new Weather();
        weather.setCity(response.getCityName());

        if (response.getMain() != null) {
            weather.setTemperature(response.getMain().getTemperature());
            weather.setFeelsLike(response.getMain().getFeelslike());
            weather.setHumidity(response.getMain().getHumidity());
            weather.setPressure(response.getMain().getPressure());
        }

        if (response.getWind() != null) {
            weather.setWindSpeed(response.getWind().getSpeed());
            weather.setWindDirection(response.getWind().getDeg());
            weather.setWindGust(response.getWind().getGust());
        }

        if (response.getSys() != null) {
            weather.setCountry(response.getSys().getCountry());
        }

        weather.setTimestamp(response.getTimestamp());

        if (response.getRain() != null) {
            weather.setRainVolume(response.getRain().getOneHour());
        }

        if (response.getSnow() != null) {
            weather.setSnowVolume(response.getSnow().getOneHour());
        }

        if (response.getWeather() != null && !response.getWeather().isEmpty()) {
            weather.setDescription(response.getWeather().get(0).getDescription());
        }

        return weather;
    }
}
