package com.liveproject.ycce.iimp.creation.groups;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Validation;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.creation.groups.conditionalgroup.Activity_DistributionList_ConditionalGroup;
import com.liveproject.ycce.iimp.creation.groups.customizedgroup.Activity_DistributionList_CustomizedGroup;
import com.liveproject.ycce.iimp.networkservice.PostService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tiger on 13-03-2017.
 */

public class Activity_GroupCreation extends AppCompatActivity {

    EditText et_groupname, et_groupdescription;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    Toast toast;

    String s_groupname, s_groupdescription, s_grouptype;
    String URL;

    BroadcastReceiver conditionalgroupcreation, customizedgroupcreation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("Group Creation");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }

        et_groupname = (EditText) findViewById(R.id.gc_et_groupname);
        et_groupdescription = (EditText) findViewById(R.id.gc_et_groupdescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

            // HINTS : COLLECT DATA FROM ALL FIELDS.
            getInfo();

            if (!(Validation.isEmpty(s_groupname) || Validation.isEmpty(s_groupdescription) || Validation.isEmpty(s_grouptype))) {
                JSONObject params = new JSONObject();
                try {
                    params.put("cid", DatabaseService.fetchID());
                    params.put("name", s_groupname);
                    params.put("type", s_grouptype);
                    params.put("description", s_groupdescription);
                } catch (JSONException e) {
                }
                URL = Constants.SITE_URL + Constants.CREATEGROUP_URL;
                if (s_grouptype.equalsIgnoreCase("Conditional")) {
                    Intent intent = new Intent(getBaseContext(), PostService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("JSONOBJECT", params.toString());
                    intent.putExtra("NAME", "ConditionalGroupCreation");
                    getBaseContext().startService(intent);
                } else if (s_grouptype.equalsIgnoreCase("Customized")) {
                    Intent intent = new Intent(getBaseContext(), PostService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("JSONOBJECT", params.toString());
                    intent.putExtra("NAME", "CustomizedGroupCreation");
                    getBaseContext().startService(intent);
                }
            } else {
                toast.makeText(getBaseContext(), "Missing Field(s) are detected. Fill all blanks!!!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class ConditionalGroupCreation extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                response = response.substring(1, response.length() - 1);
                Intent intent1 = new Intent(getBaseContext(), Activity_DistributionList_ConditionalGroup.class);
                intent1.putExtra("GID", response);
                startActivity(intent1);
            }
        }
    }

    private class CustomizedGroupCreation extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                response = response.substring(1, response.length() - 1);
                Intent intent1 = new Intent(getBaseContext(), Activity_DistributionList_CustomizedGroup.class);
                intent1.putExtra("GID", response);
                startActivity(intent1);
            }
        }
    }

    private void getInfo() {
        s_groupname = et_groupname.getText().toString();
        s_groupdescription = et_groupdescription.getText().toString();
        getType();
    }

    private void getType() {
        try {
            radioTypeGroup = (RadioGroup) findViewById(R.id.gc_rg_type);
            radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
            s_grouptype = radioTypeButton.getText().toString();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        conditionalgroupcreation = new ConditionalGroupCreation();
        this.registerReceiver(conditionalgroupcreation, new IntentFilter("android.intent.action.ConditionalGroupCreation"));
        customizedgroupcreation = new CustomizedGroupCreation();
        this.registerReceiver(customizedgroupcreation, new IntentFilter("android.intent.action.CustomizedGroupCreation"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(conditionalgroupcreation);
        this.unregisterReceiver(customizedgroupcreation);
    }
}
