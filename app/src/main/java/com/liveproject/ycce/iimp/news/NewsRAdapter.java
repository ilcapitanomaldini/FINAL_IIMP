package com.liveproject.ycce.iimp.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;

import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class NewsRAdapter extends RecyclerView.Adapter<NewsRAdapter.NewsHolder> {
    private ArrayList<News> messagelist;
    NewsRAdapter(ArrayList<News> ml)
    {
        messagelist = ml;
    }


    @Override
    public NewsRAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Here the view is inflated as and when the adapter is set in the other class.
        //The code to differentiate between the two chat layouts could be potentially inserted here.

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_child_news_pm, parent, false);
        return new NewsHolder(inflatedView);
    }


    @Override
    public void onBindViewHolder(NewsRAdapter.NewsHolder holder, int position) {
        //Binds each message object in the list to the individual items in the view.
        News message = messagelist.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    public static class NewsHolder extends RecyclerView.ViewHolder
    {
        //This class handles the mapping of each view and the individual messages according to the layout.
        //HINT :: Any changes made to the layout of an individual message should also be reflected here.
        private TextView tv_title;
        private String nid;
        private TextView tv_date;
        private TextView tv_message;
        private ImageView iv_news;

        public NewsHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.cv_news_title);
            tv_date = (TextView) itemView.findViewById(R.id.cv_news_date);
            tv_message = (TextView) itemView.findViewById(R.id.cv_news_message);
            iv_news = (ImageView) itemView.findViewById(R.id.iv_news_image);

            //itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View v) {
            //OnClick on an individual message in the view. Currently, an activity(blank) opens up.
            //Better if onHold or the like would be used.
            Context context = itemView.getContext();
            Intent showIntent = new Intent(context, MDActivity.class);
            showIntent.putExtra("pmid",pmid);
            context.startActivity(showIntent);
        }*/
        public void bindMessage(News n) {
            //Actual binding function.
            nid=n.getNid();
            tv_title.setText(n.getTitle());
            tv_date.setText(n.getDatePosted());
            tv_message.setText(n.getMessage());
           // File file = new File(n);
            iv_news.setImageURI(android.net.Uri.parse(n.getImage_loc()));


            // For now a default image is being set here.


            //iv_news.setImageDrawable(R.drawable.send_selected);
        }
    }
}
