# Weather API

A RESTful API built with Spring Boot that provides real-time weather data from OpenWeatherMap.

## Features
- ☁️ Current weather by city name
- 📍 Weather by GPS coordinates
- 🌡️ Temperature, humidity, wind speed, and more
- 🌐 Beautiful web interface included

## Tech Stack
- Java 17
- Spring Boot 3.2.0
- OpenWeatherMap API
- Maven

## Local Setup

1. Clone the repository
2. Get API key from [OpenWeatherMap](https://openweathermap.org/api)
3. Set environment variable:
```bash
   export OPENWEATHER_API_KEY=your_key_here
```
4. Run:
```bash
   mvn spring-boot:run
```
5. Visit: http://localhost:8080

## API Endpoints

### Get Weather by City
```
GET /api/weather/{city}
```
Example: `/api/weather/London`

### Get Weather by Coordinates
```
GET /api/weather/coordinates?lat={lat}&lon={lon}
```
Example: `/api/weather/coordinates?lat=51.5074&lon=-0.1278`

## Response Example
```json
{
  "city": "London",
  "temperature": 15.5,
  "feelsLike": 13.2,
  "humidity": 65,
  "description": "cloudy",
  "windSpeed": 12.5,
  "pressure": 1013,
  "country": "GB",
  "timestamp": 1702646400
}
```

## Live Demo
[Coming Soon]

## Future Enhancements
- [ ] 5-day weather forecast
- [ ] Weather alerts
- [ ] User authentication
- [ ] Favorite cities
- [ ] Historical data
- [ ] Mobile app

## Contributing
Pull requests are welcome!

## License
MIT

## Contact
[Dhananjaya Davala] - [davaladinesh34@gmail.com/LinkedIn: https://www.linkedin.com/in/dhananjaya-davala-045615ba/ ]
```

**2. Add `Procfile` (for Heroku):**
```
web: java -jar target/weather-0.0.1-SNAPSHOT.jar