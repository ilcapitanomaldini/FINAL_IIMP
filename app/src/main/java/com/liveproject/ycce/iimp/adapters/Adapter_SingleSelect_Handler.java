package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.viewholder.Header_SingleSelect_Handler;
import com.liveproject.ycce.iimp.viewholder.child.ViewHolder_SingleSelect;
import com.liveproject.ycce.iimp.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 03-03-2017.
 */

public class Adapter_SingleSelect_Handler extends
        CheckableChildRecyclerViewAdapter<ViewHolder_Header, ViewHolder_SingleSelect> {

    public Adapter_SingleSelect_Handler(List<Header_SingleSelect_Handler> groups) {
        super(groups);
    }

    @Override
    public ViewHolder_SingleSelect onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child_single_select, parent, false);
        return new ViewHolder_SingleSelect(view);
    }

    @Override
    public void onBindCheckChildViewHolder(ViewHolder_SingleSelect holder, int position,
                                           CheckedExpandableGroup group, int childIndex) {
        final MemberPersonalInfo artist = (MemberPersonalInfo) group.getItems().get(childIndex);
        holder.setChildTitle(artist.getFirstname() + " " + artist.getLastname());
    }

    @Override
    public ViewHolder_Header onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_header, parent, false);
        return new ViewHolder_Header(view);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder_Header holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setGenreTitle(group);
    }
}