package com.devops.weather.service;

import com.devops.weather.model.WeatherModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, double[]> cityCoordinates = Map.of(
            "warsaw", new double[]{52.2297, 21.0122},
            "krakow", new double[]{50.0647, 19.9450},
            "gdansk", new double[]{54.3520, 18.6466},
            "wroclaw", new double[]{51.1079, 17.0385},
            "poznan", new double[]{52.4064, 16.9252},
            "london", new double[]{51.5074, -0.1278},
            "paris", new double[]{48.8566, 2.3522},
            "berlin", new double[]{52.5200, 13.4050},
            "newyork", new double[]{40.7128, -74.0060},
            "tokyo", new double[]{35.6762, 139.6503}
    );

    public WeatherApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "weather", key = "#latitude + '_' + #longitude")
    public WeatherModel getWeather(double latitude, double longitude, String cityName) {

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m",
                latitude, longitude
        );

        try {
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode current = root.get("current");

            WeatherModel weather = new WeatherModel();
            weather.setCity(cityName != null ? cityName : "Unknown");
            weather.setLatitude(latitude);
            weather.setLongitude(longitude);
            weather.setTemperature(current.get("temperature_2m").asDouble());
            weather.setHumidity(current.get("relative_humidity_2m").asInt());
            weather.setWindSpeed(current.get("wind_speed_10m").asDouble());
            weather.setDescription(getWeatherDescription(current.get("weather_code").asInt()));

            return weather;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }


    public WeatherModel getWeatherByCity(String city) {
        String cityLower = city.toLowerCase().replaceAll("\\s+", "");

        double[] coords = cityCoordinates.get(cityLower);

        if (coords == null) {
            throw new IllegalArgumentException("City not found. Available cities: " +
                    String.join(", ", cityCoordinates.keySet()));
        }

        return getWeather(coords[0], coords[1], capitalizeCity(city));
    }

    public List<String> getAvailableCities() {
        List<String> cities = new ArrayList<>(cityCoordinates.keySet());
        cities.sort(String::compareTo);
        return cities.stream()
                .map(this::capitalizeCity)
                .toList();
    }

    public Map<String, WeatherModel> getMultipleCitiesWeather(List<String> cities) {
        Map<String, WeatherModel> results = new HashMap<>();

        for (String city : cities) {
            try {
                WeatherModel weather = getWeatherByCity(city);
                results.put(city, weather);
            } catch (Exception e) {
                continue;
            }
        }

        return results;
    }

    private String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1, 2, 3 -> "Partly cloudy";
            case 45, 48 -> "Foggy";
            case 51, 53, 55 -> "Drizzle";
            case 61, 63, 65 -> "Rain";
            case 71, 73, 75 -> "Snow";
            case 77 -> "Snow grains";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95 -> "Thunderstorm";
            case 96, 99 -> "Thunderstorm with hail";
            default -> "Unknown";
        };
    }

    private String capitalizeCity(String city) {
        if (city == null || city.isEmpty()) {
            return city;
        }
        return city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
    }
}

