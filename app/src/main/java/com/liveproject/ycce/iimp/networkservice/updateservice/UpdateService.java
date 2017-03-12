package com.liveproject.ycce.iimp.networkservice.updateservice;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.events.Event;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;
import com.liveproject.ycce.iimp.networkservice.PostService;
import com.liveproject.ycce.iimp.news.News;
import com.liveproject.ycce.iimp.polling.Poll;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 07-02-2017.
 */
public class UpdateService extends IntentService {
    private final String TAG = "UpdateService";
    private final String pmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/personalmessage";
    private final String gmURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupmessage";
    private final String eventsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/groupevent";
    private final String newsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/news";
    private final String pmDownURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/getmypersonalmessage";
    private final String pollURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/grouppoll";
    private final String myGroupsURL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/mygroups";
    protected static BroadcastReceiver br_pm;
    PersonalMessage currentPersonalMessage;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *Used to name the worker thread, important only for debugging.
     */
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Register the broadcastreceiver here
  //      br_pm = new PMReceiver();
       // this.registerReceiver(br_pm,new IntentFilter("android.intent.action.PMReceiver"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Log.d(TAG, "onDestroy: Service Completed.");
       // this.unregisterReceiver(this.br_pm);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //The volley code to refresh the local databases.
        //Insert the sqlite code in the onResponseListeners
       // Log.d(TAG, "onHandleIntent: Service Started.");
        Intent volleyIntent;
        ArrayList<PersonalMessage> personalMessages = DatabaseService.fetchNewPersonalMessages();
        ArrayList<Message> groupmessages = DatabaseService.fetchMessagesById("null"); //fetch the messages that have not been sent yet.
        ArrayList<News> news = DatabaseService.fetchNewsById("null");

        //Updates the personal messages on Salesforce.
        if(personalMessages!=null) {
            for(PersonalMessage personalMessage : personalMessages) {
                volleyIntent = new Intent(this, PostService.class);
                volleyIntent.putExtra("URL", pmURL);
                volleyIntent.putExtra("NAME","PMReceiver");

                final JSONObject params = new JSONObject();
                try {
                    params.put("fromcid", personalMessage.getSender());
                    params.put("subject", personalMessage.getSubject());
                    params.put("message", personalMessage.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyIntent.putExtra("JSONOBJECT",params.toString());
                currentPersonalMessage = personalMessage;
                DatabaseService.updatePMID(personalMessage.getLocalID(),"current");
                startService(volleyIntent);
            }
        }

        //Group messages are updated here.
        if(groupmessages!=null){
            for(Message message : groupmessages) {
                volleyIntent = new Intent(this, PostService.class);
                volleyIntent.putExtra("URL", gmURL);
                volleyIntent.putExtra("NAME","GMReceiver");

                DatabaseService.updateGMByID(message.getLocalID(),"current");

                //Determine the type and if not text, handle it properly.
                if("event".equals(message.getType())){
                    Event event = DatabaseService.fetchEvent(message.getEventID());
                    Intent newIntent;
                    newIntent = new Intent(this, PostService.class);
                    newIntent.putExtra("URL", eventsURL);
                    newIntent.putExtra("NAME","EventReceiver");
                    final JSONObject params = new JSONObject();
                    try {
                        params.put("description", event.getEventMessage());
                        params.put("edatetime", event.getDateTime());
                       // Calendar calendar = Calendar.getInstance();
                       // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'hh:mm:ss\'Z\'", Locale.getDefault());
                       // calendar.setTime(simpleDateFormat.parse(event.getDateTime()));
                       // params.put("yyyy",calendar.get(Calendar.YEAR));
                       // params.put("mm",calendar.get(Calendar.MONTH));
                       // params.put("dd",calendar.get(Calendar.DATE));
                       // params.put("h",calendar.get(Calendar.HOUR));
                       // params.put("m",calendar.get(Calendar.MINUTE));
                       // params.put("s",calendar.get(Calendar.SECOND));
                        params.put("duration", event.getDuration());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    newIntent.putExtra("JSONOBJECT",params.toString());
                    //Event does not require a local Id specifically because the EventId itself is a
                    //local Id that helps to associate with each groupmessage.
                    DatabaseService.updateEventByID(message.getEventID(),"current");
                    DatabaseService.updateGMByID(message.getLocalID(),"eventcurrent");
                    startService(newIntent);
                }

                if("text".equals(message.getType())) {
                    final JSONObject params = new JSONObject();
                    try {
                        params.put("gid", message.getGid());
                        params.put("fromcid", message.getSender());
                        params.put("message", message.getMessage());
                        params.put("type", message.getType());
                        params.put("eid", message.getEventID());
                        params.put("pid", message.getPollID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    volleyIntent.putExtra("JSONOBJECT", params.toString());
                    //update the local database so that the id is now current to identify
                    //the current row of message we are working on
                    //On failure, we revert to "null"
                    //On success, we set the ID returned from Salesforce.

                    startService(volleyIntent);
                }

                //Poll Message Type :
                if("poll".equals(message.getType())){
                    Poll poll = DatabaseService.fetchPollByLocalID(message.getPollID());
                    if(poll!=null) {
                        Intent newIntent;
                        newIntent = new Intent(this, PostService.class);
                        newIntent.putExtra("URL", pollURL);
                        newIntent.putExtra("NAME", "PollReceiver");
                        final JSONObject params = new JSONObject();
                        try {
                            params.put("duration", poll.getDuration());
                            params.put("number_answers", Integer.toString(poll.getNumber_answers()));
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        newIntent.putExtra("JSONOBJECT", params.toString());

                        DatabaseService.updatePollMappingID(message.getPollID(), "current");
                        DatabaseService.updatePollByID(message.getPollID(), "current");
                        //DatabaseService.updatePollMappingID(message.getPollID(),"current");
                        DatabaseService.updateGMByID(message.getLocalID(), "pollcurrent");
                        startService(newIntent);
                    }

                }
            }
        }

        //News Updates carried out here.
        if(news!=null) {
            for(News newNews : news) {
                volleyIntent = new Intent(this, PostService.class);
                volleyIntent.putExtra("URL", newsURL);
                volleyIntent.putExtra("NAME","NewsReceiver");

                final JSONObject params = new JSONObject();
                try {
                    params.put("title", newNews.getTitle());
                    params.put("postedbycid", newNews.getPoster());
                    params.put("body", newNews.getMessage());
                    params.put("imagepath", newNews.getImage_loc());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyIntent.putExtra("JSONOBJECT",params.toString());

                DatabaseService.updateNewsByID(newNews.getLocalID(),"current");
                startService(volleyIntent);
            }
        }

    }


}
