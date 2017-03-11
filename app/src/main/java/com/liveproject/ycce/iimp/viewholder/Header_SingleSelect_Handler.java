package com.liveproject.ycce.iimp.viewholder;

import android.os.Parcel;

import com.thoughtbot.expandablecheckrecyclerview.models.SingleCheckExpandableGroup;

import java.util.List;

/**
 * Created by Tiger on 03-03-2017.
 */

public class Header_SingleSelect_Handler extends SingleCheckExpandableGroup {

    public Header_SingleSelect_Handler(String title, List items) {
        super(title, items);
    }

    protected Header_SingleSelect_Handler(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Header_SingleSelect_Handler> CREATOR = new Creator<Header_SingleSelect_Handler>() {
        @Override
        public Header_SingleSelect_Handler createFromParcel(Parcel in) {
            return new Header_SingleSelect_Handler(in);
        }

        @Override
        public Header_SingleSelect_Handler[] newArray(int size) {
            return new Header_SingleSelect_Handler[size];
        }
    };
}