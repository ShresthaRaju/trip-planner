package com.raju.tripplanner.utils.ApiResponse;

import com.google.gson.annotations.SerializedName;
import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.models.User;

import java.util.List;

public class TripResponse {

    private Trip trip;
    @SerializedName("trips")
    private List<Trip> myTrips;

    public Trip getTrip() {
        return trip;
    }

    public List<Trip> getMyTrips() {
        return myTrips;
    }

}
