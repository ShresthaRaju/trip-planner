package com.raju.tripplanner.models;

public class Invitation {

    private String invitee, invitedOn;
    private boolean seen, notified;

    private User inviter;
    private Trip invitedTo;

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
