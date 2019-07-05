package com.raju.tripplanner.DAO;

import com.raju.tripplanner.utils.ApiResponse.InvitationsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface InvitationAPI {

    @FormUrlEncoded
    @POST("users/send-invitation")
    Call<Void> sendInvitation(@Header("Authorization") String token, @Field("invitee") String invitee, @Field("trip") String trip);

    @GET("users/invitations")
    Call<InvitationsResponse> getInvitations(@Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("users/invitations/setNotified")
    Call<Void> setNotified(@Header("Authorization") String token, @Field("invitationId") String invitationId);

    @PUT("users/invitations/accept")
    Call<Void> acceptInvitation(@Header("Authorization") String token);

    @PUT("users/invitations/decline")
    Call<Void> declineInvitation(@Header("Authorization") String token);
}
