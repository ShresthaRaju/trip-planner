package com.raju.tripplanner.models;

public class InvitationItem {

    private String userId;
    private String userDp;
    private String username;

    public InvitationItem(String userId, String userDp, String username) {
        this.userId = userId;
        this.userDp = userDp;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDp() {
        return userDp;
    }

    public String getUsername() {
        return username;
    }
}
