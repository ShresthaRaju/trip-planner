package com.raju.tripplanner.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Trip {

    private String name;

    @SerializedName("start_date")
    private Date startDate;

    @SerializedName("end_date")
    private Date endDate;

    @SerializedName("place")
    private Destination destination;

    private String creator;

    public Trip(String name, Date startDate, Date endDate, Destination destination, String creator) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getCreator() {
        return creator;
    }
}
