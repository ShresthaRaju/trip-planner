package com.raju.tripplanner.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.raju.tripplanner.BuildConfig;
import com.raju.tripplanner.DAO.WeatherAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.Weather.Forecast;
import com.raju.tripplanner.models.Weather.ForecastDay;
import com.raju.tripplanner.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private static final String ARG_PARAMS1 = "latitude";
    private static final String ARG_PARAMS2 = "longitude";
    private WeatherAPI weatherAPI;
    private double lat, lng;

    public WeatherFragment() {

    }

    public static WeatherFragment newInstance(double latitude, double longitude) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAMS1, latitude);
        args.putDouble(ARG_PARAMS2, longitude);
        weatherFragment.setArguments(args);
        return weatherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherAPI = RetrofitClient.getInstance("http://api.apixu.com/v1/").create(WeatherAPI.class);

        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAMS1);
            lng = getArguments().getDouble(ARG_PARAMS2);
        }

        fetchWeather(lat, lng);
    }

    private void fetchWeather(double lat, double lng) {
        Call<Forecast> weatherCall = weatherAPI.fetchWeather(BuildConfig.WeatherApi, lat + "," + lng, 7);
        weatherCall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                List<ForecastDay> forecasts = response.body().getForecastday();
                for (ForecastDay forecast : forecasts) {
                    Log.i("weather", forecast.getDate() + "\n" + forecast.getDay().getAvgTemp());
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Toast.makeText(getActivity(), "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
