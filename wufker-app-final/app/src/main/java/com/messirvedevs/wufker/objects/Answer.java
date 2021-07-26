package com.messirvedevs.wufker.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Answer {
    private String id;
    private String username;
    private String content;
    private String postId;
    private Date datetime;
    private int votes;
    private HashMap<String, Boolean> voters;
    private ArrayList<String> badges;

    public Answer() {
    }

    public Answer( String username, String content, String postId, Date datetime, int votes, HashMap<String, Boolean> voters, ArrayList<String> badges) {
        this.username = username;
        this.content = content;
        this.postId = postId;
        this.datetime = datetime;
        this.votes = votes;
        this.voters = voters;
        this.badges = badges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getVotes() {
        return votes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public HashMap<String, Boolean> getVoters() {
        return voters;
    }

    public void setVoters(HashMap<String, Boolean> voters) {
        this.voters = voters;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<String> badges) {
        this.badges = badges;
    }
}
