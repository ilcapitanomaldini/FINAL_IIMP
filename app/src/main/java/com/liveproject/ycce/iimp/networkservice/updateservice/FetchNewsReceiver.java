package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.news.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laptop on 18-03-2017.
 */

public class FetchNewsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        News news;
        String result = intent.getStringExtra("Result");
        if(result!=null){
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    news = new News();
                    news.setTitle(jsonObject.getString("Title__c"));
                    news.setNid(jsonObject.getString("Id"));
                    news.setDatePosted(jsonObject.getString("Date_Time__c"));
                    news.setMessage(jsonObject.getString("Body__c"));
                    news.setPoster(jsonObject.getString("Posted_By__c"));
                    if(DatabaseService.fetchNewsById(jsonObject.getString("Id"))==null){
                        DatabaseService.insertNewNews(news);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
