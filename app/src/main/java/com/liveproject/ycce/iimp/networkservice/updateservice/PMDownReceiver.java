package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 05-03-2017.
 */
public class PMDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra("Result");
        ArrayList<PersonalMessage> personalMessages = new ArrayList<>();
        int num_messages;
        try {
            JSONArray jsonArray = new JSONArray(result);
            num_messages = jsonArray.length();
            for(int i=0;i<num_messages;i++){
                JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);
                JSONObject jsonObject = jsonObjectOuter.getJSONObject("Personal_Message_Id__r");
                PersonalMessage personalMessage = new PersonalMessage();
                personalMessage.setPmid(jsonObject.getString("Id"));
                personalMessage.setSubject(jsonObject.getString("Name"));
                personalMessage.setMessage(jsonObject.getString("Message__c"));
                JSONObject sender = jsonObject.getJSONObject("From_Contact_Id__r");
                personalMessage.setSender(sender.getString("FirstName")+" "+sender.getString("LastName"));
                String dateTime = jsonObject.getString("Date_Time__c");
                personalMessage.setDate(dateTime.substring(0,dateTime.indexOf('.'))+"Z");
                personalMessages.add(personalMessage);
            }
            DatabaseService.insertPersonalMessages(personalMessages);
        }catch (JSONException je){je.printStackTrace();}
    }
}
