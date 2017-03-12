package com.liveproject.ycce.iimp.messaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.NullAdapter;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;

import java.util.ArrayList;

/**
 * Created by Laptop on 25-10-2016.
 */
public class Fragment_Messaging extends Fragment {

    public Fragment_Messaging() {

    }


    private Adapter_PersonalMessageList adapter;
    private ArrayList<PersonalMessage> messagelist;

    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rv = (RecyclerView) getView().findViewById(R.id.rv_personal_messaging);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        //Trial to reverse the layout.
        //llm.setReverseLayout(true);
        // llm.setStackFromEnd(true);

        rv.setLayoutManager(llm);

        //Populate Arraylist with current messages.
        messagelist = new ArrayList<PersonalMessage>();
        messagelist = DatabaseService.fetchPersonalMessages();
        if(messagelist!=null) {
            //Call adapter for recyclerview layout by passing the arraylist.
            adapter = new Adapter_PersonalMessageList(messagelist);
            rv.setAdapter(adapter);
        }
        else
        {
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("No Messages!");
            rv.setAdapter(new NullAdapter(strings));
        }
        setRecyclerViewScrollListener();
    }

    /*  @Override
      protected void onStart() {
          super.onStart();
          if(messagelist.size()==0)
              getmessages(currentgid);
      }*/
    public void getmessages() {
        //sqlite function call code to fetch messages.
        // messagelist = DBService.fetchMessages(currentgid);
    }


    private int getLastVisible() {
        return llm.findLastVisibleItemPosition();
    }


    private void setRecyclerViewScrollListener() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = rv.getLayoutManager().getItemCount();
                if (totalItemCount == getLastVisible() + 1) {
                    //Here, add the function that would fetch the new data from sqlite.
                    getmessages();
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_personal_message, container, false);
    }
}
