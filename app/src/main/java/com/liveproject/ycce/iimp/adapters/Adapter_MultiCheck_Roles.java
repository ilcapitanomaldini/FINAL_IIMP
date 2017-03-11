package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.Roles;
import com.liveproject.ycce.iimp.viewholder.Header_MultiCheck_Roles;
import com.liveproject.ycce.iimp.viewholder.child.ViewHolder_MultiChild;
import com.liveproject.ycce.iimp.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 02-03-2017.
 */

public class Adapter_MultiCheck_Roles extends CheckableChildRecyclerViewAdapter<ViewHolder_Header,ViewHolder_MultiChild> {

    public Adapter_MultiCheck_Roles(List<Header_MultiCheck_Roles> groups) {
        super(groups);
    }

    @Override
    public ViewHolder_MultiChild onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child_multi_select, parent, false);
        return new ViewHolder_MultiChild(view);
    }

    @Override
    public void onBindCheckChildViewHolder(ViewHolder_MultiChild holder, int position,
                                           CheckedExpandableGroup group, int childIndex) {
        final Roles artist = (Roles) group.getItems().get(childIndex);
        holder.setChildTitle(artist.getRolesName());
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
