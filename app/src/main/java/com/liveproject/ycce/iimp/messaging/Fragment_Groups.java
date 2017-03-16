package com.liveproject.ycce.iimp.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.NullAdapter;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.creation.groups.Activity_GroupCreation;
import com.liveproject.ycce.iimp.messaging.groupmessaging.GroupClass;

import java.util.ArrayList;

/**
 * Created by Laptop on 25-10-2016.
 */
public class Fragment_Groups extends Fragment {
    public Fragment_Groups(){

    }
    ListView lv;
    private GroupAdapter adapter;
    private ArrayList<GroupClass> grouplist;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private FloatingActionButton fab_add_groups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        String s[];
        String my_mobile=DBService.fetchMobileNo();
        GroupClass gc[] = DBService.getMyGroups(my_mobile);
        s = new String[gc.length];
        for(int i=0;i<gc.length;i++)
        {
            s[i] = gc[i].getGName();
        }

        //Toast.makeText(Activity_MyGroups.this, s[0], Toast.LENGTH_LONG).show();

        lv = (ListView) getView().findViewById(R.id.lv_my_groups);


        GroupListClass cl = new GroupListClass(getActivity(), s);
        lv.setAdapter(cl);
        //Here, I try to make the list "listen" to clicks.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String group_Name = ((TextView)view.findViewWithTag("id")).getText().toString();
                String group_Id = DBService.fetchgidbygname(group_Name);
                Intent i = new Intent(getActivity().getApplicationContext(),MessagingActivity.class);
                i.putExtra("gid",group_Id);
                startActivity(i);
            }
        });
        */
        rv = (RecyclerView) getActivity().findViewById(R.id.rv_messaging);
        llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);

        grouplist = new ArrayList<GroupClass>();
        grouplist = DatabaseService.getMyGroups(DatabaseService.fetchMobileNo());
        if(grouplist!=null) {
            adapter = new GroupAdapter(grouplist);
            rv.setAdapter(adapter);
        }
        else{
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("No Groups Joined!");
            rv.setAdapter(new NullAdapter(strings));
        }
        setRecyclerViewScrollListener();

        fab_add_groups = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_groups);
        fab_add_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), Activity_GroupCreation.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        grouplist = new ArrayList<GroupClass>();
        grouplist = DatabaseService.getMyGroups(DatabaseService.fetchMobileNo());
        if(grouplist!=null) {
            adapter = new GroupAdapter(grouplist);
            rv.setAdapter(adapter);
        }
        else{
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("No Groups Joined!");
            rv.setAdapter(new NullAdapter(strings));
        }
        rv.invalidate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_mygroups,container,false);
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
                    //getmessages(currentgid);
                }
            }
        });
    }
}