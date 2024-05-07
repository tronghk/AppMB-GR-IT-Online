package com.example.appgrit.models;

import com.google.gson.annotations.SerializedName;

public class UserFollowsModel {
    @SerializedName("userId")
    private String UserId;
    @SerializedName("userFollowId")
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
