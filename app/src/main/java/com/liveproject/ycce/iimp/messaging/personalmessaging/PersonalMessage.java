package com.liveproject.ycce.iimp.messaging.personalmessaging;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Laptop on 21-12-2016.
 */
public class PersonalMessage implements Serializable {
    private String message;

    private String Sender;

    private String pmid;
    private String subject;
    private String Date;
    private String localID;

    private ArrayList<String> receivers;

    public PersonalMessage(){

    }



    public String getSender() {
        return Sender;
    }

    public String getMessage() {
        return message;
    }

    public String getPmid() {
        return pmid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }



    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }
}
