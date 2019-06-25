package com.raju.tripplanner.utils.ApiResponse;

import com.google.gson.annotations.SerializedName;
import com.raju.tripplanner.models.User;

public class UserResponse {

    @SerializedName("updatedUser")
    private User user;

    public User getUser() {
        return user;
    }
}
