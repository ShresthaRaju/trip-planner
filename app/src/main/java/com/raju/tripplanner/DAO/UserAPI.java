package com.raju.tripplanner.DAO;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserAPI {

    @Multipart
    @PUT("users/upload-display-picture")
    Call<String> uploadDisplayPicture(@Header("Authorization") String token, @Part MultipartBody.Part displayPicture);

}
