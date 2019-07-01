package com.raju.tripplanner.models.WeatherResult;

public class Location {

    private String name, country;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return getName() + ", " + getCountry();
    }
}
