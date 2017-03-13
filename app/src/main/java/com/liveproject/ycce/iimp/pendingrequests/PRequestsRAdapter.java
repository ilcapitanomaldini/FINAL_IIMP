package com.liveproject.ycce.iimp.pendingrequests;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.userprofile.Activity_UserProfile;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class PRequestsRAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PendingRequest> prlist;
    private enum type{USER("USER",0),GROUP("GROUP",1);
        private String value;
        private int intvalue;
        type(String value, int intvalue)
        {
            this.value = value;
            this.intvalue = intvalue;
        }

        public int getValue()
        {
            return intvalue;
        }
        public String getStringValue()
        {
            return value;
        }
    }


    PRequestsRAdapter(ArrayList<PendingRequest> ml)
    {
        prlist = ml;
    }


    @Override
    public int getItemViewType(int position) {

        if(prlist.get(position).getType().equals(type.USER.getStringValue()))
            return type.USER.getValue();
        else if(prlist.get(position).getType().equals(type.GROUP.getStringValue()))
            return type.GROUP.getValue();
        return 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Here the view is inflated as and when the adapter is set in the other class.
        //The code to differentiate between the two chat layouts could be potentially inserted here.
        View inflatedView;
        if( viewType == type.USER.getValue()) {
             inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_pending_request_user, parent, false);
            return new PRUserHolder(inflatedView);

        }
        else
        {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_pending_request_group, parent, false);
            return new PRGroupHolder(inflatedView);

        }
       // return new PRUserHolder(inflatedView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Binds each message object in the list to the individual items in the view.

        if(holder.getItemViewType()== type.USER.getValue())
        {
            PendingRequest message = prlist.get(position);
            PRUserHolder viewHolder = (PRUserHolder) holder;
            viewHolder.bindMessage(message);
        }
        else if(holder.getItemViewType()== type.GROUP.getValue())
        {
            PendingRequest message = prlist.get(position);
            PRGroupHolder viewHolder = (PRGroupHolder) holder;
            viewHolder.bindMessage(message);
        }
    }

    @Override
    public int getItemCount() {
        return prlist.size();
    }

    public static class PRUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.


        private TextView pr_user_name;
        private TextView pr_user_designation;
        private String cid, prid;


        public PRUserHolder(View itemView) {
            super(itemView);

            pr_user_name = (TextView) itemView.findViewById(R.id.tv_pr_user_name);
            pr_user_designation = (TextView) itemView.findViewById(R.id.tv_pr_user_designation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //For User Type
            //Sends cid, prid
            Context context = itemView.getContext();
            Intent showIntent = new Intent(context, Activity_UserProfile.class);
            showIntent.putExtra("cid",cid);
            showIntent.putExtra("prid",prid);
            context.startActivity(showIntent);
        }
        public void bindMessage(PendingRequest n) {
            //Actual binding function.
            //Change the inner textview values
            pr_user_name.setText(n.getUid_poster());
            prid = n.getPrid();
            cid = n.getUid_poster_id();
            pr_user_designation.setText(n.getType());
        }
    }

    public static class PRGroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.
        private TextView pr_group_name;
        private String gid, prid;

        public PRGroupHolder(View itemView) {
            super(itemView);
            pr_group_name = (TextView) itemView.findViewById(R.id.tv_pr_group_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            /*Intent showIntent = new Intent(context, Activity_Group_Details.class);
            showIntent.putExtra("gid",gid);
            showIntent.putExtra("prid",prid);
            context.startActivity(showIntent);*/
        }
        public void bindMessage(PendingRequest n) {
            //Actual binding function.
            prid = n.getPrid();
            gid = n.getAdditional_info();
            pr_group_name.setText(n.getAdditional_info());
        }
    }
}
