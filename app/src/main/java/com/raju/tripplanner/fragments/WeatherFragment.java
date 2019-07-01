package com.raju.tripplanner.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.BuildConfig;
import com.raju.tripplanner.DAO.WeatherAPI;
import com.raju.tripplanner.R;
import com.raju.tripplanner.adapters.WeatherAdapter;
import com.raju.tripplanner.models.WeatherResult.ForecastDay;
import com.raju.tripplanner.models.WeatherResult.Weather;
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
    private WeatherAdapter weatherAdapter;
    private RecyclerView weatherContainer;

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
        View weatherView = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherContainer = weatherView.findViewById(R.id.weather_container);
        weatherContainer.setHasFixedSize(true);
        weatherContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        return weatherView;
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
        Call<Weather> weatherCall = weatherAPI.fetchWeather(BuildConfig.WeatherApi, lat + "," + lng, 7);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                List<ForecastDay> forecasts = response.body().getForecast().getForecastday();

                weatherAdapter = new WeatherAdapter(getActivity(), forecasts);
                weatherContainer.setAdapter(weatherAdapter);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getActivity(), "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
