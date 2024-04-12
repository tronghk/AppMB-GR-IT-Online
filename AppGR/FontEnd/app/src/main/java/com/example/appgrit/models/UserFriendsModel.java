package com.example.appgrit.models;

public class UserFriendsModel {
    private String userId;
    private String userFriendId;

    public UserFriendsModel() {
    }

    public UserFriendsModel(String userId, String userFriendId) {
        userId = userId;
        userFriendId = userFriendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        userId = userId;
    }

    public String getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(String userFriendId) {
        userFriendId = userFriendId;
    }
}
