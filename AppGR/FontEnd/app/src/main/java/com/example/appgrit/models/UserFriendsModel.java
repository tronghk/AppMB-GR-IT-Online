package com.example.appgrit.models;

public class UserFriendsModel {
    private String userId;
    private String userFriendId;
    private String Status;

    // Constructor mặc định
    public UserFriendsModel() {
    }
    
    public UserFriendsModel(String userId, String userFriendId, String status) {
        this.userId = userId;
        this.userFriendId = userFriendId;
        this.Status = status;
    }
    // Getter cho userId
    public String getUserId() {
        return userId;
    }

    // Setter cho userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter cho userFriendId
    public String getUserFriendId() {
        return userFriendId;
    }

    // Setter cho userFriendId
    public void setUserFriendId(String userFriendId) {
        this.userFriendId = userFriendId;
    }

    // Getter cho Status
    public String getStatus() {
        return Status;
    }

    // Setter cho Status
    public void setStatus(String status) {
        this.Status = status;
    }
}
