package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;
import com.liveproject.ycce.iimp.networkservice.PostPersistentService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laptop on 12-03-2017.
 */

public class DowndateService extends IntentService {
    public DowndateService() {
        super("DowndateService");
    }

    private final String TAG = "DowndateService";
    private final String pmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/personalmessage";
    private final String gmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupmessage";
    private final String eventsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupevent";
    private final String newsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/news";
    private final String pmDownURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/getmypersonalmessage";
    private final String pollURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/grouppoll";
    private final String myGroupsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/mygroups";
    private final String myNewsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/getnews";
    PersonalMessage currentPersonalMessage;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent volleyIntent;
        //For downloading personal messages :
        volleyIntent = new Intent(this, PostPersistentService.class);
        volleyIntent.putExtra("URL", pmDownURL);
        volleyIntent.putExtra("NAME","PMDownReceiver");
        Log.d(TAG, "onHandleIntent: PMDOWN");

        final JSONObject params = new JSONObject();
        try {
            params.put("cid", DatabaseService.fetchID());
            String date = DatabaseService.fetchLastLogin();
            date = date.substring(0,date.length()-1);
            date = date + ".000Z";
            //Log.d(TAG, "onHandleIntent: "+DatabaseService.fetchID());
            //Log.d(TAG, "onHandleIntent: "+ date);
            params.put("lastdate", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyIntent.putExtra("JSONOBJECT",params.toString());
        startService(volleyIntent);

        SystemClock.sleep(10000);

        //For fetching mygroups from salesforce.
        Intent volleyIntent_groups;
        volleyIntent_groups = new Intent(this, PostPersistentService.class);
        volleyIntent_groups.putExtra("URL", myGroupsURL);
        volleyIntent_groups.putExtra("NAME","GroupReceiver");

        final JSONObject params_groups = new JSONObject();
        try {
            params_groups.put("cid", DatabaseService.fetchID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyIntent_groups.putExtra("JSONOBJECT",params_groups.toString());
        startService(volleyIntent_groups);

        SystemClock.sleep(10000);

        //For fetching news from cloud.
        Intent newsIntent;
        newsIntent = new Intent(this,PostPersistentService.class);
        newsIntent.putExtra("URL", myNewsURL);
        newsIntent.putExtra("NAME","FetchNewsReceiver");

        final JSONObject params_news = new JSONObject();
        try {
            params_news.put("lastdate", DatabaseService.fetchLastLogin());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newsIntent.putExtra("JSONOBJECT",params_news.toString());
        startService(newsIntent);

    }
}
