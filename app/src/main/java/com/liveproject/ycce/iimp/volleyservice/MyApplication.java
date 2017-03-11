package com.liveproject.ycce.iimp.volleyservice;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tiger on 01-09-2016.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
