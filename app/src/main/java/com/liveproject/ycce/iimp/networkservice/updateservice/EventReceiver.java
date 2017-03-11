package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.events.Event;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;
import com.liveproject.ycce.iimp.networkservice.PostService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 03-03-2017.
 */
public class EventReceiver extends BroadcastReceiver {
    private final String gmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupmessage";
    @Override
    public void onReceive(Context context, Intent intent) {
        Event currentEvent;
        String result = intent.getStringExtra("Result");
        if (result != null) {
            currentEvent = DatabaseService.fetchEvent("current");
            DatabaseService.updateEventByID("current", result.substring(1, result.length() - 1));
            // Update eventID in message
            DatabaseService.updateEventIdinMessage(result.substring(1, result.length() - 1),"eventcurrent");

            Intent volleyIntent;
            Message message = new ArrayList<Message>( DatabaseService.fetchMessagesById("eventcurrent")).get(0);
            volleyIntent = new Intent(context, PostService.class);
            volleyIntent.putExtra("URL", gmURL);
            volleyIntent.putExtra("NAME","GMReceiver");
            final JSONObject params = new JSONObject();
            try {
                params.put("gid", message.getGid());
                params.put("fromcid", message.getSender());
                params.put("message", message.getMessage());
                params.put("type", message.getType());
                params.put("eid", message.getEventID());
                params.put("pid", message.getPollID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            volleyIntent.putExtra("JSONOBJECT", params.toString());
            //update the local database so that the id is now current to identify
            //the current row of message we are working on
            //On failure, we revert to "null"
            //On success, we set the ID returned from Salesforce.
            volleyIntent.putExtra("ExtraContext","Event");
            context.startService(volleyIntent);

        }
    }
}
