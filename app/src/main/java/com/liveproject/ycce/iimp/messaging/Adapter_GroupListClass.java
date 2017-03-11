package com.liveproject.ycce.iimp.messaging;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;

/**
 * Created by Laptop on 26-10-2016.
 */
public class Adapter_GroupListClass extends ArrayAdapter<String> {

    private String[] gnames;


    private Activity context;

    public Adapter_GroupListClass(Activity context, String[] gnames) {
        super(context, R.layout.list_view_layout, gnames);
        this.context = context;
        this.gnames = gnames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.group_list_layout, null, true);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.gll_tv_gnames);
        textViewId.setText(gnames[position]);
        return listViewItem;
    }
}
