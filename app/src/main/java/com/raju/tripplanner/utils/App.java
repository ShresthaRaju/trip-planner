package com.raju.tripplanner.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {

    public static final String CHANNEL_ID = "INVITATION CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel invitationChannel = new NotificationChannel(CHANNEL_ID, "New Invitation", NotificationManager.IMPORTANCE_MAX);
            invitationChannel.setDescription("Notification Channel that shows notification on receiving new invitation for a trip");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(invitationChannel);
        }
    }
}
