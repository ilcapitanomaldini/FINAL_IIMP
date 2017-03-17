package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tiger on 16-03-2017.
 */

public class GroupCondition implements Parcelable {
    String designation, role, division, doj, dob, city, gender, doj_timing, dob_timing;

    public GroupCondition(String designation, String role, String division, String city, String gender, String doj, String dob, String doj_timing, String dob_timing) {
        this.designation = designation;
        this.role = role;
        this.division = division;
        this.doj = doj;
        this.dob = dob;
        this.city = city;
        this.gender = gender;
        this.doj_timing = doj_timing;
        this.dob_timing = dob_timing;
    }

    protected GroupCondition(Parcel in) {
        designation = in.readString();
        role = in.readString();
        division = in.readString();
        doj = in.readString();
        dob = in.readString();
        city = in.readString();
        gender = in.readString();
        doj_timing = in.readString();
        dob_timing = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(designation);
        dest.writeString(role);
        dest.writeString(division);
        dest.writeString(doj);
        dest.writeString(dob);
        dest.writeString(city);
        dest.writeString(gender);
        dest.writeString(doj_timing);
        dest.writeString(dob_timing);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupCondition> CREATOR = new Creator<GroupCondition>() {
        @Override
        public GroupCondition createFromParcel(Parcel in) {
            return new GroupCondition(in);
        }

        @Override
        public GroupCondition[] newArray(int size) {
            return new GroupCondition[size];
        }
    };


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoj_timing() {
        return doj_timing;
    }

    public void setDoj_timing(String doj_timing) {
        this.doj_timing = doj_timing;
    }

    public String getDob_timing() {
        return dob_timing;
    }

    public void setDob_timing(String dob_timing) {
        this.dob_timing = dob_timing;
    }
}
