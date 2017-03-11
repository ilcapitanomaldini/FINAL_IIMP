package com.liveproject.ycce.iimp.networkservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;

/**
 * Created by Laptop on 12-02-2017.
 */

    //Service class for volley Get request. Uses IntentService
    //Expects URL and NAME as String extras. The second is the name of the
    // broadcast receiver that was created for handling the result of this request.
public class GetService extends IntentService {

    private String URL;
    private String result;
    private String receiver_name;
    final String TAG = "GetService" ;

    public GetService() {
        super("GetService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        URL = intent.getStringExtra("URL");
        receiver_name = intent.getStringExtra("NAME");

        Log.d(TAG, "onHandleIntent: Service Started.");

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"onResponse: Yes Volley is working!!");
                        Intent i = new Intent("android.intent.action."+receiver_name).putExtra("Result", response);
                        sendBroadcast(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: Volley not working?!");
                        Intent i = new Intent("android.intent.action."+receiver_name).putExtra("Error", "true");
                        sendBroadcast(i);
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getRequestQueue().add(stringRequest);
    }
}
