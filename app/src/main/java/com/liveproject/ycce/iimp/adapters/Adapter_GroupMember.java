package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.headers.Header_Members;
import com.liveproject.ycce.iimp.adapters.viewholder.child.ViewHolder_GroupMember;
import com.liveproject.ycce.iimp.adapters.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 16-03-2017.
 */

public class Adapter_GroupMember  extends ExpandableRecyclerViewAdapter<ViewHolder_Header, ViewHolder_GroupMember> {

    public Adapter_GroupMember(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ViewHolder_Header onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_header, parent, false);
        return new ViewHolder_Header(view);
    }

    @Override
    public ViewHolder_GroupMember onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child, parent, false);
        return new ViewHolder_GroupMember(view);
    }

    @Override
    public void onBindChildViewHolder(ViewHolder_GroupMember holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final MemberPersonalInfo memberPersonalInfo = ((Header_Members) group).getItems().get(childIndex);
        holder.setName(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
        if(memberPersonalInfo.getG_role().equalsIgnoreCase("Admin"))
        {
            holder.setRole("(A)");
        } else if(memberPersonalInfo.getG_role().equalsIgnoreCase("Normal")){
            holder.setRole("(N)");
        }else if(memberPersonalInfo.getG_role().equalsIgnoreCase("Management")){
            holder.setRole("(M)");
        }
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder_Header holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setParentTitle(group);
    }
}