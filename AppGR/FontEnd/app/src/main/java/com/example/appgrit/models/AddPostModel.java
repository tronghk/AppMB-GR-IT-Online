package com.example.appgrit.models;

import java.util.Date;

public class AddPostModel {
    private String UserId;
    private String Content;
    private Date PostTime;

    // Constructors, getters, and setters

    public AddPostModel() {
    }

    public AddPostModel(String userId, String content, Date postTime) {
        UserId = userId;
        Content = content;
        PostTime = postTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getPostTime() {
        return PostTime;
    }

    public void setPostTime(Date postTime) {
        PostTime = postTime;
    }
}
