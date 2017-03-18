package com.liveproject.ycce.iimp.networkservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laptop on 18-03-2017.
 */

public class PostPersistentService extends Service {
    private String URL;
    private String result;
    private JSONObject json_object;
    private String receiver_name;
    private String contextString;
    final String TAG = "PostService" ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onHandleIntent: Service Started. " + receiver_name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        URL = intent.getStringExtra("URL");
        receiver_name = intent.getStringExtra("NAME");

        if(intent.getStringExtra("ExtraContext")!=null)
            contextString = intent.getStringExtra("ExtraContext");
        else
            contextString = null;

        try {
            json_object = new JSONObject(intent.getStringExtra("JSONOBJECT"));
        }catch (JSONException e){
            Log.d(TAG, "onHandleIntent: Exception in JSONObject");
        }


        //Adapt the following code according to the standards used in the main project.

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!receiver_name.equals("null")) {
                            Intent i = new Intent("android.intent.action." + receiver_name).putExtra("Result", response);
                            if(contextString!=null)
                                i.putExtra("ExtraContext",contextString);
                            Log.d(TAG, "onResponse: " + response + " for " + receiver_name);
                            sendBroadcast(i);
                            stopSelf();
                        }
                        else Log.d(TAG, "onResponse: Message received Response : "+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent("android.intent.action."+receiver_name).putExtra("Error", "true");
                        sendBroadcast(i);
                        Log.e(TAG, "onErrorResponse: ExceptionVolley " + error.toString(), error);
                        stopSelf();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json_object.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getRequestQueue().add(stringRequest);

        return super.onStartCommand(intent, flags, startId);
    }
}
