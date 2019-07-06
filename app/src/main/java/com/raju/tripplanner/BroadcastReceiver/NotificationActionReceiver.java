package com.raju.tripplanner.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.raju.tripplanner.DaoImpl.InvitationDaoImpl;

import static com.raju.tripplanner.service.TripNotificationService.INVITATION_ACCEPTED;
import static com.raju.tripplanner.service.TripNotificationService.INVITATION_ID;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        InvitationDaoImpl invitationDaoImpl = new InvitationDaoImpl(context);

        boolean accepted = intent.getBooleanExtra(INVITATION_ACCEPTED, true);
        String invitationId = intent.getStringExtra(INVITATION_ID);

        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = context.getSystemService(NotificationManager.class);
        }
        if (accepted) {
            notificationManager.cancel(7);
            invitationDaoImpl.acceptInvitation(invitationId);
        } else {
            notificationManager.cancel(7);
            invitationDaoImpl.declineInvitation(invitationId);
        }
    }
}
