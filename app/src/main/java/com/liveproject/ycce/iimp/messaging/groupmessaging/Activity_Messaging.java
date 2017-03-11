package com.liveproject.ycce.iimp.messaging.groupmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.events.Activity_CreateEvent;
import com.liveproject.ycce.iimp.polling.Activity_CreatePoll;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class Activity_Messaging extends AppCompatActivity {
    // private MessageRAdapter adapter;
    private Adapter_Group_Message adapter;
    private ArrayList<Message> messagelist;
    String currentgid="101";

    private FloatingActionButton fab_events,fab_poll;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ImageButton send; private EditText sender;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        currentgid = getIntent().getStringExtra("gid");

        send = (ImageButton) findViewById(R.id.bt_send);
        sender = (EditText) findViewById(R.id.et_message);
        fab_events = (FloatingActionButton) findViewById(R.id.fab_event);
        fab_poll = (FloatingActionButton) findViewById(R.id.fab_poll);


        fab_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity_CreateEvent.class);
                intent.putExtra("gid",currentgid);
                startActivity(intent);
            }
        });

        fab_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Activity_CreatePoll.class);
                intent.putExtra("gid",currentgid);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {

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
            }
        });


        rv = (RecyclerView) findViewById(R.id.rv_messaging);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true);

        //Trial to reverse the layout.
        //llm.setReverseLayout(true);
        // llm.setStackFromEnd(true);

        rv.setLayoutManager(llm);

        //Populate Arraylist with current messages.
        messagelist = new ArrayList<Message>();
        messagelist = DatabaseService.fetchMessages(currentgid);

        //Call adapter for recyclerview layout by passing the arraylist.
        // adapter = new MessageRAdapter(messagelist);
        adapter = new Adapter_Group_Message(messagelist);
        rv.setAdapter(adapter);
        setRecyclerViewScrollListener();
    }

    /*  @Override
      protected void onStart() {
          super.onStart();
          if(messagelist.size()==0)
              getmessages(currentgid);
      }*/
    public void getmessages(String currentgid)
    {
        //sqlite code
        messagelist = DatabaseService.fetchMessages(currentgid);
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
                    getmessages(currentgid);
                }
            }
        });
    }

}
