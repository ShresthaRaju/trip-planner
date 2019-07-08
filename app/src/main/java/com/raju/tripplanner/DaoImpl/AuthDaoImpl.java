package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

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
import com.raju.tripplanner.utils.UserSession;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthDaoImpl {

    private Activity activity;
    private AuthAPI authAPI;
    private UserSession userSession;
    private APIError apiError;
    private Gson gson;
    private AuthListener authListener;

    public AuthDaoImpl(Activity activity) {
        this.activity = activity;
        authAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(AuthAPI.class);
        userSession = new UserSession(activity);
        gson = new GsonBuilder().create();
        apiError = new APIError();
    }

    public void signUp(User user) {
        Call<UserResponse> signUpCall = authAPI.registerUser(user);

        // enqueue method runs the api call in background thread
        signUpCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        authListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                authListener.onSignedUp(response.body().getUser());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signIn(User user) {
        Call<SignInResponse> signInCall = authAPI.signIn(user);

        signInCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        authListener.onError(apiError.getError());
                    } catch (IOException e) {
                        Log.e("IOException", e.getMessage());
                    }
                    return;
                }

                authListener.onSignedIn(response.body().getUser(), response.body().getAuthToken());
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface AuthListener {
        void onSignedUp(User registeredUser);

        void onSignedIn(User authUser, String authToken);

        void onError(Error error);
    }

    public void setAuthListener(AuthListener authListener) {
        this.authListener = authListener;
    }

}
