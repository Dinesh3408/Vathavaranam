package com.example.weather.service;

import com.example.weather.dto.OpenWeatherMapResponse;
import com.example.weather.model.Weather;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OpenWeatherMapProviderTest {
    private MockWebServer mockWebServer;
    private OpenWeatherMapProvider weatherProvider;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        weatherProvider = new OpenWeatherMapProvider();
        // Inject values
        ReflectionTestUtils.setField(weatherProvider, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(weatherProvider, "apiUrl", mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getWeatherByCity_Success() {
        String mockResponse = """
                {
                        "name": "London",
                        "main": {
                            "temp": 15.5,
                            "feels_like": 13.2,
                            "humidity": 65,
                            "pressure": 1013
                        },
                        "weather": [
                            {
                                "description": "cloudy"
                            }
                        ],
                        "wind": {
                            "speed": 12.5,
                            "deg": 270,
                            "gust": 15.0
                        },
                        "sys": {
                            "country": "GB"
                        },
                        "dt": 1702646400,
                        "rain": {
                            "1h": 2.5
                        }
                    }
                    """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).addHeader("Content-Type", "application/json"));

        Weather weather = weatherProvider.getWeatherByCity("London");

        assertNotNull(weather);
        assertEquals("London", weather.getCity());
        assertEquals(15.5, weather.getTemperature(), 0.01);
        assertEquals(270, weather.getWindDirection(), 0.01);
        assertEquals(2.5, weather.getRainVolume(), 0.01);
    }

    @Test
    void getWeatherByCoordinates_Success() {
        String mockResponse = """
                {
                    "name":"Mumbai",
                    "main": {
                            "temp": 28.5,
                            "feels_like": 31.0,
                            "humidity": 75,
                            "pressure": 1012
                        },
                        "weather": [
                            {
                                "description": "haze"
                            }
                        ],
                        "wind": {
                            "speed": 3.5
                        },
                        "sys": {
                            "country": "IN"
                        },
                        "dt": 1702650000
                    }
                """;
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse).addHeader("Content-Type", "application/json"));

        Weather weather = weatherProvider.getWeatherByCoordinates(19.0760, 72.8777);

        assertNotNull(weather);
        assertEquals("Mumbai", weather.getCity());
    }
}
