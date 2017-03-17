package com.liveproject.ycce.iimp.creation.groups.customizedgroup;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.Adapter_RoleSelection;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.messaging.Activity_Home_Messaging;
import com.liveproject.ycce.iimp.networkservice.PostService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 17-03-2017.
 */

public class Activity_RoleSelection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_RoleSelection adapter_roleSelection;
    private List<MemberPersonalInfo> selectedMembers = new ArrayList<>();
    private String[] selectedRoles;
    private Toast toast;

    private String s_gid;
    private String URL;

    BroadcastReceiver roleselection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_title.setText("Select Role");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        selectedMembers = getIntent().getParcelableArrayListExtra("SELECTED_MEMBERS");
        s_gid = getIntent().getStringExtra("GID");
        selectedRoles = new String[selectedMembers.size()];
        recyclerView = (RecyclerView) findViewById(R.id.rs_rv);
        adapter_roleSelection = new Adapter_RoleSelection(selectedMembers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter_roleSelection);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            selectedRoles = adapter_roleSelection.getSelectedRoles();
            if (selectedRoles.length != selectedMembers.size()) {
                toast.makeText(getBaseContext(), "Select role for every member.", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject params = new JSONObject();
                try {
                    params.put("gid", s_gid);
                    JSONArray contacts = new JSONArray();
                    for (int i = 0; i < selectedMembers.size(); i++) {
                        JSONObject member = new JSONObject();
                        member.put("cid", selectedMembers.get(i).getId());
                        member.put("role", selectedRoles[i]);
                        contacts.put(i, member);
                    }
                    params.put("contacts", contacts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                URL = Constants.SITE_URL + Constants.ADDGROUPMEMBERS_URL;
                Intent intent = new Intent(getBaseContext(), PostService.class);
                Log.d("PARAMS", "onOptionsItemSelected: " + params);
                intent.putExtra("URL", URL);
                intent.putExtra("NAME", "RoleSelection");
                intent.putExtra("JSONOBJECT", params.toString());
                getBaseContext().startService(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class RoleSelection extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                if (response.equalsIgnoreCase("\"true\"")) {
                    Intent i = new Intent(getBaseContext(), Activity_Home_Messaging.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toast.makeText(getBaseContext(), "Group created. Sent towards handler for approval.", Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                } else if (response.equalsIgnoreCase("\"false\"")) {
                    toast.makeText(getBaseContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        roleselection = new RoleSelection();
        this.registerReceiver(roleselection, new IntentFilter("android.intent.action.RoleSelection"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(roleselection);
    }
}
