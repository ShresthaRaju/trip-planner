package com.raju.tripplanner.utils.ApiResponse;

import com.raju.tripplanner.models.User;

public class SignInResponse {

    private User user;
    private String authToken;


    public User getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }
}
