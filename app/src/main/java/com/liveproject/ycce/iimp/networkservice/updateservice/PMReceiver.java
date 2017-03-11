package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;
import com.liveproject.ycce.iimp.networkservice.PostService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laptop on 01-03-2017.
 */
public class PMReceiver extends BroadcastReceiver {
    private final String pmReceiverURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/personalmessagereceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PersonalMessage currentPersonalMessage;
        String result = intent.getStringExtra("Result");
        if(result!=null){
            currentPersonalMessage= DatabaseService.getPMbyID("current");
            try {
                 DatabaseService.updatePMID(currentPersonalMessage.getLocalID(), result.substring(1, result.length() - 1));
            }catch (NullPointerException npe){npe.printStackTrace();}
            // currentPersonalMessage=DBService.getPMbyID(result.substring(1,result.length()-1));
            Intent volleyIntent = new Intent(context, PostService.class);
            volleyIntent.putExtra("URL",pmReceiverURL);
            volleyIntent.putExtra("NAME","null");

            final JSONArray receivers = new JSONArray();
            try {
                for (String s : currentPersonalMessage.getReceivers()) {
                    receivers.put(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            final JSONObject params = new JSONObject();
            try {
                params.put("pmid", result.substring(1,result.length()-1));
                params.put("tocontactids",receivers);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            volleyIntent.putExtra("JSONOBJECT",params.toString());

            context.startService(volleyIntent);
        }
       // context.unregisterReceiver(UpdateService.br_pm);
    }
}