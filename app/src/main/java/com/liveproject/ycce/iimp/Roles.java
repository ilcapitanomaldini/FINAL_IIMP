package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiger on 01-03-2017.
 */

public class Roles implements Parcelable {
    private String rolesName;

    public Roles(String rolesName) {
        this.rolesName = rolesName;
    }

    protected Roles(Parcel in) {
        rolesName = in.readString();
    }

    public static final Creator<Roles> CREATOR = new Creator<Roles>() {
        @Override
        public Roles createFromParcel(Parcel in) {
            return new Roles(in);
        }

        @Override
        public Roles[] newArray(int size) {
            return new Roles[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rolesName);
    }
}