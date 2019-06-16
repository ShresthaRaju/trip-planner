package com.raju.tripplanner.models;

import java.io.Serializable;

public class Destination implements Serializable {

    private double lat;
    private double lng;
    private String photoUrl;

    public Destination(double lat, double lng, String photoUrl) {
        this.lat = lat;
        this.lng = lng;
        this.photoUrl = photoUrl;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
