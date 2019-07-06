package com.raju.tripplanner.DaoImpl;

import android.content.Context;
import android.widget.Toast;

import com.raju.tripplanner.DAO.InvitationAPI;
import com.raju.tripplanner.models.Invitation;
import com.raju.tripplanner.utils.ApiResponse.InvitationsResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationDaoImpl {

    private InvitationAPI invitationAPI;
    private Context activity;
    private UserSession userSession;
    private InvitationsListener invitationsListener;

    public InvitationDaoImpl(Context activity) {
        this.activity = activity;
        invitationAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(InvitationAPI.class);
        userSession = new UserSession(activity);
    }

    public void sendInvitation(String invitee, String trip) {
        Call<Void> sendInvitationCall = invitationAPI.sendInvitation("Bearer " + userSession.getAuthToken(), invitee, trip);
        sendInvitationCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 409) {
                        Toast.makeText(activity, "Already invited", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                Toast.makeText(activity, "Invitation Sent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getMyInvitations() {
        Call<InvitationsResponse> myInvitationsCall = invitationAPI.getInvitations("Bearer " + userSession.getAuthToken());
        myInvitationsCall.enqueue(new Callback<InvitationsResponse>() {
            @Override
            public void onResponse(Call<InvitationsResponse> call, Response<InvitationsResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                invitationsListener.onInvitationsFetched(response.body().getInvitations().getInvitations());

            }

            @Override
            public void onFailure(Call<InvitationsResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void acceptInvitation(String invitationId) {
        Call<Void> acceptInvitationCall = invitationAPI.acceptInvitation("Bearer " + userSession.getAuthToken(), invitationId);
        acceptInvitationCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR ACCEPT: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Invitation Accepted", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "ACCEPT FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void declineInvitation(String invitationId) {
        Call<Void> declineInvitationCall = invitationAPI.declineInvitation("Bearer " + userSession.getAuthToken(), invitationId);
        declineInvitationCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(activity, "ERROR DECLINE: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Invitation Declined", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "DECLINE FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface InvitationsListener {
        void onInvitationsFetched(List<Invitation> myInvitations);
    }

    public void setInvitationsListener(InvitationsListener invitationsListener) {
        this.invitationsListener = invitationsListener;
    }
}
