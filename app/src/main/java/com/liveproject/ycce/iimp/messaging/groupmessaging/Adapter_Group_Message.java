package com.liveproject.ycce.iimp.messaging.groupmessaging;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.events.Event;
import com.liveproject.ycce.iimp.polling.Activity_PollResults;
import com.liveproject.ycce.iimp.polling.Poll;
import com.liveproject.ycce.iimp.polling.PollMapping;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class Adapter_Group_Message extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> messagelist;
    private enum type{TEXT_S("text",0),TEXT_R("text",1),EVENT("event",2),POLL("poll",3),IMAGE("image",4);
        private String value;
        private int intvalue;
        type(String value, int intvalue)
        {
            this.value = value;
            this.intvalue = intvalue;
        }

        public int getValue()
        {
            return intvalue;
        }
        public String getStringValue()
        {
            return value;
        }
    }


   public Adapter_Group_Message(ArrayList<Message> ml)
    {
        messagelist = ml;
    }


    @Override
    public int getItemViewType(int position) {

        if(messagelist.get(position).getType().equals(type.TEXT_S.getStringValue())&&messagelist.get(position).getSender().equals(DatabaseService.fetchID()))
            return type.TEXT_S.getValue();
        else if(messagelist.get(position).getType().equals(type.TEXT_R.getStringValue()))
        {
           return type.TEXT_R.getValue();}
        else if(messagelist.get(position).getType().equals(type.EVENT.getStringValue()))
            return type.EVENT.getValue();
        else if(messagelist.get(position).getType().equals(type.POLL.getStringValue()))
            return type.POLL.getValue();
        else if(messagelist.get(position).getType().equals(type.IMAGE.getStringValue()))
            return type.IMAGE.getValue();
        return 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Here the view is inflated as and when the adapter is set in the other class.
        //The code to differentiate between the two chat layouts could be potentially inserted here.
        View inflatedView;
        if( viewType == type.TEXT_S.getValue()) {
             inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_child_chatbubble_sender, parent, false);
            return new TextSHolder(inflatedView);

        }
        else if ( viewType == type.TEXT_R.getValue())
        {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_child_chatbubble, parent, false);
            return new TextRHolder(inflatedView);
        }
        else if ( viewType == type.EVENT.getValue())
        {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_child_group_message_event, parent, false);
            return new EventHolder(inflatedView);
        }
        else if ( viewType == type.POLL.getValue())
        {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_child_poll_participation, parent, false);
            return new PollHolder(inflatedView);
        }
        else
        {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_child_chatbubble, parent, false);
            return new TextRHolder(inflatedView);
        }

       // return new PRUserHolder(inflatedView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Binds each message object in the list to the individual items in the view.

        if(holder.getItemViewType()== type.TEXT_S.getValue())
        {
            Message message = messagelist.get(position);
            TextSHolder viewHolder = (TextSHolder) holder;
            viewHolder.bindMessage(message);
        }
        else if(holder.getItemViewType()== type.TEXT_R.getValue())
        {
            Message message = messagelist.get(position);
            TextRHolder viewHolder = (TextRHolder) holder;
            viewHolder.bindMessage(message);
        }
        else if(holder.getItemViewType()== type.EVENT.getValue())
        {
            Message message = messagelist.get(position);
            Event event = DatabaseService.fetchEvent(message.getEventID());
            EventHolder viewHolder = (EventHolder) holder;
            viewHolder.bindMessage(event);
        }
        else if(holder.getItemViewType()== type.POLL.getValue())
        {
            Message message = messagelist.get(position);
            Poll poll = DatabaseService.fetchPoll(message.getPollID());
            PollHolder viewHolder = (PollHolder) holder;
            viewHolder.bindMessage(poll);
        }
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    //Manages the Sender layout of Text type
    public static class TextSHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.


        private TextView messagetext;


        public TextSHolder(View itemView) {
            super(itemView);

            messagetext = (TextView) itemView.findViewById(R.id.message_text);

        }

        public void bindMessage(Message n) {
            //Actual binding function.
            //Change the inner textview values
         messagetext.setText(n.getMessage());
        }
    }

    //Manages the receiver layout of Text type
    public static class TextRHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.


        private TextView messagetext;
        private TextView messagesender;

        public TextRHolder(View itemView) {
            super(itemView);

            messagetext = (TextView) itemView.findViewById(R.id.message_text);
            messagesender = (TextView) itemView.findViewById(R.id.message_sender);
        }

        public void bindMessage(Message n) {
            //Actual binding function.
            //Change the inner textview values
            messagetext.setText(n.getMessage());
            messagesender.setText(n.getSender());
        }
    }

    //Manages the Event type
    public static class EventHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.


        private TextView eventmessage;
        private TextView datetime;
        private TextView duration;

        public EventHolder(View itemView) {
            super(itemView);

            eventmessage = (TextView) itemView.findViewById(R.id.gm_event_message);
            datetime = (TextView) itemView.findViewById(R.id.gm_event_datetime);
            duration = (TextView) itemView.findViewById(R.id.gm_event_duration);
        }

        public void bindMessage(Event n) {
            //Actual binding function.
            //Change the inner textview values
            eventmessage.setText(n.getEventMessage());
            datetime.setText(n.getDateTime());
            duration.setText(n.getDuration());
        }
    }

    //Manages the Poll type
    public static class PollHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.

        private int num;
        private TextView pollname;
        private Button select;
        private RadioGroup answer_group;
        private RadioButton rb1;
        private RadioButton rb2;
        private RadioButton rb3;
        private RadioButton rb4;

        public PollHolder(View itemView) {
            super(itemView);

            pollname = (TextView) itemView.findViewById(R.id.tv_poll_part_name);
            answer_group = (RadioGroup) itemView.findViewById(R.id.rg_poll_part);
            rb1 = (RadioButton) itemView.findViewById(R.id.rb_pollpart_1);
            rb2 = (RadioButton) itemView.findViewById(R.id.rb_pollpart_2);
            rb3 = (RadioButton) itemView.findViewById(R.id.rb_pollpart_3);
            rb4 = (RadioButton) itemView.findViewById(R.id.rb_pollpart_4);
            select = (Button) itemView.findViewById(R.id.bt_pollpart_select);
        }

        public void bindMessage(Poll n) {
            //Actual binding function.
            //Change the inner textview values
            final Poll poll = n;
            num = n.getNumber_answers();
            pollname.setText(n.getTitle());
            if("polled".equals(DatabaseService.checkPollStatus(n.getPid())))
            {
                select.setText("View Results");
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), Activity_PollResults.class);
                        intent.putExtra("Title",poll.getTitle());
                        ArrayList<PollMapping> pollMappings = DatabaseService.fetchPollMappingByPollID(poll.getPid());
                        try {
                            String[] result = new String[pollMappings.size()];
                            float[] resultvalues = new float[pollMappings.size()];
                            int i = 0;
                            for (PollMapping pollmap :
                                    pollMappings) {
                                result[i] = pollmap.getAnswerTitle();
                                resultvalues[i] = Float.parseFloat(pollmap.getNumberOfVotes());
                                i++;
                            }
                            intent.putExtra("Result", result);
                            intent.putExtra("ResultValue",resultvalues);
                        }catch (NullPointerException npe){npe.printStackTrace();}
                        itemView.getContext().startActivity(intent);
                    }
                });
                answer_group.setVisibility(View.GONE);
            }
            else {
                if (num == 2) {
                    rb1.setText(n.getPm().get(0).getAnswerTitle());
                    rb2.setText(n.getPm().get(1).getAnswerTitle());
                    rb3.setVisibility(View.GONE);
                    rb4.setVisibility(View.GONE);
                } else if (num == 3) {
                    rb1.setText(n.getPm().get(0).getAnswerTitle());
                    rb2.setText(n.getPm().get(1).getAnswerTitle());
                    rb3.setText(n.getPm().get(2).getAnswerTitle());
                    rb4.setVisibility(View.GONE);
                } else {
                    rb1.setText(n.getPm().get(0).getAnswerTitle());
                    rb2.setText(n.getPm().get(1).getAnswerTitle());
                    rb3.setText(n.getPm().get(2).getAnswerTitle());
                    rb4.setText(n.getPm().get(3).getAnswerTitle());
                }

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Add the sqlite entry into poll table here.
                        RadioButton rb_answer;
                        rb_answer = (RadioButton) itemView.findViewById(answer_group.getCheckedRadioButtonId());
                        String answer = rb_answer.getText().toString();
                        DatabaseService.updatePollMappingAnswerNumberByTitle(answer);
                        DatabaseService.setPollStatus("polled", poll.getPid());
                        answer_group.setVisibility(View.GONE);
                        select.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}
