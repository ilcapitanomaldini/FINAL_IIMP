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
import android.view.View;
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

import java.util.List;

/**
 * Created by Tiger on 27-02-2017.
 */

public class Activity_UserProfile extends AppCompatActivity {

    TextView tv_designation, tv_mobile, tv_email, tv_division, tv_handler, tv_address, tv_dob, tv_doj, tv_gender, tv_roles;
    Toast toast;
    BroadcastReceiver userprofile;

    String s_roles, s_id;
    String URL;

    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        if (s_id != null) {
            URL = Constants.SITE_URL + Constants.SEARCHCONTACT_URL + "?id=" + s_id;
            Intent intent = new Intent(getBaseContext(), GetService.class);
            intent.putExtra("URL", URL);
            intent.putExtra("NAME", "UserProfile");
            getBaseContext().startService(intent);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_up);
            fab.setVisibility(View.GONE);
        } else {
            member = DatabaseService.getMember(DatabaseService.fetchID());
            MemberPersonalInfo memberPersonalInfo = new MemberPersonalInfo(member.getMemberPersonalInfo());
            MemberAddress memberAddress = new MemberAddress(member.getMemaddr());
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_up);
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

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_up);
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
        this.registerReceiver(userprofile, new IntentFilter("android.intent.action.OTPReceiver"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(userprofile);
    }

    private class UserProfile extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra("Result");
            String error = intent.getStringExtra("Error");

            if (error != null) {
                toast.makeText(getBaseContext(), "Please check your internet connection!!", Toast.LENGTH_LONG).show();
            } else if (response != null) {
                JSONService jsonService = new JSONService(response);
                member = jsonService.parseSearchByID();
                toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();

                MemberPersonalInfo memberPersonalInfo = new MemberPersonalInfo(member.getMemberPersonalInfo());
                MemberAddress memberAddress = new MemberAddress(member.getMemaddr());
                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_up);
                collapsingToolbarLayout.setTitle(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
                collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);

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
                tv_roles.setText(s_roles);

                tv_address.setText(memberAddress.getAddrline() + ", " + memberAddress.getStreet() + ", "
                        + memberAddress.getLocality() + ", " + memberAddress.getCity() + ", "
                        + memberAddress.getState() + ", " + memberAddress.getCountry() + " - "
                        + memberAddress.getPincode());
            }
        }
    }
}
