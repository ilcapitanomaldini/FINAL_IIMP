package com.liveproject.ycce.iimp.creation.personalmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.networkservice.PostService;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Tiger on 03-03-2017.
 */

public class Activity_CreationPersonalMessage extends AppCompatActivity {

    EditText et_subject, et_body;
    String s_subject, s_body;
    String URL;
    Toast toast;

    BroadcastReceiver personalmessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_personal_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("Create Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_subject = (EditText) findViewById(R.id.cpm_et_subject);
        et_body = (EditText) findViewById(R.id.cpm_et_body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {
            s_subject = et_subject.getText().toString();
            s_body = et_body.getText().toString();

            if (Validation.isEmpty(s_subject)) {
                toast.makeText(getBaseContext(), "Please enter subject.", Toast.LENGTH_SHORT).show();
            } else if (Validation.isEmpty(s_body)) {
                toast.makeText(getBaseContext(), "Please enter the body of message", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("INTO", "onOptionsItemSelected: " + "ENTERED");
                URL = Constants.SITE_URL + Constants.PERSONALMESSAGE_URL;
                JSONObject params = new JSONObject();
                try {
                    params.put("fromcid", DatabaseService.fetchID());
                    params.put("subject", s_subject);
                    params.put("message", s_body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("PARAMS", "onOptionsItemSelected: " + params);
                Intent intent = new Intent(getBaseContext(), PostService.class);
                intent.putExtra("URL", URL);
                intent.putExtra("NAME", "PersonalMessage");
                intent.putExtra("JSONOBJECT", params.toString());
                getBaseContext().startService(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class PersonalMessage extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"false\"")) {
                    toast.makeText(getBaseContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    response = response.substring(1, response.length() - 1);
                    Intent i = new Intent(getBaseContext(), Activity_DistributionList_PersonalMessage.class);
                    i.putExtra("PMID", response);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalmessage = new PersonalMessage();
        this.registerReceiver(personalmessage, new IntentFilter("android.intent.action.PersonalMessage"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(personalmessage);
    }
}
