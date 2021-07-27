package com.messirvedevs.wufker.objects;

import java.util.Date;

public class ForoPost {
    private String id;
    private String category;
    private String title;
    private String authorEmail;
    private String content;
    private Date timestamp;


    public ForoPost() {
    }

    public ForoPost(String category, String title, String authorEmail, String content, String id, Date timestamp) {
        this.category = category;
        this.title = title;
        this.authorEmail = authorEmail;
        this.content = content;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }
    public String setId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
