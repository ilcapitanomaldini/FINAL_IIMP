package com.liveproject.ycce.iimp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.adapters.headers.Header_Members;
import com.liveproject.ycce.iimp.adapters.viewholder.header.ViewHolder_Header;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 06-03-2017.
 */

public class Adapter_MemberWithClose extends ExpandableRecyclerViewAdapter<ViewHolder_Header, Adapter_MemberWithClose.ViewHolder_MemberWithClose> {

    List<MemberPersonalInfo> memberPersonalInfoList = new ArrayList<>();

    public Adapter_MemberWithClose(List<? extends ExpandableGroup> groups) {
        super(groups);
        memberPersonalInfoList = groups.get(0).getItems();
    }

    @Override
    public ViewHolder_Header onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_header, parent, false);
        return new ViewHolder_Header(view);
    }

    @Override
    public ViewHolder_MemberWithClose onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child_member_with_cancel, parent, false);
        return new ViewHolder_MemberWithClose(view);
    }

    @Override
    public void onBindChildViewHolder(ViewHolder_MemberWithClose holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final MemberPersonalInfo memberPersonalInfo = ((Header_Members) group).getItems().get(childIndex);
        holder.setChildTitle(memberPersonalInfo.getFirstname() + " " + memberPersonalInfo.getLastname());
    }

    @Override
    public void onBindGroupViewHolder(ViewHolder_Header holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setParentTitle(group);
    }

    public void delete(int position){
        if(position == 1)
            position--;
        memberPersonalInfoList.remove(position);
        notifyItemRemoved(position);
    }

    public List<MemberPersonalInfo> getMemberPersonalInfoList(){
        return memberPersonalInfoList;
    }

    public class ViewHolder_MemberWithClose extends ChildViewHolder {
        private TextView tv_member;
        private ImageButton im_close;

        @SuppressWarnings("deprecation")
        public ViewHolder_MemberWithClose(View itemView) {
            super(itemView);
            tv_member = (TextView) itemView.findViewById(R.id.list_item_member_name);
            im_close = (ImageButton) itemView.findViewById(R.id.list_item_close);
            im_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getPosition());

                }
            });
        }

        public void setChildTitle(String title) {
            tv_member.setText(title);
        }
    }
}