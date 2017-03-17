package com.liveproject.ycce.iimp.adapters.viewholder.child;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import com.liveproject.ycce.iimp.R;
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;

/**
 * Created by Tiger on 03-03-2017.
 */

public class ViewHolder_SingleSelect extends CheckableChildViewHolder {

    private CheckedTextView childCheckedTextView;

    public ViewHolder_SingleSelect(View itemView) {
        super(itemView);
        childCheckedTextView =
                (CheckedTextView) itemView.findViewById(R.id.list_item_singlecheck);
    }

    @Override
    public Checkable getCheckable() {
        return childCheckedTextView;
    }

    public void setChildTitle(String title) {
        childCheckedTextView.setText(title);
    }
}