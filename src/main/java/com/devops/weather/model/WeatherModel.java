package com.devops.weather.model;

import java.time.LocalDateTime;

public class WeatherModel {private String city;
    private double latitude;
    private double longitude;
    private double temperature;
    private String description;
    private double windSpeed;
    private int humidity;
    private LocalDateTime timestamp;
    private String source;

    public WeatherModel() {
        this.timestamp = LocalDateTime.now();
        this.source = "Open-Meteo API";
    }


    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
