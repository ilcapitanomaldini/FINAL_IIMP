package com.liveproject.ycce.iimp.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Activity_Messaging;
import com.liveproject.ycce.iimp.messaging.groupmessaging.GroupClass;

/**
 * Created by Laptop on 25-10-2016.
 */
public class Fragment_Groups extends Fragment {
    public Fragment_Groups(){

    }
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String s[];
        String my_mobile= DatabaseService.fetchMobileNo();
        GroupClass gc[] = DatabaseService.getMyGroups(my_mobile);
        s=new String[gc.length];
        for(int i=0;i<gc.length;i++)
        {
            s[i] = gc[i].getGName();
        }

        //Toast.makeText(Activity_MyGroups.this, s[0], Toast.LENGTH_LONG).show();

        lv = (ListView) getView().findViewById(R.id.lv_my_groups);


        Adapter_GroupListClass cl = new Adapter_GroupListClass(getActivity(), s);
        lv.setAdapter(cl);
        //Here, I try to make the list "listen" to clicks.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String group_Name = ((TextView)view.findViewWithTag("id")).getText().toString();
                String group_Id = DatabaseService.fetchgidbygname(group_Name);
                Intent i = new Intent(getActivity().getApplicationContext(),Activity_Messaging.class);
                i.putExtra("gid",group_Id);
                startActivity(i);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_child_my_groups,container,false);
    }
}
