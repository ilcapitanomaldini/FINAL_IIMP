package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;
import com.liveproject.ycce.iimp.networkservice.PostService;
import com.liveproject.ycce.iimp.polling.Poll;
import com.liveproject.ycce.iimp.polling.PollMapping;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 06-03-2017.
 */
public class PollReceiver extends BroadcastReceiver {
    private final String pollMappingURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/pollanswermapping";
    private final String gmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupmessage";

    @Override
    public void onReceive(Context context, Intent intent) {
        Poll currentPoll;
        String result = intent.getStringExtra("Result");
        if (result != null) {
            currentPoll = DatabaseService.fetchPoll("current");
            currentPoll.setPid(result.substring(1, result.length() - 1));
            DatabaseService.updatePollByID("current", result.substring(1, result.length() - 1));
            DatabaseService.updatePollMappingID("current", result.substring(1, result.length() - 1));
            // Update pollID in message
            DatabaseService.updatePollIdinMessage(result.substring(1, result.length() - 1), "pollcurrent");

            // TODO : ADD the UPDATE PID in POLLMAPPING Table HERE.

            Intent volleyIntent, intentGM;
            try {
                ArrayList<PollMapping> pollMappings = currentPoll.getPm();
                volleyIntent = new Intent(context, PostService.class);
                volleyIntent.putExtra("URL", pollMappingURL);
                volleyIntent.putExtra("NAME", "null");
                for (PollMapping pollMapping :
                        pollMappings) {
                    final JSONObject params = new JSONObject();
                    try {
                        Log.d("PollAnswerLoop", "onReceive: " + pollMapping.getAnswerTitle());
                        params.put("answertitle", pollMapping.getAnswerTitle());
                        params.put("pid", currentPoll.getPid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    volleyIntent.putExtra("JSONOBJECT", params.toString());
                    context.startService(volleyIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message message = new ArrayList<Message>(DatabaseService.fetchMessagesById("pollcurrent")).get(0);
            intentGM = new Intent(context, PostService.class);
            intentGM.putExtra("URL", gmURL);
            intentGM.putExtra("NAME", "GMReceiver");
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
            intentGM.putExtra("JSONOBJECT", params.toString());
            intentGM.putExtra("ExtraContext", "Poll");
            context.startService(intentGM);


        }
    }
}
