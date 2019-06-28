package com.raju.tripplanner.utils.ApiResponse;

import com.raju.tripplanner.models.MapResult.Restaurant;

import java.util.List;

public class NearbySearchResponse {

    private List<Restaurant> results;

    public List<Restaurant> getResults() {
        return results;
    }
}
