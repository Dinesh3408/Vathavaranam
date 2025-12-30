package com.example.weather.dto;

import com.example.weather.model.Weather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.sun.tools.javac.Main;
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {
    @JsonProperty("name")
    private String cityName;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("weather")
    private List<WeatherInfo> weather;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("sys")
    private Sys sys;
    @JsonProperty("dt")
    private long timestamp;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private double temperature;
        @JsonProperty("feels_like")
        private double feelslike;
        @JsonProperty("humidity")
        private int humidity;
        @JsonProperty("pressure")
        private int pressure;

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getFeelslike() {
            return feelslike;
        }

        public void setFeelslike(double feelslike) {
            this.feelslike = feelslike;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo{
        @JsonProperty("description")
        private String description;
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }



    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind{
        @JsonProperty("speed")
        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        @JsonProperty("country")
        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }









}
