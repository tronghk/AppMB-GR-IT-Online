package com.example.appgrit.models;

public class UserFollowsModel {
    private String UserId;
    private String UserName;
    private String UserImage;

    // Constructors, getters, and setters

    public UserFollowsModel() {
    }

    public UserFollowsModel(String userId, String userName, String userImage) {
        UserId = userId;
        UserName = userName;
        UserImage = userImage;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
