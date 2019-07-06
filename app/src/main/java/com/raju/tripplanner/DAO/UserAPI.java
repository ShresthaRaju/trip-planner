package com.raju.tripplanner.DAO;

import com.raju.tripplanner.utils.ApiResponse.AllUsersResponse;
import com.raju.tripplanner.utils.ApiResponse.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserAPI {

    @Multipart
    @PUT("users/upload-display-picture")
    Call<UserResponse> uploadDisplayPicture(@Header("Authorization") String token, @Part MultipartBody.Part displayPicture);

    @FormUrlEncoded
    @PUT("users/update-details")
    Call<UserResponse> updateDetails(@Header("Authorization") String token, @Field("firstName") String firstName,
                                     @Field("familyName") String familyName, @Field("email") String email, @Field("username") String username);

    @FormUrlEncoded
    @PUT("users/change-password")
    Call<Void> changePassword(@Header("Authorization") String token, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @POST("sign-out")
    Call<Void> signOut(@Header("Authorization") String token);

    @GET("users")
    Call<AllUsersResponse> getAllUsers(@Header("Authorization") String token);
}
