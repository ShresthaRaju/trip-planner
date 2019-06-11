package com.raju.tripplanner.utils;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    private boolean success;
    private String message;
    @SerializedName("auth_token")
    private String authToken;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return authToken;
    }
}
