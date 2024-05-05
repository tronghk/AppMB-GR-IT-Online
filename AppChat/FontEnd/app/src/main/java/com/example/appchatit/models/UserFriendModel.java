package com.example.appchatit.models;

public class UserFriendModel {
    private String userId;
    private String userFriendId;

    public UserFriendModel() {
    }

    public UserFriendModel(String userId, String userFriendId) {
        this.userId = userId;
        this.userFriendId = userFriendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(String userFriendId) {
        this.userFriendId = userFriendId;
    }
}