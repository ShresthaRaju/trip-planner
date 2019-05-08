package com.raju.tripplanner.DAO;

import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.AuthApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {

    @POST("sign-up")
    Call<AuthApiResponse> registerUser(@Body User user);

    @POST("sign-in")
    Call<String> signIn(@Body User user);

}
