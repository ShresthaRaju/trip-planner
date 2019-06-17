package com.raju.tripplanner.ApiCalls;

import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.ApiResponse.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {

    @POST("sign-up")
    Call<SignUpResponse> registerUser(@Body User user);

    @POST("sign-in")
    Call<SignInResponse> signIn(@Body User user);

}
