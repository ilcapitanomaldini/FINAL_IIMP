package com.liveproject.ycce.iimp.polling;

import java.io.Serializable;

/**
 * Created by Laptop on 01-02-2017.
 */
public class PollMapping implements Serializable {
    private String pid;
    private String aid;
    private String answerTitle;
    private String numberOfVotes;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(String numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }
}
