package com.example.weather.service;
import com.example.weather.dto.OpenWeatherMapResponse;

import com.example.weather.model.Weather;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {
    private MockWebServer mockWebServer;
    private WeatherService weatherService;

    @BeforeEach
    void setup() throws IOException {
        //start mock server before each start
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        //create weather service with mock server url
        weatherService = new WeatherService();
        weatherService.apiUrl = mockWebServer.url("/").toString();
        weatherService.apiKey ="49a4b21be343b524dbfb040ec30eccc7";
        weatherService.restTemplate = new RestTemplate();

    }
    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    @Test
    void getWeatherByCity_Success(){
        //Arrange : Prepare mock response
        String mockresponse = """
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
                            "speed": 12.5
                        },
                        "sys": {
                            "country": "GB"
                        },
                        "dt": 1702646400
                    }
                    """;
        mockWebServer.enqueue(new MockResponse().setBody(mockresponse).addHeader("Content-Type", "application/json"));
        // Act: Call the method
        Weather weather = weatherService.getWeatherByCity("London");
        //Assert:Verify the results
        assertNotNull(weather, "weather object should not be null");
        assertEquals("London", weather.getCity());
        assertEquals(15.5, weather.getTemperature(), 0.01);
        assertEquals(13.2, weather.getFeelsLike(), 0.01);
        assertEquals(65, weather.getHumidity());
        assertEquals(1013, weather.getPressure());
        assertEquals("cloudy", weather.getDescription());
        assertEquals(12.5, weather.getWindSpeed(), 0.01);
        assertEquals("GB", weather.getCountry());

    }
    @Test
    void getWeatherByCoordinates_Success(){
        //Arrange
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

        //Act
        Weather weather = weatherService.getWeatherByCoordinates(19.0760, 72.8777);

        //Assert
        assertNotNull(weather);
        assertEquals("Mumbai", weather.getCity());
        assertEquals(28.5, weather.getTemperature(), 0.01);
        assertEquals("IN",weather.getCountry());

    }
    @Test
    void getWeatherByCity_HandlesNullWeatherDescription(){
        //Test when weather description is missing
        String mockResponse = """
            {
                "name": "TestCity",
                "main": {
                    "temp": 20.0,
                    "feels_like": 19.0,
                    "humidity": 50,
                    "pressure": 1000
                },
                "weather": [],
                "wind": {
                    "speed": 5.0
                },
                "sys": {
                    "country": "US"
                },
                "dt": 1702650000
            }
            """;
        mockWebServer.enqueue(new MockResponse()
        .setBody(mockResponse).addHeader("Content-Type", "application/json"));

        Weather weather = weatherService.getWeatherByCity("TestCity");

        assertNotNull(weather);
        assertNull(weather.getDescription()); //Description should be null when weather array is empty

    }


}
