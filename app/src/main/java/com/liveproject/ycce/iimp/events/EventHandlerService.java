package com.liveproject.ycce.iimp.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liveproject.ycce.iimp.DatabaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Laptop on 04-02-2017.
 */
public class EventHandlerService extends Service {
    ArrayList<Event> eventList;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        eventList = new ArrayList<Event>();
        Log.d("EventHandlerService", "onCreate: Created the service.");
        onSetChanged();
    }
   void onSetChanged(){
       //Code that adds pendingIntent for new events in the DB.
       //Fetch Events that have status as "new".
       ArrayList<Event> newList = DatabaseService.fetchEvents();
       //Log.d("EHS", "onSetChanged: "+ newList.get(0).getEventMessage());
       //eventList.addAll(newList);
       if(newList!=null) {
           int i = 0;
           for (Event event : newList) {
               //
               eventList.add(event);
               Calendar calendar = Calendar.getInstance();
               PendingIntent pendingIntent;
                //TODO : Change the status of the event in the DB.
               //Convert String to Calendar using a SimpleDateFormat
               try {


                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'hh:mm:ss\'Z\'", Locale.getDefault());
                   calendar.setTime(simpleDateFormat.parse(event.getDateTime()));
               }catch (ParseException e)
               {
                   Log.d("EventHandlerService", "onSetChanged: Exception when parsing the date.");
               }

               Intent myIntent = new Intent(EventHandlerService.this, AlarmService.class);

               myIntent.putExtra("Message",event.getEventMessage());
               myIntent.putExtra("Id",i+1);
               pendingIntent = PendingIntent.getService(EventHandlerService.this, i++, myIntent,0);

               AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                   alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
               }
               else {
                   alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
               }
               DatabaseService.updateEventStatus(event.getEID());
           }
       }
       stopSelf();
   }
}
