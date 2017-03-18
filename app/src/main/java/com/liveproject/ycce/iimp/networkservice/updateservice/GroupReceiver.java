package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.groupmessaging.GroupClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 07-03-2017.
 */
public class GroupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra("Result");
        ArrayList<GroupClass> groupClasses = new ArrayList<>();
        int num_messages;
        try {
            JSONArray jsonArray = new JSONArray(result);
            if(jsonArray!=null&&jsonArray.length()>0) {
                num_messages = jsonArray.length();
                for (int i = 0; i < num_messages; i++) {
                    JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);
                    GroupClass groupClass = new GroupClass();
                    groupClass.setGName(jsonObjectOuter.getString("Name"));
                    groupClass.setGType(jsonObjectOuter.getString("Group_Type__c"));
                    groupClass.setGid(jsonObjectOuter.getString("Id"));
                    //JSONObject jsonInner = jsonObjectOuter.getJSONObject("Created_by_Contact__r");
                    groupClass.setGRole(jsonObjectOuter.getString("Role__c"));
                    groupClass.setStatus("active");
                    groupClasses.add(groupClass);
                }
                DatabaseService.insertGroups(groupClasses);
            }
        }catch (JSONException je){je.printStackTrace();}

    }
}
