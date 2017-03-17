package com.liveproject.ycce.iimp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liveproject.ycce.iimp.R;

/**
 * Created by Tiger on 17-03-2017.
 */

public class Adapter_RoleSelection extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_role_selection,parent,false);
        return new ViewHolder_RoleSelection(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder_RoleSelection extends RecyclerView.ViewHolder{

        public ViewHolder_RoleSelection(View itemView) {
            super(itemView);
        }
    }
}
