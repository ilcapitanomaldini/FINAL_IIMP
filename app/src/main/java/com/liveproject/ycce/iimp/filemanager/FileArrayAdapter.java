package com.liveproject.ycce.iimp.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liveproject.ycce.iimp.R;

import java.io.File;
import java.util.List;

/**
 * Created by Laptop on 15-02-2017.
 */
public class FileArrayAdapter extends ArrayAdapter<File> {
    private Context mContext; //Activity context.
    private int mResource; //Represents our rows as an int
    private List<File> mObjects; //The List of objects we got from our model.

    public FileArrayAdapter(Context c, int res, List<File> o) {
        super(c, res, o);
        mContext = c;
        mResource = res;
        mObjects = o;
    }

    public FileArrayAdapter(Context c, int res) {
        super(c, res);
        mContext = c;
        mResource = res;
    }

    /*Does exactly what it looks like.  Pulls out a specific File Object at a specified index.*/
    @Override
    public File getItem(int i) {
        return mObjects.get(i);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*This is the entire file [list_rowl] with its RelativeLayout, ImageView, and two
        TextViews.  It will always be null the very first time, so we need to inflate it with a
           LayoutInflater.*/
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));

            v = inflater.inflate(mResource, null);
        }

        /* We pull out the ImageView and TextViews so we can set their properties.*/
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        TextView nameView = (TextView) v.findViewById(R.id.name_text_view);

        TextView detailsView = (TextView) v.findViewById(R.id.details_text_view);

        File file = getItem(position);

        /* If the file is a dir, set the image view's image to a folder, else, a file. */
        if (file.isDirectory()) {
            iv.setImageResource(R.drawable.directory_icon);
        } else {
            iv.setImageResource(R.drawable.file_icon);
            if (file.length() > 0) {
                detailsView.setText(String.valueOf(file.length()));
            }
        }

        //Finally, set the name of the file or directory.
        nameView.setText(file.getName());

        //Send the view back so the ListView can show it as a row, the way we modified it.
        return v;
    }
}
