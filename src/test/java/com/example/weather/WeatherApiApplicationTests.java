package com.example.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WeatherApiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void weatherServiceBeanExists() {
        // Verify that WeatherService bean is created
        assertNotNull(applicationContext.getBean("weatherService"));
    }

    @Test
    void weatherControllerBeanExists() {
        // Verify that WeatherController bean is created
        assertNotNull(applicationContext.getBean("weatherController"));
    }
}