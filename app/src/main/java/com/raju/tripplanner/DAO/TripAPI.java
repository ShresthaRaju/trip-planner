package com.raju.tripplanner.DAO;

import com.raju.tripplanner.models.Trip;
import com.raju.tripplanner.utils.ApiResponse.InviteesResponse;
import com.raju.tripplanner.utils.ApiResponse.TripResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TripAPI {

    @POST("my-trips")
    Call<TripResponse> createTrip(@Header("Authorization") String token, @Body Trip trip);

    @GET("my-trips")
    Call<TripResponse> getUserTrips(@Header("Authorization") String token);

    @GET("my-trips/{tripId}/{tripSlug}")
    Call<InviteesResponse> showTrip(@Header("Authorization") String token,
                                    @Path("tripId") String tripId, @Path("tripSlug") String tripSlug);

    @PUT("my-trips/{tripId}")
    Call<Void> updateTrip(@Header("Authorization") String token, @Path("tripId") String tripId, @Body Trip trip);

    @DELETE("my-trips/{tripId}")
    Call<Void> deleteTrip(@Header("Authorization") String token, @Path("tripId") String tripId);

}
