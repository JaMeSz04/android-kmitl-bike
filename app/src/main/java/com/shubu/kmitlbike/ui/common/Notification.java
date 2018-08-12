package com.shubu.kmitlbike.ui.common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
//import android.support.v7.app.NotificationCompat;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.splash.SplashActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notification {

    public static void showNotification(String message, Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        android.app.Notification notification =
                new NotificationCompat.Builder(context) // this is context
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("KMITL Bike")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
    }
}
