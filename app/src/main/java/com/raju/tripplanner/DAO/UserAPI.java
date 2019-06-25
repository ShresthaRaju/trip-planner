package com.raju.tripplanner.DAO;

import com.raju.tripplanner.utils.ApiResponse.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPI {

    @Multipart
    @PUT("users/upload-display-picture")
    Call<UserResponse> uploadDisplayPicture(@Header("Authorization") String token, @Part MultipartBody.Part displayPicture, @Part("userId") RequestBody userId);

    @FormUrlEncoded
    @PUT("users/update-details/{userId}")
    Call<UserResponse> updateDetails(@Header("Authorization") String token, @Field("firstName") String firstName, @Field("familyName") String familyName, @Path("userId") String userId);

    @FormUrlEncoded
    @PUT("users/change-password/{userId}")
    Call<Void> changePassword(@Header("Authorization") String token, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, @Path("userId") String userId);

}
