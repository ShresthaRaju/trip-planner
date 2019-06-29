package com.raju.tripplanner.models.MapResult;

import java.util.List;

public class Place {

    private String name;

    private List<Photo> photos;

    private float rating;

    public String getName() {
        return name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public float getRating() {
        return rating;
    }
}
