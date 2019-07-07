package com.raju.tripplanner.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.raju.tripplanner.BroadcastReceiver.NotificationActionReceiver;
import com.raju.tripplanner.DAO.InvitationAPI;
import com.raju.tripplanner.MainActivity;
import com.raju.tripplanner.R;
import com.raju.tripplanner.models.Invitation;
import com.raju.tripplanner.utils.ApiResponse.InvitationsResponse;
import com.raju.tripplanner.utils.RetrofitClient;
import com.raju.tripplanner.utils.Tools;
import com.raju.tripplanner.utils.UserSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.raju.tripplanner.utils.App.CHANNEL_ID;

public class TripNotificationService extends Service {

    private NotificationManagerCompat notificationManagerCompat;
    private Handler handler = new Handler();
    private InvitationAPI invitationAPI;
    private UserSession userSession;
    public static final String INVITATION_ID = "Invitation_Id";
    public static final String INVITATION_ACCEPTED = "Invitation_Accepted";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        invitationAPI = RetrofitClient.getInstance(Tools.BASE_URL).create(InvitationAPI.class);
        userSession = new UserSession(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 5000);

        return START_STICKY;
    }

    private void sendNotification(String invitationId, String message) {

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainActivity, 0);

//        action accept invitation
        Intent acceptedBroadcastIntent = new Intent(this, NotificationActionReceiver.class);
        acceptedBroadcastIntent.putExtra(INVITATION_ID, invitationId);
        acceptedBroadcastIntent.putExtra(INVITATION_ACCEPTED, true);
        PendingIntent acceptedActionIntent = PendingIntent.getBroadcast(this, 1, acceptedBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        action decline invitation
        Intent declinedBroadcastIntent = new Intent(this, NotificationActionReceiver.class);
        declinedBroadcastIntent.putExtra(INVITATION_ID, invitationId);
        declinedBroadcastIntent.putExtra(INVITATION_ACCEPTED, false);
        PendingIntent declinedActionIntent = PendingIntent.getBroadcast(this, 2, declinedBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification tripNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("New Invitation")
                .setContentText(message)
                .setColor(getResources().getColor(R.color.teal_500))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher_round, "Accept", acceptedActionIntent)
                .addAction(R.mipmap.ic_launcher_round, "Decline", declinedActionIntent)
                .build();

        notificationManagerCompat.notify(7, tripNotification);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkForNewNotification();
            handler.postDelayed(this, 10000);
        }
    };

    private void checkForNewNotification() {
        Call<InvitationsResponse> myInvitationsCall = invitationAPI.getInvitations("Bearer " + userSession.getAuthToken());
        myInvitationsCall.enqueue(new Callback<InvitationsResponse>() {
            @Override
            public void onResponse(Call<InvitationsResponse> call, Response<InvitationsResponse> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "ERROR SERVICE: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                for (Invitation invitation : response.body().getInvitations().getInvitations()) {
                    if (!invitation.isNotified()) {
                        String inviter = invitation.getInviter().getFirstName() + " " + invitation.getInviter().getFamilyName();
                        String text = " has invited you to ";
                        String tripName = invitation.getInvitedTo().getName();

                        sendNotification(invitation.getId(), inviter + text + tripName);

                        setInvitationNotified(invitation.getId());

                    }
                }
            }

            @Override
            public void onFailure(Call<InvitationsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "FAILED SERVICE: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setInvitationNotified(String invitationId) {
        Call<Void> invitationNotifiedCall = invitationAPI.setNotified("Bearer " + userSession.getAuthToken(), invitationId);
        invitationNotifiedCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
