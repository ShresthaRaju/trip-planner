package com.raju.tripplanner.utils.ApiResponse;

import com.raju.tripplanner.models.MapResult.Place;

import java.util.List;

public class NearbySearchResponse {

    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }
}
