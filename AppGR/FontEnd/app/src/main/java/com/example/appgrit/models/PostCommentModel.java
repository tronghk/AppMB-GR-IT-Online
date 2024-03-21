package com.example.appgrit.models;

public class PostCommentModel {
    private String PostId;
    private String UserId;
    private String Content;
    private String CommentTime;

    // Constructors, getters, and setters

    public PostCommentModel() {
    }

    public PostCommentModel(String postId, String userId, String content, String commentTime) {
        PostId = postId;
        UserId = userId;
        Content = content;
        CommentTime = commentTime;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
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

    public String getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(String commentTime) {
        CommentTime = commentTime;
    }
}
