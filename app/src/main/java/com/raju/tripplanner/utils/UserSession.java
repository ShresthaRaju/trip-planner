package com.raju.tripplanner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.raju.tripplanner.R;

public class UserSession {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String USER_ID = "LOGGED_IN_USER_ID";

    public UserSession(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.user_session), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void startSession(String userId) {
        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public boolean getSession() {
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public void endSession() {

        editor.putBoolean(IS_USER_LOGGED_IN, false);
        editor.remove(USER_ID);
        editor.commit();
    }
}
