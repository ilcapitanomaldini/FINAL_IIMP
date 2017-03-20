package com.liveproject.ycce.iimp.messaging.groupmessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.NullAdapter;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.events.Activity_CreateEvent;
import com.liveproject.ycce.iimp.networkservice.updateservice.GroupMessageService;
import com.liveproject.ycce.iimp.networkservice.updateservice.UpdateService;
import com.liveproject.ycce.iimp.polling.Activity_CreatePoll;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class Activity_Messaging extends AppCompatActivity {
    // private MessageRAdapter adapter;
    private Adapter_Group_Message adapter;
    private ArrayList<Message> messagelist;
    String currentgid = "101";
    String groupname;
    String grouprole;
    BroadcastReceiver receiver;

    private Toolbar toolbar;
    private FloatingActionButton fab_events, fab_poll, fab_send;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private EditText sender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        currentgid = getIntent().getStringExtra("gid");
        groupname = getIntent().getStringExtra("gname");
        grouprole = getIntent().getStringExtra("grole");

        try {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText(groupname);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity_Group_Details.class);
                intent.putExtra("GID", currentgid);
                intent.putExtra("GNAME", groupname);
                startActivity(intent);
            }
        });

        fab_send = (FloatingActionButton) findViewById(R.id.fab_send);
        sender = (EditText) findViewById(R.id.et_message);
        fab_events = (FloatingActionButton) findViewById(R.id.fab_event);
        fab_poll = (FloatingActionButton) findViewById(R.id.fab_poll);

        if (!grouprole.equals(Constants.GROUPROLES[2])) {

            fab_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Activity_CreateEvent.class);
                    intent.putExtra("gid", currentgid);
                    startActivityForResult(intent, 1);
                }
            });

            fab_poll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), Activity_CreatePoll.class);
                    intent.putExtra("gid", currentgid);
                    startActivityForResult(intent, 2);
                }
            });

            if (sender.getText().toString() != null) {
                fab_send.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //TO DO :: Add a send_message_to_cloud() function here. SQLite for demonstration purposes only.
                        Message message = new Message();
                        message.setType("text");
                        message.setSender(DatabaseService.fetchID());
                        message.setMessage(sender.getText().toString());
                        message.setGid(currentgid);
                        message.setEventID("null");
                        message.setPollID("null");
                        message.setMid("null");
                        DatabaseService.insertMessage(message);
                    /*if (Validation.isOnline(v.getContext()))
                    {
                        Intent intent1 = new Intent(v.getContext(),UpdateService.class);
                        v.getContext().startService(intent1);
                    }*/
                        messagelist = new ArrayList<Message>();
                        messagelist = DatabaseService.fetchMessages(currentgid);

                        //TODO : Add an actual notifyItemInserted.
                        if (messagelist != null) {
                            adapter = new Adapter_Group_Message(messagelist);
                            rv.setAdapter(adapter);
                        }
                    }
                });
            }
        } else {
            fab_events.setVisibility(View.GONE);
            fab_poll.setVisibility(View.GONE);
            findViewById(R.id.ll_text_sender).setVisibility(View.GONE);
        }


        rv = (RecyclerView) findViewById(R.id.rv_messaging);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        //Trial to reverse the layout.
        //llm.setReverseLayout(true);
        // llm.setStackFromEnd(true);

        rv.setLayoutManager(llm);

        //Populate Arraylist with current messages.
        messagelist = new ArrayList<Message>();
        messagelist = DatabaseService.fetchMessages(currentgid);

        //Call adapter for recyclerview layout by passing the arraylist.
        // adapter = new MessageRAdapter(messagelist);
        if (messagelist != null) {
            adapter = new Adapter_Group_Message(messagelist);
            rv.setAdapter(adapter);
        } else {
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("No Messages!");
            rv.setAdapter(new NullAdapter(strings));
        }
        Intent intent = new Intent(this, GroupMessageService.class);
        intent.putExtra("gid", currentgid);
        intent.putExtra("receiver", "MessageReceiver");
        startService(intent);
        setRecyclerViewScrollListener();

    }

    /*  @Override
      protected void onStart() {
          super.onStart();
          if(messagelist.size()==0)
              getmessages(currentgid);
      }*/
    public void getmessages(String currentgid) {
        //sqlite code
        messagelist = DatabaseService.fetchMessages(currentgid);
    }


    private int getLastVisible() {
        return llm.findLastVisibleItemPosition();
    }


    private void setRecyclerViewScrollListener() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = rv.getLayoutManager().getItemCount();
                if (totalItemCount == getLastVisible() + 1) {
                    //Here, add the function that would fetch the new data from sqlite.
                    getmessages(currentgid);
                }
            }
        });
    }

    /*  @Override
      protected void onResume() {
          super.onResume();
          messagelist = new ArrayList<Message>();
          messagelist = DatabaseService.fetchMessages(currentgid);
          if(messagelist!=null) {
              adapter = new Adapter_Group_Message(messagelist);
              rv.setAdapter(adapter);
          }
      }
  */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            messagelist = new ArrayList<Message>();
            messagelist = DatabaseService.fetchMessages(currentgid);
            if (messagelist != null) {
                adapter = new Adapter_Group_Message(messagelist);
                rv.setAdapter(adapter);
            }
            /*if (Validation.isOnline(this))
            {
                Intent intent1 = new Intent(this,UpdateService.class);
                this.startService(intent1);
            }*/
        }
        if (requestCode == 2) {
            messagelist = new ArrayList<Message>();
            messagelist = DatabaseService.fetchMessages(currentgid);
            if (messagelist != null) {
                adapter = new Adapter_Group_Message(messagelist);
                rv.setAdapter(adapter);
            }
            if (Validation.isOnline(this)) {
                Intent intent1 = new Intent(this, UpdateService.class);
                this.startService(intent1);
            }
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            messagelist = new ArrayList<Message>();
            messagelist = DatabaseService.fetchMessages(currentgid);

            //Call adapter for recyclerview layout by passing the arraylist.
            // adapter = new MessageRAdapter(messagelist);
            if (messagelist != null) {
                adapter = new Adapter_Group_Message(messagelist);
                rv.setAdapter(adapter);
            } else {
                ArrayList<String> strings = new ArrayList<String>();
                strings.add("No Messages!");
                rv.setAdapter(new NullAdapter(strings));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MessageReceiver");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Validation.isOnline(this)) {
            Intent intent1 = new Intent(this, UpdateService.class);
            this.startService(intent1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
