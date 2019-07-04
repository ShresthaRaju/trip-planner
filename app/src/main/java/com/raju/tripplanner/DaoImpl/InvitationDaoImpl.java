package com.raju.tripplanner.DaoImpl;

import android.app.Activity;
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
    private Activity activity;
    private UserSession userSession;
    private InvitationsListener invitationsListener;

    public InvitationDaoImpl(Activity activity) {
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
                    Toast.makeText(activity, "ERROR: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(activity, "Invited", Toast.LENGTH_LONG).show();
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

//                for (Invitation invitation : response.body().getInvitations().getInvitations()) {
//
//                    Log.i("invitation", invitation.getInviter().getFirstName() + " " + invitation.getInviter().getFamilyName() + " invited you to " + invitation.getInvitedTo().getName());
//
//                }
                invitationsListener.onInvitationsFetched(response.body().getInvitations().getInvitations());

            }

            @Override
            public void onFailure(Call<InvitationsResponse> call, Throwable t) {
                Toast.makeText(activity, "FAILED: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
