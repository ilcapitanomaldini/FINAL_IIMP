package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Created by Laptop on 29-01-2017.
 */
public class NetworkReceiver extends BroadcastReceiver {
    private final String TAG = "NetworkReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: Received successfully");
       //Call the Service for the volley functions here.
        //First, lets check whether the network has truly been connected or not.
        if (isOnline(context))
        {
            //TODO : Add UpdateService as well.
            Intent intent1 = new Intent(context,DowndateService.class);
            context.startService(intent1);
        }
    }



    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}
