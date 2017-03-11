package com.liveproject.ycce.iimp.messaging;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.messaging.personalmessaging.Activity_PersonalMessageDetails;
import com.liveproject.ycce.iimp.messaging.personalmessaging.PersonalMessage;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class Adapter_PersonalMessageList extends RecyclerView.Adapter<Adapter_PersonalMessageList.PersonalMessageHolder> {
    private ArrayList<PersonalMessage> messagelist;

    Adapter_PersonalMessageList(ArrayList<PersonalMessage> ml) {
        messagelist = ml;
    }


    @Override
    public Adapter_PersonalMessageList.PersonalMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Here the view is inflated as and when the adapter is set in the other class.
        //The code to differentiate between the two chat layouts could be potentially inserted here.

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child_card_pm, parent, false);
        return new PersonalMessageHolder(inflatedView);
    }


    @Override
    public void onBindViewHolder(Adapter_PersonalMessageList.PersonalMessageHolder holder, int position) {
        //Binds each message object in the list to the individual items in the view.
        PersonalMessage message = messagelist.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public static class PersonalMessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.
        private TextView tv_subject;
        private String pmid;
        private TextView tv_date;
        private TextView tv_sender;
        private String body;

        public PersonalMessageHolder(View itemView) {
            super(itemView);
            tv_sender = (TextView) itemView.findViewById(R.id.cv_pm_from);
            tv_date = (TextView) itemView.findViewById(R.id.cv_pm_date);
            tv_subject = (TextView) itemView.findViewById(R.id.cv_pm_subject);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //OnClick on an individual message in the view. Currently, an activity(blank) opens up.
            //Better if onHold or the like would be used.
            Context context = itemView.getContext();
            Intent showIntent = new Intent(context, Activity_PersonalMessageDetails.class);
            showIntent.putExtra("pmid", pmid);
            showIntent.putExtra("subject", tv_subject.getText().toString());
            showIntent.putExtra("body", body);
            context.startActivity(showIntent);
        }

        public void bindMessage(PersonalMessage m) {
            //Actual binding function.
            pmid = m.getPmid();
            tv_sender.setText(m.getSender());
            tv_date.setText(m.getDate());
            tv_subject.setText(m.getSubject());
            body = m.getMessage();
        }
    }
}
