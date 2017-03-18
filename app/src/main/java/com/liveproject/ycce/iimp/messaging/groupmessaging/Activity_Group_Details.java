package com.liveproject.ycce.iimp.messaging.groupmessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liveproject.ycce.iimp.GroupCondition;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.Adapter_GroupCondition;
import com.liveproject.ycce.iimp.adapters.Adapter_GroupMember;
import com.liveproject.ycce.iimp.adapters.Adapter_List;
import com.liveproject.ycce.iimp.adapters.headers.Header_GroupCondition;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.networkservice.GetService;
import com.liveproject.ycce.iimp.pendingrequests.PRActivity;
import com.liveproject.ycce.iimp.adapters.headers.Header_Members;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tiger on 28-02-2017.
 */

public class Activity_Group_Details extends AppCompatActivity {

    private String s_gid, s_prid, s_gname, s_group_description, s_createdby, s_type;
    String URL;
    private Adapter_GroupCondition adapter_groupCondition;
    private Adapter_GroupMember adapter_groupMember;
    LinearLayout l1;
    FloatingActionButton fab_accept, fab_reject;
    ProgressBar progressBar;
    Toast toast;
    BroadcastReceiver groupdescription, pendingrequestaccepted, pendingrequestrejected, conditionalgroup, customizedgroup;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        s_gid = getIntent().getStringExtra("GID");
        s_prid = getIntent().getStringExtra("PRID");
        s_gname = getIntent().getStringExtra("GNAME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.gd_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.gd_ctl);
        collapsingToolbarLayout.setTitle(s_gname);
        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        if (s_gid != null && s_prid != null) {
            l1 = (LinearLayout) findViewById(R.id.cgd_ll_pending_request);
            l1.setVisibility(View.VISIBLE);
            fab_accept = (FloatingActionButton) findViewById(R.id.cgd_fab_accept);
            fab_reject = (FloatingActionButton) findViewById(R.id.cgd_fab_reject);

            fab_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    URL = Constants.SITE_URL + Constants.PROCESSPENDINGREQUEST_URL + "?prid=" + s_prid + "&action=accept";
                    Intent intent = new Intent(getBaseContext(), GetService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", "PendingRequestAccepted");
                    getBaseContext().startService(intent);
                }
            });

            fab_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    URL = Constants.SITE_URL + Constants.PROCESSPENDINGREQUEST_URL + "?prid=" + s_prid + "&action=reject";
                    Intent intent = new Intent(getBaseContext(), GetService.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", "PendingRequestRejected");
                    getBaseContext().startService(intent);
                }
            });
        }

        URL = Constants.SITE_URL + Constants.GETGROUPDETAILS_URL + "?gid=" + s_gid;
        Intent intent = new Intent(getBaseContext(), GetService.class);
        intent.putExtra("URL", URL);
        intent.putExtra("NAME", "GroupDescription");
        getBaseContext().startService(intent);
    }

    private class GroupDescription extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                try {
                    JSONObject result = new JSONObject(response);
                    s_group_description = result.getString("Description__c");
                    s_type = result.getString("Group_Type__c");
                    JSONObject Creator = new JSONObject(result.getJSONObject("Created_by_Contact__r").toString());
                    s_createdby = Creator.getString("FirstName") + " " + Creator.getString("LastName");

                    if (s_type.equalsIgnoreCase("Conditional")) {
                        URL = Constants.SITE_URL + Constants.GETCONDITIONALGROUPCONDITIONS_URL + "?gid=" + s_gid;
                        Intent conditionalIntent = new Intent(getBaseContext(), GetService.class);
                        conditionalIntent.putExtra("URL", URL);
                        conditionalIntent.putExtra("NAME", "ConditionalGroup");
                        getBaseContext().startService(conditionalIntent);
                    } else if (s_type.equalsIgnoreCase("Customized")) {
                        URL = Constants.SITE_URL + Constants.GETCUSTOMIZEDGROUPMEMBERS_URL + "?gid=" + s_gid;
                        Intent customizedIntent = new Intent(getBaseContext(), GetService.class);
                        customizedIntent.putExtra("URL", URL);
                        customizedIntent.putExtra("NAME", "ConditionalGroup");
                        getBaseContext().startService(customizedIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConditionalGroup extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                List<GroupCondition> groupConditionList = jsonService.parseListOfGroupCondition();
                Header_GroupCondition header = new Header_GroupCondition("Conditions", groupConditionList);
                List<Header_GroupCondition> header_groupConditions = Arrays.asList(header);

                adapter_groupCondition = new Adapter_GroupCondition(header_groupConditions);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter_groupCondition);
            }
        }
    }

    private class CustomizedGroup extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                List<MemberPersonalInfo> memberlist = jsonService.parseListOfGroupMembers();
                Header_Members headerMembers1 = new Header_Members("Members", memberlist);
                List<Header_Members> headerMembers = Arrays.asList(headerMembers1);

                adapter_groupMember = new Adapter_GroupMember(headerMembers);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter_groupMember);
            }
        }
    }

    private class PendingRequestAccepted extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                Log.d("RESPONSE", "onReceive: " + response);
                if (response.equalsIgnoreCase("\"true\"")) {
                    Intent intent1 = new Intent(getBaseContext(), PRActivity.class);
                    toast.makeText(getBaseContext(), "Request Accepted!!", Toast.LENGTH_SHORT).show();
                    startActivity(intent1);
                } else {
                    toast.makeText(getBaseContext(), "Something went wrong. Please try again!!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class PendingRequestRejected extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                Log.d("RESPONSE", "onReceive: " + response);
                if (response.equalsIgnoreCase("\"true\"")) {
                    Intent intent1 = new Intent(getBaseContext(), PRActivity.class);
                    toast.makeText(getBaseContext(), "Request Rejected!!", Toast.LENGTH_SHORT).show();
                    startActivity(intent1);
                } else {
                    toast.makeText(getBaseContext(), "Something went wrong. Please try again!!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupdescription = new GroupDescription();
        this.registerReceiver(groupdescription, new IntentFilter("android.intent.action.GroupDescription"));
        conditionalgroup = new ConditionalGroup();
        this.registerReceiver(conditionalgroup, new IntentFilter("android.intent.action.ConditionalGroup"));
        customizedgroup = new CustomizedGroup();
        this.registerReceiver(customizedgroup, new IntentFilter("android.intent.action.CustomizedGroup"));
        pendingrequestaccepted = new PendingRequestAccepted();
        this.registerReceiver(pendingrequestaccepted, new IntentFilter("android.intent.action.PendingRequestAccepted"));
        pendingrequestrejected = new PendingRequestRejected();
        this.registerReceiver(pendingrequestrejected, new IntentFilter("android.intent.action.PendingRequestRejected"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(pendingrequestaccepted);
        this.unregisterReceiver(pendingrequestrejected);
        this.unregisterReceiver(groupdescription);
        this.unregisterReceiver(conditionalgroup);
        this.unregisterReceiver(customizedgroup);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter_groupCondition.onSaveInstanceState(outState);
        adapter_groupMember.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter_groupCondition.onRestoreInstanceState(savedInstanceState);
        adapter_groupMember.onRestoreInstanceState(savedInstanceState);
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