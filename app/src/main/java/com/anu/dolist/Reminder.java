//package com.anu.dolist;
//
//import android.app.Application;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.provider.Settings;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//    /**
//     * register notification channel on the global context
//     *
//     * @author: Limin Deng
//     */
//    public class Reminder extends Application {
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//
//        }
//
//
////    public static void createNotificationChannel(Context context, String channelId) {
////
////        Log.d("Shark", "Notification received");
////
////
////        // 1: check version
////        // Create the NotificationChannel, but only on API 26+ because
////        // the NotificationChannel class is new and not in the support library
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////
////            // 2: get channel
////            NotificationChannel channel = new NotificationChannel(channelId,
////                    channelId+"_name",
////                    NotificationManager.IMPORTANCE_DEFAULT);
////            channel.setDescription(channelId+"_desc");
////            channel.setShowBadge(true);
////
////
////            // 3: register this notification to manager
////            NotificationManager manager = context.getSystemService(NotificationManager.class);
////            manager.createNotificationChannel(channel);
////        }
////    }
////
////
////    public static void createNotification(Context context, String channelId, String title, String content) {
////
////
////        Log.d("Shark", "createNotificaton");
////
////        // 1: Create an explicit intent for an Activity in your app
////        Intent intent = new Intent(context, EditorActivity.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
////
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
////                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
////                .setContentTitle(title)
////                .setContentText(content)
////                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                .setStyle(new NotificationCompat.BigTextStyle()
////                        .bigText(content))
////                .setAutoCancel(true) // which automatically removes the notification when the user taps it.
////                // Set the intent that will fire when the user taps the notification
////                .setContentIntent(pendingIntent)
////                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
////
////
////
////        // 3: show the notification
////        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
////        // notificationId is a unique int for each notification that you must define
////        notificationManager.notify(1001, builder.build());
////
////    }
////
//}
