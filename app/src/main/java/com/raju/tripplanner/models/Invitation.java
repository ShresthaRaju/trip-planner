package com.raju.tripplanner.models;

import com.google.gson.annotations.SerializedName;

public class Invitation {

    @SerializedName("_id")
    private String id;
    private String invitee, invitedOn;
    private boolean seen, notified;
    private User inviter;
    private Trip invitedTo;

    public String getId() {
        return id;
    }

    public String getInvitee() {
        return invitee;
    }

    public String getInvitedOn() {
        return invitedOn;
    }

    public boolean isSeen() {
        return seen;
    }

    public boolean isNotified() {
        return notified;
    }

    public User getInviter() {
        return inviter;
    }

    public Trip getInvitedTo() {
        return invitedTo;
    }
}
