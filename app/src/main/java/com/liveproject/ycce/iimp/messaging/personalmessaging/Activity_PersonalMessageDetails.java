package com.liveproject.ycce.iimp.messaging.personalmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;

/**
 * Created by Tiger on 07-03-2017.
 */

public class Activity_PersonalMessageDetails extends AppCompatActivity {

    TextView tv_subject,tv_body;
    String s_subject,s_body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_message_details);

        Intent intent = getIntent();
        s_subject =intent.getStringExtra("subject");
        s_body = intent.getStringExtra("body");

        tv_subject = (TextView) findViewById(R.id.pmd_tv_subject);
        tv_body = (TextView) findViewById(R.id.pmd_tv_body);

        tv_subject.setText(s_subject);
        tv_body.setText(s_body);
    }
}
