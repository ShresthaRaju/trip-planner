package com.raju.tripplanner.models.WeatherResult;

import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("maxtemp_c")
    private float maxTemp;

    @SerializedName("mintemp_c")
    private float minTemp;

    @SerializedName("avgtemp_c")
    private float avgTemp;

    @SerializedName("maxwind_kph")
    private float wind;

    @SerializedName("avgvis_km")
    private float visibility;

    @SerializedName("avghumidity")
    private float humidity;

    private float uv;

    private Condition condition;

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getAvgTemp() {
        return avgTemp;
    }

    public float getWind() {
        return wind;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getUv() {
        return uv;
    }

    public Condition getCondition() {
        return condition;
    }
}
