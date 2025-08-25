package org.example.weather_app.service;

import org.example.weather_app.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.forecast.url}")
    private String apiForecastUrl;

    // RestTemplate allows a class to consume api of another class
    private RestTemplate template = new RestTemplate();

    public String test()
    {
        return "Good";
    }

    public WeatherResponse getData(String city)
    {
        // Consume API
        String url = apiUrl + "?key=" + apiKey + "&q=" + city;
        Root response = template.getForObject(url, Root.class);
//        // ( url and , returning objects that is local and current only na so they are in root.class)
//        return response;




//        CustomAPI
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCity(response.getLocation().name);
        weatherResponse.setRegion(response.getLocation().region);
        weatherResponse.setCountry(response.getLocation().country);
        weatherResponse.setCondition(response.getCurrent().getCondition().getText());
        weatherResponse.setTemperature(response.getCurrent().getTemp_c());

        return weatherResponse;
    }

    public WeatherForeCast getForeCast(String city, int days)
    {
        // in forecast we want weatherResponse object na so just call Upper method
        WeatherResponse weatherResponse = getData(city);



        // now we want dayTemp but there are multiple days Temp so it should in list
        WeatherForeCast response = new WeatherForeCast();
        response.setWeatherResponse(weatherResponse);

        // now we want list of days so first get whole data
        List<DayTemp> dayList = new ArrayList<>();
        String url = apiForecastUrl + "?key=" + apiKey + "&q=" + city + "&days=" + days;
        Root apiResponse = template.getForObject(url, Root.class);
        Forecast forecast = apiResponse.getForecast();
        for(Forecastday day : forecast.getForecastday())
        {
            // i will create new day for every forecast na so
            DayTemp d = new DayTemp();
            d.setDate(day.getDate());
            d.setMinTemp(day.getDay().mintemp_c);
            d.setAvgTemp(day.getDay().avgtemp_c);
            d.setMaxTemp(day.getDay().maxtemp_c);
            dayList.add(d);
        }

        response.setDayTemp(dayList);
        return response;
    }
}
