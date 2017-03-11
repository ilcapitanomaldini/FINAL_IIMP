package com.liveproject.ycce.iimp.polling;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Laptop on 01-02-2017.
 */
public class Poll implements Serializable {
    private String pid;
    private String gid;
    private String title;
    private int number_answers;
    private String creatorId;
    private String duration;
    private ArrayList<PollMapping> pm;

    public ArrayList<PollMapping> getPm() {
        return pm;
    }

    public void setPm(ArrayList<PollMapping> pm) {
        this.pm = pm;
    }

    public String getPid() {
        return pid;

    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber_answers() {
        return number_answers;
    }

    public void setNumber_answers(int number_answers) {
        this.number_answers = number_answers;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
