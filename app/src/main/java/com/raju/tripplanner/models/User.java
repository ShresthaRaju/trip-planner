package com.raju.tripplanner.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String id;

    private String firstName, familyName, email, username, password, displayPicture;

    public User(String firstName, String familyName, String email, String username, String password) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }
}
