package com.example.appgrit.models;

public class UserFollowsModel {
    private String UserId;
    private String UserFollowId;

    public UserFollowsModel() {
    }

    public UserFollowsModel(String userId, String userFollowId) {
        UserId = userId;
        UserFollowId = userFollowId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserFollowId() {
        return UserFollowId;
    }

    public void setUserFollowId(String userFollowId) {
        UserFollowId = userFollowId;
    }
}
