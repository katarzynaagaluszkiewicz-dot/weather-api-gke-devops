package com.devops.weather.controller;

import com.devops.weather.model.WeatherModel;
import com.devops.weather.service.WeatherApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherApiService weatherService;

    public WeatherController(WeatherApiService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String cityName) {
        try {
            WeatherModel weather = weatherService.getWeatherByCity(cityName);
            return ResponseEntity.ok(weather);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch weather data"));
        }
    }

    @GetMapping("/coordinates")
    public ResponseEntity<?> getWeatherByCoordinates(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) String city) {
        try {
            WeatherModel weather = weatherService.getWeather(lat, lon, city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch weather data"));
        }
    }

    @PostMapping("/multiple")
    public ResponseEntity<Map<String, WeatherModel>> getMultipleCities(
            @RequestBody List<String> cities) {
        Map<String, WeatherModel> results = weatherService.getMultipleCitiesWeather(cities);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAvailableCities() {
        List<String> cities = weatherService.getAvailableCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Weather API Proxy",
                "version", "1.0.0"
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "name", "Weather API Proxy",
                "description", "Simple weather API using Open-Meteo",
                "version", "1.0.0",
                "endpoints", Map.of(
                        "GET /api/weather/city/{city}", "Get weather by city name",
                        "GET /api/weather/coordinates", "Get weather by coordinates",
                        "POST /api/weather/multiple", "Get weather for multiple cities",
                        "GET /api/weather/cities", "List available cities"
                ),
                "availableCities", weatherService.getAvailableCities().size(),
                "dataSource", "Open-Meteo API (https://open-meteo.com/)"
        ));
    }
}