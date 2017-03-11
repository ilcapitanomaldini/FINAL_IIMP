package com.liveproject.ycce.iimp.events;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.liveproject.ycce.iimp.R;


/**
 * Created by Laptop on 29-01-2017.
 */
public class AlarmService extends Service {
    private String TAG = "In AlarmService";
    private String message;
    private int id;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        message = intent.getStringExtra("Message");
        id = intent.getIntExtra("Id",1);
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        message = intent.getStringExtra("Message");
        id = intent.getIntExtra("Id",1);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setContentTitle("Event")
                .setContentText(message);
        //.setVibrate(pattern)
        // .setOngoing(true);

        //mBuilder.setContentIntent(pi);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        Notification n=mBuilder.build();

        mNotificationManager.notify(id, n);




        // TO DO : Here, add the code to delete the event from the table now that it has been completed based on the id put in the intent.
        // That id is the location in the arraylist, I hope.


        stopSelf();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {

       // Toast.makeText(this,"Service Created!!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate: The Service has started.");
        //Intent intent = new Intent(this, MainActivity.class);
        // long[] pattern = {0, 300, 0};
        //PendingIntent pi = PendingIntent.getActivity(this, 1234, intent, 0);

        //Restore this if the onStartCommand does not work!!
        /*
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setContentTitle("Event")
                .setContentText(message);
                //.setVibrate(pattern)
               // .setOngoing(true);

        //mBuilder.setContentIntent(pi);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        Notification n=mBuilder.build();

        mNotificationManager.notify(id, n);




        // TO DO : Here, add the code to delete the event from the table now that it has been completed based on the id put in the intent.
        // That id is the location in the arraylist, I hope.


        stopSelf();*/
    }

}
