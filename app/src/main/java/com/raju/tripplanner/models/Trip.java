package com.raju.tripplanner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trip implements Serializable {

    @SerializedName("_id")
    private String id;
    private String name, startDate, endDate, creator;

    @SerializedName("place")
    private Destination destination;

    public Trip(String name, String startDate, String endDate, Destination destination, String creator) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.creator = creator;
    }

    public Trip(String name, String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getCreator() {
        return creator;
    }
}
