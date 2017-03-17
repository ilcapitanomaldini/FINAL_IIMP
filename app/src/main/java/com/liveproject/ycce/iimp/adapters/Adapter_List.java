package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.headers.Header_Members;
import com.liveproject.ycce.iimp.adapters.viewholder.child.ViewHolder_Child;
import com.liveproject.ycce.iimp.adapters.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 28-02-2017.
 */

public class Adapter_List extends ExpandableRecyclerViewAdapter<ViewHolder_Header, ViewHolder_Child> {

    public Adapter_List(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ViewHolder_Header onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_header, parent, false);
        return new ViewHolder_Header(view);
    }

    @Override
    public ViewHolder_Child onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child, parent, false);
        return new ViewHolder_Child(view);
    }

    @Override
    public void onBindChildViewHolder(ViewHolder_Child holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final MemberPersonalInfo memberPersonalInfo = ((Header_Members) group).getItems().get(childIndex);
        holder.setName(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder_Header holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setParentTitle(group);
    }
}
