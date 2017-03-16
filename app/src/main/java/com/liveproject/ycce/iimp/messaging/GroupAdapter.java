package com.liveproject.ycce.iimp.messaging;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.messaging.groupmessaging.Activity_Messaging;
import com.liveproject.ycce.iimp.messaging.groupmessaging.GroupClass;

import java.util.ArrayList;

/**
 * Created by Laptop on 11-03-2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GroupClass> groups;

    public GroupAdapter(ArrayList<GroupClass> groupClasses){this.groups = groupClasses;}


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_groups,parent,false);
        return new GroupHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupClass group = groups.get(position);
        GroupHolder viewHolder = (GroupHolder) holder;
        viewHolder.bindMessage(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    //Manages the receiver layout of Text type
    public static class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.


        private TextView grouptitle;
        private ImageView iv_1,iv_2,iv_3;
        private String gid,gname,grole;

        public GroupHolder(View itemView) {
            super(itemView);

            grouptitle = (TextView) itemView.findViewById(R.id.tv_mygroups_title);
            iv_1 = (ImageView) itemView.findViewById(R.id.iv_mygroups_1);
            iv_2 = (ImageView) itemView.findViewById(R.id.iv_mygroups_2);
            iv_3 = (ImageView) itemView.findViewById(R.id.iv_mygroups_3);
            itemView.setOnClickListener(this);
        }

        public void bindMessage(GroupClass n) {
            //Actual binding function.
            //Change the inner textview values
            grouptitle.setText(n.getGName());
            grole = n.getGRole();
            gid = n.getGid();
            gname = n.getGName();
            if(grole.equals(Constants.GROUPROLES[1]))
            {
                iv_3.setVisibility(View.GONE);
            }
            else if (grole.equals(Constants.GROUPROLES[2]))
            {
                iv_3.setVisibility(View.GONE);
                iv_2.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(),Activity_Messaging.class);
            i.putExtra("gid",gid);
            i.putExtra("gname",gname);
            i.putExtra("grole",grole);
            v.getContext().startActivity(i);
        }
    }

}
