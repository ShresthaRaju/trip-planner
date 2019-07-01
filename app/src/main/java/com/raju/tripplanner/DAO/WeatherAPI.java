package com.raju.tripplanner.DAO;

import com.raju.tripplanner.models.WeatherResult.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("forecast.json")
    Call<Weather> fetchWeather(@Query("key") String key, @Query("q") String query, @Query("days") int days);
}
