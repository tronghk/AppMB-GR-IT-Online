package com.example.appgrit.models;

public class SharePostModel {
    private String UserId;
    private String PostId;
    private String Content;
    private String TimeShare;

    // Constructors, getters, and setters

    public SharePostModel() {
    }

    public SharePostModel(String userId, String postId, String content, String timeShare) {
        UserId = userId;
        PostId = postId;
        Content = content;
        TimeShare = timeShare;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTimeShare() {
        return TimeShare;
    }

    public void setTimeShare(String timeShare) {
        TimeShare = timeShare;
    }
}
