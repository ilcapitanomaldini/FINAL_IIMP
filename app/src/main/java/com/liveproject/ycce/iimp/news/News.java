package com.liveproject.ycce.iimp.news;

import java.io.Serializable;

/**
 * Created by Laptop on 21-12-2016.
 */
public class News implements Serializable {
    private String message;
    private String title;

    private String poster;


    private String image_loc;

    private String nid;
    private String tags;
    private String datePosted;
    private String LocalID;

    public News() {

    }





    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getImage_loc() {
        return image_loc;
    }

    public void setImage_loc(String image_loc) {
        this.image_loc = image_loc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalID() {
        return LocalID;
    }

    public void setLocalID(String localID) {
        LocalID = localID;
    }
}