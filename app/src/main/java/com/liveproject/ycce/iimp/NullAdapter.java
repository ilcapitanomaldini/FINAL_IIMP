package com.liveproject.ycce.iimp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Laptop on 10-03-2017.
 */

public class NullAdapter extends RecyclerView.Adapter<NullAdapter.NullHolder> {
    private ArrayList<String> messagelist;
    public NullAdapter(ArrayList<String> ml)
    {
        messagelist = ml;
    }
    @Override
    public NullAdapter.NullHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Here the view is inflated as and when the adapter is set in the other class.
        //The code to differentiate between the two chat layouts could be potentially inserted here.

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_null_message, parent, false);
        return new NullAdapter.NullHolder(inflatedView);
    }


    @Override
    public void onBindViewHolder(NullAdapter.NullHolder holder, int position) {
        //Binds each message object in the list to the individual items in the view.
        String s = messagelist.get(position);
        holder.bindMessage(s);
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }


    public static class NullHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.
        private TextView tv_message;
        public NullHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_null_message);
        }

        public void bindMessage(String m) {
            //Actual binding function.
           tv_message.setText(m);
        }
    }
}
