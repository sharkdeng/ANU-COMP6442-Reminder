package com.anu.dolist.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.anu.dolist.Constants;
import com.anu.dolist.MainActivity;
import com.anu.dolist.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

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


        System.out.println("ninosnkonsongowg");


        /**
         * notification
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            // 2: get channel
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    channelId+"_name",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription(channelId+"_desc");
//            channel.setShowBadge(true);
//
//
//            // 3: register this notification to manager
//            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//
//
//        // 1: Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Title")
//                .setContentText("Content")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Content"))
//                .setAutoCancel(true) // which automatically removes the notification when the user taps it.
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//
//
//
//        // 3: show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1001, builder.build());












        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(
//                    this,
//                    geofenceTransition,
//                    triggeringGeofences
//            );

            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails);


//            Log.i(Constants.TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
//            Log.e(Constants.TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));


            Log.e(Constants.TAG, "Geofence Error");
        }
    
    

    }
}
