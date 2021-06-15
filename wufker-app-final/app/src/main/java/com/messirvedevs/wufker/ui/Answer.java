package com.messirvedevs.wufker.ui;

import java.sql.Timestamp;

public class Answer {
    private String username;
    private String content;
    private Timestamp datetime;
    private int votes;

    public Answer() {
    }

    public Answer(String username, String content, Timestamp datetime, int votes) {
        this.username = username;
        this.content = content;
        this.datetime = datetime;
        this.votes = votes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
