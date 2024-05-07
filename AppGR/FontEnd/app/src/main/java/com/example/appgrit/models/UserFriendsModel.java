package com.example.appgrit.models;

public class UserFriendsModel {
    private String userId;
    private String userFriendId;
    private String Status;

    public UserFriendsModel() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UserFriendsModel(String userId, String userFriendId, String status) {
        this.userId = userId;
        this.userFriendId = userFriendId;
        this.Status = status;
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
