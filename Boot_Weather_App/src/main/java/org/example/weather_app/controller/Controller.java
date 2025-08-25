package org.example.weather_app.controller;

import org.example.weather_app.dto.WeatherForeCast;
import org.example.weather_app.dto.WeatherResponse;
import org.example.weather_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@CrossOrigin
public class Controller {

    @Autowired
    private WeatherService service;

    @GetMapping("/{city}")
    private String getWeatherData(@PathVariable String city)
    {
        return service.test();
    }

    @GetMapping("/my/{city}")
    private WeatherResponse getWeather(@PathVariable String city)
    {
        return service.getData(city);
    }

    @GetMapping("/forecast")
    private WeatherForeCast getForecast(@RequestParam String city, @RequestParam int days)
    {
        return service.getForeCast(city, days);
    }
}
