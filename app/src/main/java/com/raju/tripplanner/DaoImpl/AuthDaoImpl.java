package com.raju.tripplanner.DaoImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raju.tripplanner.DAO.AuthAPI;
import com.raju.tripplanner.models.User;
import com.raju.tripplanner.utils.APIError;
import com.raju.tripplanner.utils.ApiResponse.SignInResponse;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;
import com.raju.tripplanner.utils.Error;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class AuthDaoImpl {

    private AuthAPI authAPI;
    private APIError apiError;
    private Gson gson;
    private AuthListener authListener;

    public AuthDaoImpl() {
        authAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(AuthAPI.class);
        gson = new GsonBuilder().create();
        apiError = new APIError();
    }

    public boolean signUp(User user) {
        boolean isSignUpSuccessful = false;
        Call<UserResponse> signUpCall = authAPI.registerUser(user);

        try {
            Response<UserResponse> signUpResponse = signUpCall.execute();
            if (!signUpResponse.isSuccessful()) {
                apiError = gson.fromJson(signUpResponse.errorBody().string(), APIError.class);
                authListener.onError(apiError.getError());
                return isSignUpSuccessful;
            }
            if (signUpResponse.body().getUser() != null) {
                isSignUpSuccessful = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSignUpSuccessful;
    }

    public SignInResponse signIn(User user) {
        SignInResponse signInResponse = null;
        Call<SignInResponse> signInCall = authAPI.signIn(user);

        try {
            Response<SignInResponse> loginResponse = signInCall.execute();
            if (!loginResponse.isSuccessful()) {
                apiError = gson.fromJson(loginResponse.errorBody().string(), APIError.class);
                authListener.onError(apiError.getError());
                return signInResponse;
            }
            if (loginResponse.body().isSuccess()) {
                signInResponse = loginResponse.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return signInResponse;
    }

    public interface AuthListener {
        void onError(Error error);
    }

    public void setAuthListener(AuthListener authListener) {
        this.authListener = authListener;
    }

}
