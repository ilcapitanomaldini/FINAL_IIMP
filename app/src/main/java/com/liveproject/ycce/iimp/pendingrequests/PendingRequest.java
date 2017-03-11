package com.liveproject.ycce.iimp.pendingrequests;

import java.io.Serializable;

/**
 * Created by Laptop on 21-12-2016.
 */
public class PendingRequest implements Serializable {


    private String uid_poster;
    private String uid_handler;
    private String uid_poster_id;

    private String additional_info;

    private String prid;
    private String type;


    public PendingRequest() {

    }


    public String getUid_poster() {
        return uid_poster;
    }

    public void setUid_poster(String uid_poster) {
        this.uid_poster = uid_poster;
    }

    public String getUid_handler() {
        return uid_handler;
    }

    public void setUid_handler(String uid_handler) {
        this.uid_handler = uid_handler;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getPrid() {
        return prid;
    }

    public void setPrid(String prid) {
        this.prid = prid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid_poster_id() {
        return uid_poster_id;
    }

    public void setUid_poster_id(String uid_poster_id) {
        this.uid_poster_id = uid_poster_id;
    }
}