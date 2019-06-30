package com.raju.tripplanner.DAO;

import com.raju.tripplanner.utils.ApiResponse.NearbySearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesAPI {

    @GET("json")
    Call<NearbySearchResponse> fetchNearbyPlaces(@Query("location") String location, @Query("radius") double radius,
                                                 @Query("type") String type, @Query("key") String key);

}
