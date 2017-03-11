package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.news.News;

import java.util.ArrayList;

/**
 * Created by Laptop on 03-03-2017.
 */
public class NewsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        News news;
        String result = intent.getStringExtra("Result");
        if(result!=null){
            news = new ArrayList<News>(DatabaseService.fetchNewsById("current")).get(0);
            DatabaseService.updateNewsByID(news.getLocalID(),result.substring(1,result.length()-1));
        }
    }
}
