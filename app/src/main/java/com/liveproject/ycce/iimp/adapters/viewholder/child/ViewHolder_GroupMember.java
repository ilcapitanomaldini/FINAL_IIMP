package com.liveproject.ycce.iimp.adapters.viewholder.child;

import android.view.View;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by Tiger on 16-03-2017.
 */

public class ViewHolder_GroupMember extends ChildViewHolder {

    private TextView tv_name, tv_role;

    public ViewHolder_GroupMember(View itemView) {
        super(itemView);
        tv_name = (TextView) itemView.findViewById(R.id.list_item_child_name);
        tv_role = (TextView) itemView.findViewById(R.id.list_item_child_role);
    }

    public void setName(String name) {
        tv_name.setText(name);
    }
    public void setRole(String name){ tv_role.setText(name);}
}