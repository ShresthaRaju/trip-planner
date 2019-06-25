package com.raju.tripplanner.DAO;

import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {

    @POST("sign-up")
    Call<UserResponse> registerUser(@Body User user);

    @POST("sign-in")
    Call<SignInResponse> signIn(@Body User user);

}
