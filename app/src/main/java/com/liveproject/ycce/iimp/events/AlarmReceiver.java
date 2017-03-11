package com.liveproject.ycce.iimp.events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;


/**
 * Created by Laptop on 29-01-2017.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        generateNotification(context);
    }
    public static void generateNotification(Context context){

        Intent intent = new Intent(context, Activity_Home_Messaging.class);
       // long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(context, 1234, intent, 0);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.send)
                .setContentTitle("Event Generated.")
                .setContentText("GO ON NOW!!")
                //.setVibrate(pattern)
                .setOngoing(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       Notification n=mBuilder.build();

        mNotificationManager.notify(1234, n);
    }
}
