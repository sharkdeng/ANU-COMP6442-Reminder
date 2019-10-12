package com.anu.dolist;

import android.app.Notification ;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.os.Build;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import static com.anu.dolist.EditorActivity.NOTIFICATION_CHANNEL_ID;

/**
 * @author: Supriya Kamble
 */
public class MyNotification extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    public void onReceive (Context context , Intent intent) {

        Log.d("Shark", "Notification received");

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
//        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
//
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES.O ) {
////            int importance = NotificationManager.IMPORTANCE_HIGH ;
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance);
//
////            assert notificationManager != null;
//           assert notificationManager != null;
//           notificationManager.createNotificationChannel(notificationChannel) ;
//        }
//        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
//        assert notificationManager != null;
//        notificationManager.notify(id , notification) ;






        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            CharSequence name = context.getString(R.string.channel_name);
//            String description = context.getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification name", importance);
            channel.setDescription("description for notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }







}