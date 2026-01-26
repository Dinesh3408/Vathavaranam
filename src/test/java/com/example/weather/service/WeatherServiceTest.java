package com.example.weather.service;

import com.example.weather.dto.IpApiResponse;
import com.example.weather.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherServiceTest {

    private WeatherProvider weatherProvider;

    private IpLocationService ipLocationService;

    private WeatherService weatherService;

    @BeforeEach
    void setup() {
        System.out.println("Running setup...");
        weatherProvider = mock(WeatherProvider.class);
        ipLocationService = mock(IpLocationService.class);
        weatherService = new WeatherService(weatherProvider, ipLocationService);
    }

    @Test
    void getWeatherByCity_DelegatesToProvider() {
        Weather mockWeather = new Weather();
        mockWeather.setCity("London");
        when(weatherProvider.getWeatherByCity("London")).thenReturn(mockWeather);

        Weather result = weatherService.getWeatherByCity("London");

        assertNotNull(result);
        assertEquals("London", result.getCity());
        verify(weatherProvider).getWeatherByCity("London");
    }

    @Test
    void getWeatherByCoordinates_DelegatesToProvider() {
        Weather mockWeather = new Weather();
        mockWeather.setCity("Mumbai");
        when(weatherProvider.getWeatherByCoordinates(10.0, 20.0)).thenReturn(mockWeather);

        Weather result = weatherService.getWeatherByCoordinates(10.0, 20.0);

        assertEquals("Mumbai", result.getCity());
        verify(weatherProvider).getWeatherByCoordinates(10.0, 20.0);
    }

    @Test
    void getWeatherByIp_Success() {
        IpApiResponse mockLocation = new IpApiResponse();
        mockLocation.setCity("New York");
        when(ipLocationService.getLocation("1.2.3.4")).thenReturn(mockLocation);

        Weather mockWeather = new Weather();
        mockWeather.setCity("New York");
        when(weatherProvider.getWeatherByCity("New York")).thenReturn(mockWeather);

        Weather result = weatherService.getWeatherByIp("1.2.3.4");

        assertNotNull(result);
        assertEquals("New York", result.getCity());
        verify(ipLocationService).getLocation("1.2.3.4");
        verify(weatherProvider).getWeatherByCity("New York");
    }
}
