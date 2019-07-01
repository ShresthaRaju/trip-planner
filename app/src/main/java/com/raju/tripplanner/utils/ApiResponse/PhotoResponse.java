package com.raju.tripplanner.utils.ApiResponse;

import com.google.gson.annotations.SerializedName;
import com.raju.tripplanner.models.MapResult.PlacePhotoResult;

public class PhotoResponse {

    @SerializedName("result")
    private PlacePhotoResult placePhotoResult;

    public PlacePhotoResult getPlacePhotoResult() {
        return placePhotoResult;
    }
}
