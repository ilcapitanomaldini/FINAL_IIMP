package com.liveproject.ycce.iimp.messaging.groupmessaging;

import java.io.Serializable;

/**
 * Created by Laptop on 21-12-2016.
 */
public class Message implements Serializable {
    private String message;

    private String Sender;
    private String gid;
    private String mid;

    private String type;
    private String EventID;
    private String PollID;
    private String LocalID;


    public Message(){

    }

    public String getGid() {
        return gid;
    }

    public String getSender() {
        return Sender;
    }

    public String getMessage() {
        return message;
    }

    public String getMid() {
        return mid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getPollID() {
        return PollID;
    }

    public void setPollID(String pollID) {
        PollID = pollID;
    }

    public String getLocalID() {
        return LocalID;
    }

    public void setLocalID(String localID) {
        LocalID = localID;
    }
}
