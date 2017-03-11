package com.liveproject.ycce.iimp.messaging.groupmessaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.viewholder.Header_Members;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tiger on 28-02-2017.
 */

public class Activity_Group_Details extends AppCompatActivity {

    public Adapter_Group_Members adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_gd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_gd);
        collapsingToolbarLayout.setTitle("YCCE");
        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        List<MemberPersonalInfo> memberlist = getMemberList();
        Header_Members headerMembers1 = new Header_Members("Members",memberlist);
        List<Header_Members> headerMembers = Arrays.asList(headerMembers1);

        adapter = new Adapter_Group_Members(headerMembers);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<MemberPersonalInfo> getMemberList() {
        List<String> roles = new ArrayList<>();
        MemberPersonalInfo m1 = new MemberPersonalInfo("1","Mayur","Dongare","mayurbdongare99@gmail.com","1111","Male","1995-09-05","2017-05-05","HOD","CT","AAA","FF","LL",roles , "Status");
        MemberPersonalInfo m2 = new MemberPersonalInfo("1","Mayur","Dongare","mayurbdongare99@gmail.com","1111","Male","1995-09-05","2017-05-05","HOD","CT","AAA","FF","LL",roles , "Status");
        MemberPersonalInfo m3 = new MemberPersonalInfo("1","Mayur","Dongare","mayurbdongare99@gmail.com","1111","Male","1995-09-05","2017-05-05","HOD","CT","AAA","FF","LL",roles , "Status");
        return Arrays.asList(m1,m2,m3);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
}