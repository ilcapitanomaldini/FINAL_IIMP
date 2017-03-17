package com.liveproject.ycce.iimp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liveproject.ycce.iimp.MemberPersonalInfo;
import com.liveproject.ycce.iimp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 17-03-2017.
 */

public class Adapter_RoleSelection extends RecyclerView.Adapter<Adapter_RoleSelection.ViewHolder_RoleSelection> {
    private List<MemberPersonalInfo> selectedMembers = new ArrayList<>();
    String[] selectedRoles;
    public Adapter_RoleSelection(List<MemberPersonalInfo> memberPersonalInfoList) {
        selectedMembers = memberPersonalInfoList;
        selectedRoles = new String[selectedMembers.size()];
    }

    @Override
    public ViewHolder_RoleSelection onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_role_selection, parent, false);
        return new ViewHolder_RoleSelection(inflatedView);
    }


    @Override
    public void onBindViewHolder(ViewHolder_RoleSelection holder, final int position) {
        holder.tv_name.setText(selectedMembers.get(position).getFirstname() + " "+ selectedMembers.get(position).getLastname());

        holder.rb_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoles[position] ="Admin";
            }
        });


        holder.rb_managment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoles[position] = "Management";
            }
        });

        holder.rb_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoles[position] = "Normal";
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedMembers.size();
    }

    public String[] getSelectedRoles() {
        return selectedRoles;
    }

    class ViewHolder_RoleSelection extends RecyclerView.ViewHolder {

        TextView tv_name;
        RadioGroup rg_roles;
        RadioButton rb_admin, rb_managment, rb_normal;

        public ViewHolder_RoleSelection(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.list_item_name);
            rg_roles = (RadioGroup) itemView.findViewById(R.id.list_item_rg_roles);
            rb_admin = (RadioButton) itemView.findViewById(R.id.list_item_rb_admin);
            rb_managment = (RadioButton) itemView.findViewById(R.id.list_item_rb_management);
            rb_normal = (RadioButton) itemView.findViewById(R.id.list_item_rb_normal);
        }
    }
}
