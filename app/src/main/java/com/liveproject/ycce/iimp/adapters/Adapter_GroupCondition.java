package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.GroupCondition;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.headers.Header_GroupCondition;
import com.liveproject.ycce.iimp.adapters.viewholder.child.ViewHolder_Child;
import com.liveproject.ycce.iimp.adapters.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 16-03-2017.
 */

public class Adapter_GroupCondition extends ExpandableRecyclerViewAdapter<ViewHolder_Header, ViewHolder_Child> {

    public Adapter_GroupCondition(List<? extends ExpandableGroup> groups) {
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
        String conditions = null;
        final GroupCondition groupCondition = ((Header_GroupCondition) group).getItems().get(childIndex);
        if (!groupCondition.getDesignation().isEmpty()) {
            conditions = "Designation : " + groupCondition.getDesignation() + "\n";
        }
        if (!groupCondition.getDivision().isEmpty()) {
            conditions = "Division : " + groupCondition.getDivision() + "\n";
        }
        if (!groupCondition.getCity().isEmpty()) {
            conditions = "City : " + groupCondition.getCity() + "\n";
        }
        if (!groupCondition.getGender().isEmpty()) {
            conditions = "Gender : " + groupCondition.getGender() + "\n";
        }
        if (!groupCondition.getRole().isEmpty()) {
            conditions = "Role : " + groupCondition.getRole() + "\n";
        }
        if (!groupCondition.getDob().isEmpty()) {
            conditions = "Dob : " + groupCondition.getDob_timing() + " " + groupCondition.getDob() + "\n";
        }
        if (!groupCondition.getDoj().isEmpty()) {
            conditions = "Doj : " + groupCondition.getDoj_timing() + " " + groupCondition.getDoj();
        }

        holder.setName(conditions);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder_Header holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setParentTitle(group);
    }
}