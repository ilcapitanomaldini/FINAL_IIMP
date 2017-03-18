package com.liveproject.ycce.iimp.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.NullAdapter;
import com.liveproject.ycce.iimp.R;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class NewsActivity extends AppCompatActivity {
    private NewsRAdapter adapter;
    private ArrayList<News> messagelist;


    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            tv_title.setText("News");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("Toolbar", "onCreate: " + e.toString());
        }

        rv = (RecyclerView) findViewById(R.id.rv_news);
        llm = new LinearLayoutManager(this);

        //Trial to reverse the layout.
        //llm.setReverseLayout(true);
       // llm.setStackFromEnd(true);

        rv.setLayoutManager(llm);

        //Populate Arraylist with current messages.
        messagelist = new ArrayList<News>();
        messagelist = DatabaseService.fetchNews();

        //Call adapter for recyclerview layout by passing the arraylist.
        if(messagelist!=null) {
            adapter = new NewsRAdapter(messagelist);
            rv.setAdapter(adapter);
        }
        else
        {
            ArrayList<String> strings = new ArrayList<String>();
            strings.add("No News!");
            strings.add("Neither Good nor Bad!");
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
    public void getmessages()
    {
        //sqlite function call code to fetch messages.
       // messagelist = DBService.fetchMessages(currentgid);
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
                    getmessages();
                }
            }
        });
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
