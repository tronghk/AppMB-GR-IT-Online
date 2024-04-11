package com.example.appgrit.models;

public class UserFriendsModel {
    private String UserId;
    private String UserFriendId;

    public UserFriendsModel() {
    }

    public UserFriendsModel(String userId, String userFriendId) {
        UserId = userId;
        UserFriendId = userFriendId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserFriendId() {
        return UserFriendId;
    }

    public void setUserFriendId(String userFriendId) {
        UserFriendId = userFriendId;
    }
}
