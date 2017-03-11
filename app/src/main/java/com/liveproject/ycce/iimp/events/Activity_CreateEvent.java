package com.liveproject.ycce.iimp.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.LocalIdGen;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Message;


/**
 * Created by Laptop on 04-03-2017.
 */
public class Activity_CreateEvent extends AppCompatActivity {
    private EditText duration,dateTime,message;
    private Button create;
    String gid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        if(getIntent().getStringExtra("gid")!=null){
            gid = getIntent().getStringExtra("gid");
        }

        duration = (EditText) findViewById(R.id.et_cevent_duration);
        dateTime = (EditText) findViewById(R.id.et_cevent_date_time);
        message = (EditText) findViewById(R.id.et_cevent_message);
        create = (Button) findViewById(R.id.bt_cevent_create);

        //Extract Current GID from the intent.

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO DO : Validation
                Event event = new Event();
                String s_id = new LocalIdGen().nextLocalId();
                event.setDuration(duration.getText().toString());
                event.setEID(s_id);
                event.setStatus("new");
                event.setEventMessage(message.getText().toString());
                event.setDateTime(dateTime.getText().toString());
                event.setPostedBy(DatabaseService.fetchID());
                event.setGID(gid);
                DatabaseService.insertnewEvent(event);
                Message message = new Message();
                message.setEventID(s_id);
                message.setGid(gid);
                message.setLocalID(new LocalIdGen().nextLocalId());
                message.setType("event");
                message.setPollID("null");
                message.setMid("null");
                message.setSender(DatabaseService.fetchID());
                message.setMessage(event.getEventMessage());
                DatabaseService.insertMessage(message);
            }
        });

    }
}
