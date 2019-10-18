package com.anu.dolist.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.anu.dolist.Constants;
import com.anu.dolist.MainActivity;
import com.anu.dolist.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Limin Deng(U6849956)
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        // error
        if (geofencingEvent.hasError()) {
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());
            String errorMessage = "Geofence Error";
            Log.e(Constants.TAG, errorMessage);
            return;
        }


        // make sure this receiver is called
        System.out.println("ninosnkonsongowg");





        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    context,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(context, intent, geofenceTransitionDetails);


//            Log.i(Constants.TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
//            Log.e(Constants.TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));


            Log.e(Constants.TAG, "Geofence Error");
        }

    }


    /**
     * get location information
     * @param context
     * @param geofenceTransition
     * @param triggeringGeofences
     * @return
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = String.valueOf(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + " : " + triggeringGeofencesIdsString;
    }


    /**
     * send location triggered notification
     * @param context
     * @param intent
     */
    public void sendNotification(Context context, Intent intent, String content) {
        // get content
        String channelId = String.valueOf(intent.getIntExtra(Constants.GEO_CHANNEL_ID , -1));
        String channelName = intent.getStringExtra(Constants.GEO_CHANNEL_NAME);


        /**
         * notification
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2: get channel
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);


            // 3: register this notification to manager
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            assert manager != null;
            assert channel != null;
            manager.createNotificationChannel(channel);

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(channelName)
                .setContentText("You have entered your event location")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have entered your event location"))
                .setAutoCancel(true) // which automatically removes the notification when the user taps it.
                // Set the intent that will fire when the user taps the notification
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);



        // 3: show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, builder.build());

    }


}
