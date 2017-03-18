package com.liveproject.ycce.iimp.userprofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.JSONService;
import com.liveproject.ycce.iimp.Member;
import com.liveproject.ycce.iimp.MemberAddress;
import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.networkservice.GetService;
import com.liveproject.ycce.iimp.pendingrequests.PRActivity;

import java.util.List;

/**
 * Created by Tiger on 27-02-2017.
 */

public class Activity_UserProfile extends AppCompatActivity {

    TextView tv_designation, tv_mobile, tv_email, tv_division, tv_handler, tv_address, tv_dob, tv_doj, tv_gender, tv_roles;
    Toast toast;
    BroadcastReceiver userprofile, pendingrequestaccepted, pendingrequestrejected;
    LinearLayout l1;
    FloatingActionButton fab_accept, fab_reject;
    ProgressBar progressBar;

    String s_roles, s_id, s_prid;
    String URL;

    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.up_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        s_id = getIntent().getStringExtra("CID");
        s_prid = getIntent().getStringExtra("PRID");

        tv_designation = (TextView) findViewById(R.id.cp_tv_designation);
        tv_mobile = (TextView) findViewById(R.id.cp_tv_mobile);
        tv_email = (TextView) findViewById(R.id.cp_tv_email);
        tv_division = (TextView) findViewById(R.id.cp_tv_division);
        tv_handler = (TextView) findViewById(R.id.cp_tv_handler);
        tv_address = (TextView) findViewById(R.id.cp_tv_address);
        tv_doj = (TextView) findViewById(R.id.cp_tv_doj);
        tv_dob = (TextView) findViewById(R.id.cp_tv_dob);
        tv_gender = (TextView) findViewById(R.id.cp_tv_gender);
        tv_roles = (TextView) findViewById(R.id.cp_tv_roles);
        progressBar = (ProgressBar) findViewById(R.id.up_pb);

        if (s_id != null) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.up_fab);
            fab.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            URL = Constants.SITE_URL + Constants.SEARCHCONTACT_URL + "?id=" + s_id;
            Intent intent = new Intent(getBaseContext(), GetService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "UserProfile");
            getBaseContext().startService(intent);
            if (s_prid != null) {
                l1 = (LinearLayout) findViewById(R.id.cp_ll_pending_request);
                l1.setVisibility(View.VISIBLE);
                fab_accept = (FloatingActionButton) findViewById(R.id.cp_fab_accept);
                fab_reject = (FloatingActionButton) findViewById(R.id.cp_fab_reject);

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

        } else {
            member = DatabaseService.getMember(DatabaseService.fetchID());
            MemberPersonalInfo memberPersonalInfo = new MemberPersonalInfo(member.getMemberPersonalInfo());
            MemberAddress memberAddress = new MemberAddress(member.getMemaddr());
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.up_ctl);
            collapsingToolbarLayout.setTitle(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
            collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);

            tv_designation.setText(memberPersonalInfo.getDesignation());
            tv_mobile.setText(memberPersonalInfo.getMobileno());
            tv_email.setText(memberPersonalInfo.getEmailid());
            tv_division.setText(memberPersonalInfo.getDivision());
            tv_handler.setText(memberPersonalInfo.getHandler_firstname() + " " + memberPersonalInfo.getHandler_lastname());
            tv_gender.setText(memberPersonalInfo.getGender());
            tv_dob.setText(memberPersonalInfo.getDob());
            tv_doj.setText(memberPersonalInfo.getDoj());
            List<String> roles = memberPersonalInfo.getRoles();
            for (int i = 0; i < roles.size(); i++) {
                if (i == 1) {
                    s_roles = roles.get(i);
                } else {
                    s_roles = s_roles + ", " + roles.get(i);
                }
            }
            if (s_roles != null) {
                tv_roles.setText(s_roles);
            } else {
                tv_roles.setText("No Extra roles.");
            }

            tv_address.setText(memberAddress.getAddrline() + ", " + memberAddress.getStreet() + ", "
                    + memberAddress.getLocality() + ", " + memberAddress.getCity() + ", "
                    + memberAddress.getState() + ", " + memberAddress.getCountry() + " - "
                    + memberAddress.getPincode());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.up_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent("com.liveproject.persi.ycce.iimp.EDIT_PROFILE");
                    i.putExtra("Member", member);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userprofile = new UserProfile();
        this.registerReceiver(userprofile, new IntentFilter("android.intent.action.UserProfile"));
        pendingrequestaccepted = new PendingRequestAccepted();
        this.registerReceiver(pendingrequestaccepted, new IntentFilter("android.intent.action.PendingRequestAccepted"));
        pendingrequestrejected = new PendingRequestRejected();
        this.registerReceiver(pendingrequestrejected, new IntentFilter("android.intent.action.PendingRequestRejected"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(userprofile);
        this.unregisterReceiver(pendingrequestaccepted);
        this.unregisterReceiver(pendingrequestrejected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private class UserProfile extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
                finish();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                member = jsonService.parseSearchByID();

                MemberPersonalInfo memberPersonalInfo = new MemberPersonalInfo(member.getMemberPersonalInfo());
                MemberAddress memberAddress = new MemberAddress(member.getMemaddr());
                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.up_ctl);
                collapsingToolbarLayout.setTitle(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
                collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);

                tv_designation.setText(memberPersonalInfo.getDesignation());
                tv_mobile.setText(memberPersonalInfo.getMobileno());
                tv_email.setText(memberPersonalInfo.getEmailid());
                tv_division.setText(memberPersonalInfo.getDivision());
                tv_handler.setText(memberPersonalInfo.getHandler_firstname() + " " + memberPersonalInfo.getHandler_lastname());
                tv_gender.setText(memberPersonalInfo.getGender());
                tv_dob.setText(memberPersonalInfo.getDob());
                tv_doj.setText(memberPersonalInfo.getDoj());
                List<String> roles = memberPersonalInfo.getRoles();
                for (int i = 0; i < roles.size(); i++) {
                    if (i == 1) {
                        s_roles = roles.get(i);
                    } else {
                        s_roles = s_roles + ", " + roles.get(i);
                    }
                }
                if (s_roles != null) {
                    tv_roles.setText(s_roles);
                } else {
                    tv_roles.setText("No Extra roles.");
                }

                tv_address.setText(memberAddress.getAddrline() + ", " + memberAddress.getStreet() + ", "
                        + memberAddress.getLocality() + ", " + memberAddress.getCity() + ", "
                        + memberAddress.getState() + ", " + memberAddress.getCountry() + " - "
                        + memberAddress.getPincode());
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
}
