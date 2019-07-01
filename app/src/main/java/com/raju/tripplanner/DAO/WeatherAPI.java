package com.raju.tripplanner.DAO;

import com.raju.tripplanner.models.Weather.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("forecast.json")
    Call<Forecast> fetchWeather(@Query("key") String key, @Query("q") String query, @Query("days") int days);
}
