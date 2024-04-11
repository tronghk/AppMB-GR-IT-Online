package com.example.appgrit.models;

import java.util.Date;

public class SharePostModel {
    private String UserId;
    private String PostId;
    private String Content;
    private Date TimeShare;

    // Constructors, getters, and setters

    public SharePostModel() {
    }

    public SharePostModel(String userId, String postId, String content, Date timeShare) {
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

    public Date getTimeShare() {
        return TimeShare;
    }

    public void setTimeShare(Date timeShare) {
        TimeShare = timeShare;
    }
}
