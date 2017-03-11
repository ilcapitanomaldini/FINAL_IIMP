package com.liveproject.ycce.iimp.networkservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liveproject.ycce.iimp.volleyservice.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laptop on 12-02-2017.
 */

    //Service class for volley Get request. Uses IntentService
    //Expects URL, NAME and JSONOBJECT as String extras. The second is the name of the
    // broadcast receiver that was created for handling the result of this request.
public class PostService extends IntentService {

    private String URL;
    private String result;
    private JSONObject json_object;
    private String receiver_name;
    private String contextString;
    final String TAG = "PostService" ;

    public PostService() {
        super("PostService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
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
        Log.d(TAG, "onHandleIntent: Service Started.");

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
    }
}
