package com.raju.tripplanner.DAO;

import com.raju.tripplanner.utils.ApiResponse.PhotoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacePhotoAPI {

    @GET("json")
    Call<PhotoResponse> fetchPhoto(@Query("placeid") String placeId, @Query("fields") String field, @Query("key") String key);
}
