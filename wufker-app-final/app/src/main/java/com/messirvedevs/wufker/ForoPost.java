package com.messirvedevs.wufker;

public class ForoPost {
    private String category;
    private String title;
    private String authorEmail;
    private String content;


    public ForoPost() {
    }

    public ForoPost(String category, String title, String authorEmail, String content) {
        this.category = category;
        this.title = title;
        this.authorEmail = authorEmail;
        this.content = content;
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
}
