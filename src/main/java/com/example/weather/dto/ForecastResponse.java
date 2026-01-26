package com.example.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("list")
    private List<ForecastItem> list;

    @JsonProperty("city")
    private City city;

    public List<ForecastItem> getList() {
        return list;
    }

    public void setList(List<ForecastItem> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastItem implements Serializable {
        private static final long serialVersionUID = 1L;
        @JsonProperty("dt")
        private long dt;

        @JsonProperty("main")
        private OpenWeatherMapResponse.Main main;

        @JsonProperty("weather")
        private List<OpenWeatherMapResponse.WeatherInfo> weather;

        @JsonProperty("dt_txt")
        private String dtTxt;

        @JsonProperty("wind")
        private OpenWeatherMapResponse.Wind wind;

        @JsonProperty("rain")
        private OpenWeatherMapResponse.Rain rain;

        @JsonProperty("snow")
        private OpenWeatherMapResponse.Snow snow;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public OpenWeatherMapResponse.Main getMain() {
            return main;
        }

        public void setMain(OpenWeatherMapResponse.Main main) {
            this.main = main;
        }

        public List<OpenWeatherMapResponse.WeatherInfo> getWeather() {
            return weather;
        }

        public void setWeather(List<OpenWeatherMapResponse.WeatherInfo> weather) {
            this.weather = weather;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public void setDtTxt(String dtTxt) {
            this.dtTxt = dtTxt;
        }

        public OpenWeatherMapResponse.Wind getWind() {
            return wind;
        }

        public void setWind(OpenWeatherMapResponse.Wind wind) {
            this.wind = wind;
        }

        public OpenWeatherMapResponse.Rain getRain() {
            return rain;
        }

        public void setRain(OpenWeatherMapResponse.Rain rain) {
            this.rain = rain;
        }

        public OpenWeatherMapResponse.Snow getSnow() {
            return snow;
        }

        public void setSnow(OpenWeatherMapResponse.Snow snow) {
            this.snow = snow;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City implements Serializable {
        private static final long serialVersionUID = 1L;
        @JsonProperty("name")
        private String name;
        @JsonProperty("country")
        private String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
