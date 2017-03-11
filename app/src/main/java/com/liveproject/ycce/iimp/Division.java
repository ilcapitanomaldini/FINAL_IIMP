package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiger on 04-03-2017.
 */

public class Division implements Parcelable {
    String id, name;

    public String getId() {
        return id;
    }

    public Division(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Division(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Division> CREATOR = new Creator<Division>() {
        @Override
        public Division createFromParcel(Parcel in) {
            return new Division(in);
        }

        @Override
        public Division[] newArray(int size) {
            return new Division[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
