package com.liveproject.ycce.iimp.viewholder.child;

import android.view.View;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by Tiger on 28-02-2017.
 */

public class ViewHolder_Child extends ChildViewHolder {

    private TextView childTextView;

    public ViewHolder_Child(View itemView) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_child_name);
    }

    public void setArtistName(String name) {
        childTextView.setText(name);
    }
}