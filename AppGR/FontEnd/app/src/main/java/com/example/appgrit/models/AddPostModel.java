package com.example.appgrit.models;

public class AddPostModel {
    private String UserId;
    private String Content;
    private String PostTime;

    // Constructors, getters, and setters

    public AddPostModel() {
    }

    public AddPostModel(String userId, String content, String postTime) {
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

    public String getPostTime() {
        return PostTime;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }
}
