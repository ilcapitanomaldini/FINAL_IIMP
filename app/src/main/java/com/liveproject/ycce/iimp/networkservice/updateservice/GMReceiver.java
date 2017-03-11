package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;

import java.util.ArrayList;

/**
 * Created by Laptop on 03-03-2017.
 */
public class GMReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Message currentMessage;
        String result = intent.getStringExtra("Result");
        if(result!=null){
            if(intent.getStringExtra("ExtraContext")!=null) {
                if(intent.getStringExtra("ExtraContext").equals("Event")) {
                    currentMessage = new ArrayList<Message>(DatabaseService.fetchMessagesById("eventcurrent")).get(0);
                    DatabaseService.updateGMByID(currentMessage.getLocalID(),result.substring(1,result.length()-1));
                }
                else if(intent.getStringExtra("ExtraContext").equals("Poll")) {
                    currentMessage = new ArrayList<Message>(DatabaseService.fetchMessagesById("pollcurrent")).get(0);
                    DatabaseService.updateGMByID(currentMessage.getLocalID(),result.substring(1,result.length()-1));
                }
            }
            else {
                currentMessage = new ArrayList<Message>(DatabaseService.fetchMessagesById("current")).get(0);
                DatabaseService.updateGMByID(currentMessage.getLocalID(), result.substring(1, result.length() - 1));
            }
        }
    }
}
