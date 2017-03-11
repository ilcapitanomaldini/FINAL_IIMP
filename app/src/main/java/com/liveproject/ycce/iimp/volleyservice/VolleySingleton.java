package com.liveproject.ycce.iimp.volleyservice;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Tiger on 01-09-2016.
 */
public class VolleySingleton {
    private static VolleySingleton ourInstance = new VolleySingleton();
    private static RequestQueue requestQueue;

    public static VolleySingleton getInstance() {
        return ourInstance;
    }

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
