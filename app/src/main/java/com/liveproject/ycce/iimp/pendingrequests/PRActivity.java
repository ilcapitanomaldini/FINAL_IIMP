package com.liveproject.ycce.iimp.pendingrequests;


/*COPYRIGHT NOTICE

Copyright By "YCCE TEAM" on 15-03-2017.

Members of "YCCE TEAM" are stated in the postscript.

We, the creators of this software (i.e. developers) referenced as "YCCE TEAM" or "we" from here on,
allow the person who gets this software and/or code for the software to present this software/code
in a presentation dated 16-03-2017 only. Any further usage would be deemed to be breach of contract.
 Accepting this software/code is legally binding and would mean that the terms stated here have been
  accepted. The person does not have the right to copy/modify/distribute or in any form make the
  software or code available to anyone without the explicit permission of all the members of
   "YCCE TEAM". It is the responsibility of the aforementioned person that this software/code
   does not get illegally distributed till the time the person is in possession of the software/code.

P.S. :
Members of "YCCE TEAM" :
1. Aakash Wankhede
2. Akash Kahalkar
3. Mayur Dongare
4. Ved Mehta*/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.NullAdapter;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.networkservice.GetService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class PRActivity extends AppCompatActivity {
    private PRequestsRAdapter adapter;
    private ArrayList<PendingRequest> messagelist;
    private  final String URL = "https://ycc-developer-edition.ap2.force.com/member/services/apexrest/checkpendingrequest?";
    private PendingRequestReceiver pendingRequestReceiver;

    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Pending Request");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }
        rv = (RecyclerView) findViewById(R.id.rv_pr);
        llm = new LinearLayoutManager(this);

        //Trial to reverse the layout.
        //llm.setReverseLayout(true);
       // llm.setStackFromEnd(true);

        rv.setLayoutManager(llm);
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("No Pending Requests!");
        strings.add("Job Done!");
        rv.setAdapter(new NullAdapter(strings));

        //Populate Arraylist with current messages.
       // messagelist = new ArrayList<PendingRequest>();
        Intent intent = new Intent(this, GetService.class);
        intent.putExtra("URL",URL+"cid="+ DatabaseService.fetchID());
        intent.putExtra("NAME","PendingRequestReceiver");
        startService(intent);
        //messagelist = DBService.fetchPendingRequests();

        //Call adapter for recyclerview layout by passing the arraylist.
       // adapter = new PRequestsRAdapter(messagelist);
       // rv.setAdapter(adapter);
       // setRecyclerViewScrollListener();
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        if(messagelist.size()==0)
            getmessages(currentgid);
    }*/
    public void getmessages()
    {
        //sqlite function call code to fetch messages.
       // messagelist = DBService.fetchMessages(currentgid);
    }


    private int getLastVisible(){ return llm.findLastVisibleItemPosition(); }



    private void setRecyclerViewScrollListener() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = rv.getLayoutManager().getItemCount();
                if ( totalItemCount == getLastVisible() + 1) {
                    //Here, add the function that would fetch the new data from sqlite.
                    getmessages();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pendingRequestReceiver = new PendingRequestReceiver();
        this.registerReceiver(pendingRequestReceiver,new IntentFilter("android.intent.action.PendingRequestReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(pendingRequestReceiver);
    }

    public class PendingRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            messagelist = new ArrayList<PendingRequest>();
            String response = intent.getStringExtra("Result");
            try {
                JSONArray jsonArray = new JSONArray(response);
                int num_requests = jsonArray.length();
                for(int i=0;i<num_requests;i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String type = jsonObject.getString("Type__c");
                    if(type.equals("User"))
                    {
                        //Handle User
                        JSONObject innerJson = jsonObject.getJSONObject("RequesterID__r");
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setType("USER");
                        pendingRequest.setPrid(jsonObject.getString("Id"));
                        pendingRequest.setUid_poster(innerJson.getString("FirstName")+" "+innerJson.getString("LastName"));
                        pendingRequest.setUid_poster_id(innerJson.getString("Id"));
                        messagelist.add(pendingRequest);
                    }
                    else if(type.equals("Group")){
                        //Handle Group
                        JSONObject innerJson = jsonObject.getJSONObject("RequesterID__r");
                        PendingRequest pendingRequest = new PendingRequest();
                        pendingRequest.setType("GROUP");
                        pendingRequest.setPrid(jsonObject.getString("Id"));
                        pendingRequest.setUid_poster(innerJson.getString("FirstName")+" "+innerJson.getString("LastName"));
                        pendingRequest.setUid_poster_id(innerJson.getString("Id"));
                        JSONObject innerJsonGroup = jsonObject.getJSONObject("Group_Id__r");
                        pendingRequest.setAdditional_info(innerJsonGroup.getString("Id"));
                        messagelist.add(pendingRequest);
                    }
                }
            }catch (JSONException je){je.printStackTrace();}
            if(messagelist!=null) {
                adapter = new PRequestsRAdapter(messagelist);
                rv.setAdapter(adapter);
                setRecyclerViewScrollListener();
            }
            else {
                ArrayList<String> strings = new ArrayList<String>();
                strings.add("No Pending Requests!");
                strings.add("Job Done!");
                rv.setAdapter(new NullAdapter(strings));
            }
        }
    }
}
