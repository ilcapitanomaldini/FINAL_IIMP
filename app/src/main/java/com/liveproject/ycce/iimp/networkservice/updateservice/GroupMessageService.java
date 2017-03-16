package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.events.Event;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Activity_Messaging;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;
import com.liveproject.ycce.iimp.networkservice.PostService;
import com.liveproject.ycce.iimp.polling.Poll;
import com.liveproject.ycce.iimp.polling.PollMapping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 16-03-2017.
 */

//INPUT : gid as String.
//For fetching groupmessages from the cloud.

public class GroupMessageService extends Service {
    final String TAG = "GroupMessageService";
    private String currentgid;
    final private String getPollAM = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest//getpollresult";
    final private String groupmessagesURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/getmygroupmessage";
    BroadcastReceiver receiver,pamreceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new GroupMessageReceiver();
        Log.d(TAG, "onCreate: Created the service.");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.GroupMessageReceiver");
        registerReceiver(receiver, filter);
        IntentFilter pamfilter = new IntentFilter();
        pamfilter.addAction("android.intent.action.PAMReceiver");
        registerReceiver(pamreceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentgid = intent.getStringExtra("gid");
        Intent volleyIntent_groups;
        volleyIntent_groups = new Intent(this, PostService.class);
        volleyIntent_groups.putExtra("URL", groupmessagesURL);
        volleyIntent_groups.putExtra("NAME","GroupMessageReceiver");

        final JSONObject params_groups = new JSONObject();
        try {
            params_groups.put("gid", currentgid);
            params_groups.put("lastdate", DatabaseService.fetchLastLogin());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyIntent_groups.putExtra("JSONOBJECT",params_groups.toString());
        startService(volleyIntent_groups);
        return super.onStartCommand(intent, flags, startId);
    }

    public class GroupMessageReceiver extends BroadcastReceiver{
        String type;

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result");
            try {
                JSONArray outerJsonArray = new JSONArray(result);
                for(int counter = 0; counter<outerJsonArray.length(); counter++){
                    JSONObject outerJsonObject = outerJsonArray.getJSONObject(counter);
                    Message message = new Message();
                    message.setSender(outerJsonObject.getString("From__c"));
                    message.setMessage(outerJsonObject.getString("Message__c"));
                    message.setMid(outerJsonObject.getString("Id"));
                    message.setGid(outerJsonObject.getString("Group_Id__c"));
                    message.setType(outerJsonObject.getString("Type__c"));
                    type = outerJsonObject.getString("Type__c");
                    if(type.equals("event")){
                        //For type event.
                        JSONObject eventJsonObject = outerJsonObject.getJSONObject("Event_Details_Id__r");
                        Event event = new Event();
                        event.setEID(eventJsonObject.getString("Id"));
                        String dateTime = eventJsonObject.getString("Event_Date_Time__c");
                        event.setDateTime(dateTime.substring(0,dateTime.indexOf('.'))+"Z");
                        event.setStatus("new");
                        //TODO : Duration
                        event.setDuration("null");
                        event.setEventMessage(outerJsonObject.getString("Message__c"));
                        event.setGID(outerJsonObject.getString("Group_Id__c"));
                        event.setPostedBy(outerJsonObject.getString("From__c"));
                        DatabaseService.insertnewEvent(event);
                        message.setEventID(eventJsonObject.getString("Id"));
                        message.setPollID("null");
                    }
                    else if (type.equals("poll")){
                        continue;/*
                        JSONObject pollJsonObject = outerJsonObject.getJSONObject("Poll_Id__r");
                        Poll poll = new Poll();
                        poll.setPid(pollJsonObject.getString("Id"));
                        //TODO : Duration
                        poll.setDuration("null");
                        poll.setNumber_answers(pollJsonObject.getInt("Number_Answers__c"));
                        //Remember that PM is set to null.
                        poll.setPm(null);
                        poll.setCreatorId(outerJsonObject.getString("From__c"));
                        poll.setTitle(outerJsonObject.getString("Message__c"));
                        DatabaseService.insertPoll(poll);
                        message.setEventID("null");
                        message.setPollID(pollJsonObject.getString("Id"));
                        //TODO : Call the answermapping from Cloud.
                        Intent pamIntent = new Intent(context,PostService.class);
                        pamIntent.putExtra("URL", getPollAM);
                        pamIntent.putExtra("NAME","PAMReceiver");

                        final JSONObject params_pam = new JSONObject();
                        try {
                            params_pam.put("pid", pollJsonObject.getString("Id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pamIntent.putExtra("JSONOBJECT",params_pam.toString());
                        startService(pamIntent);*/
                    }
                    DatabaseService.insertMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!"poll".equals(type)) {
                sendBroadcast(new Intent(context, Activity_Messaging.MessageReceiver.class));
                stopSelf();
            }
        }
    }

    public class PAMReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result");
            if(result!=null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<PollMapping> pollMappings = new ArrayList<>();
                    Poll poll = DatabaseService.fetchPoll(jsonArray.getJSONObject(0).getString("Poll_Id__c"));
                    if(poll!=null) {
                        DatabaseService.deletePollByID(jsonArray.getJSONObject(0).getString("Poll_Id__c"));
                        for (int counter = 0; counter < jsonArray.length(); counter++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(counter);
                            PollMapping pollMapping = new PollMapping();
                            pollMapping.setPid(jsonObject.getString("Poll_Id__c"));
                            pollMapping.setAid(jsonObject.getString("Id"));
                            pollMapping.setAnswerTitle(jsonObject.getString("Answer_Title__c"));
                            pollMapping.setNumberOfVotes(jsonObject.getString("Votes__c"));
                            pollMappings.add(pollMapping);
                        }
                        poll.setPm(pollMappings);
                        DatabaseService.insertPoll(poll);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sendBroadcast(new Intent(context, Activity_Messaging.MessageReceiver.class));
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
