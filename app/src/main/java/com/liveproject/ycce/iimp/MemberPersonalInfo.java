package com.liveproject.ycce.iimp;

import android.os.Parcel;
import android.os.Parcelable;

import com.liveproject.ycce.iimp.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiger on 26-10-2016.
 */
public class MemberPersonalInfo implements Parcelable {
    private String id, firstname, lastname, dob, gender, doj, mobileno, emailid;
    private String designation, division;
    private List<String> roles;
    private String handler_id, handler_firstname, handler_lastname, status;


    public MemberPersonalInfo() {
        this.id = null;
        this.firstname = null;
        this.lastname = null;
        this.dob = null;
        this.gender = null;
        this.doj = null;
        this.mobileno = null;
        this.emailid = null;
        this.designation = null;
        this.division = null;
        this.handler_id = null;
        this.handler_firstname = null;
        this.handler_lastname = null;
        this.roles = new ArrayList<>();
        this.status = null;
    }

    public MemberPersonalInfo(String id, String firstname, String lastname, String emailid, String mobileno, String gender, String dob, String doj, String designation, String division, String handler_id, String handler_firstname, String handler_lastname, List roles, String status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        this.doj = doj;
        this.mobileno = mobileno;
        this.emailid = emailid;

        this.designation = designation;
        this.division = division;
        this.handler_id = handler_id;
        this.handler_firstname = handler_firstname;
        this.handler_lastname = handler_lastname;
        this.roles = roles;
        this.status = status;
    }

    public MemberPersonalInfo(String firstname, String lastname, String emailid, String mobileno,  String gender, String dob, String doj, String designation, String division, List<String> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        this.doj = doj;
        this.mobileno = mobileno;
        this.emailid = emailid;
        this.designation = designation;
        this.division = division;
        this.roles = roles;
    }

    public MemberPersonalInfo(MemberPersonalInfo mpi) {
        this.id = mpi.getId();
        this.firstname = mpi.getFirstname();
        this.lastname = mpi.getLastname();
        this.dob = mpi.getDob();
        this.gender = mpi.getGender();
        this.doj = mpi.getDoj();
        this.mobileno = mpi.getMobileno();
        this.emailid = mpi.getEmailid();
        this.designation = mpi.getDesignation();
        this.division = mpi.getDivision();
        this.handler_id = mpi.getHandler_id();
        this.handler_firstname = mpi.getHandler_firstname();
        this.handler_lastname = mpi.getHandler_lastname();
        this.roles = mpi.getRoles();
        this.status = mpi.getStatus();
    }

    public MemberPersonalInfo(Parcel parcel) {
        this.id = parcel.readString();
        this.firstname = parcel.readString();
        this.lastname = parcel.readString();
        this.dob = parcel.readString();
        this.gender = (parcel.readByte() == 1 ? Constants.GENDER[0] : Constants.GENDER[1]);
        this.doj = parcel.readString();
        this.mobileno = parcel.readString();
        this.emailid = parcel.readString();

        this.designation = parcel.readString();
        this.division = parcel.readString();
        this.handler_id = parcel.readString();
        this.handler_firstname = parcel.readString();
        this.handler_lastname = parcel.readString();
        this.roles = parcel.readArrayList(null);
        this.status=parcel.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getHandler_id() {
        return handler_id;
    }

    public void setHandler_id(String handler_id) {
        this.handler_id = handler_id;
    }

    public String getHandler_firstname() {
        return handler_firstname;
    }

    public void setHandler_firstname(String handler_firstname) {
        this.handler_firstname = handler_firstname;
    }

    public String getHandler_lastname() {
        return handler_lastname;
    }

    public void setHandler_lastname(String handler_lastname) {
        this.handler_lastname = handler_lastname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final Creator<MemberPersonalInfo> CREATOR = new Creator<MemberPersonalInfo>() {
        @Override
        public MemberPersonalInfo createFromParcel(Parcel in) {
            return new MemberPersonalInfo(in);
        }

        @Override
        public MemberPersonalInfo[] newArray(int size) {
            return new MemberPersonalInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(dob);
        dest.writeByte(gender == Constants.GENDER[0] ? (byte) 1 : (byte) 0);
        dest.writeString(doj);
        dest.writeString(mobileno);
        dest.writeString(emailid);

        dest.writeString(designation);
        dest.writeString(division);
        dest.writeString(handler_id);
        dest.writeString(handler_firstname);
        dest.writeString(handler_lastname);
        dest.writeList(roles);
        dest.writeString(status);
    }
}