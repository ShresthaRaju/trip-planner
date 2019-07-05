package com.raju.tripplanner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.User;

public class UserSession {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String USER = "LOGGED_IN_USER";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";

    public UserSession(Context activity) {
        sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.user_session), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void startSession(User user, String authToken) {

        String userJson = new Gson().toJson(user);

        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.putString(USER, userJson);
        editor.putString(AUTH_TOKEN, authToken);

        editor.commit();
    }

    public boolean getSession() {
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public User getUser() {

        String userJson = sharedPreferences.getString(USER, "");
        User user = new Gson().fromJson(userJson, User.class);

        return user;
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, "");
    }

    public void endSession() {

        editor.putBoolean(IS_USER_LOGGED_IN, false);
        editor.remove(USER);
        editor.remove(AUTH_TOKEN);
        editor.commit();
    }
}
