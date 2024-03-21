package com.example.appgrit.models;

public class DeletePostModel {
    private String UserId;
    private String PostId;

    // Constructors, getters, and setters

    public DeletePostModel() {
    }

    public DeletePostModel(String userId, String postId) {
        UserId = userId;
        PostId = postId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }
}
